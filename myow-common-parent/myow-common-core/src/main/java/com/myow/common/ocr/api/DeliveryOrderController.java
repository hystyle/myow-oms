package com.myow.common.ocr.api;

import com.myow.common.ocr.DeliveryOrderParser;
import com.myow.common.ocr.DocumentParseException;
import com.myow.common.ocr.config.OcrConfig;
import com.myow.common.ocr.model.ParseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * DO 解析 HTTP 接口：文件入参，返回 21 字段 + 解析元信息 JSON。
 *
 * <p>文件类型自动识别：PDF 走文本层（稀疏时自动降级 OCR）；图片（PNG/JPG/TIFF 等）
 * 直接走 OCR 通道。OCR 通道依赖本机 Tesseract + tessdata，未配置时会在解析阶段抛错。</p>
 *
 * <p>本类仅依赖 {@code spring-web}（<b>provided</b> 作用域），由接入它的 Web 应用（如
 * myow-common-web / Spring Boot 主应用）在运行时提供，保持 common-core 不直接捆绑 Web 容器。</p>
 *
 * <p>就绪条件：接入方需对 {@code com.myow.common.ocr} 开启组件扫描。可选注入自定义
 * {@link OcrConfig}（如 setTessDataPath）覆盖默认构造。</p>
 */
@RestController
@RequestMapping("/api/delivery-orders")
public class DeliveryOrderController {

    private final DeliveryOrderParser parser;

    /** 默认配置（OCR 回退需本机 Tesseract） */
    public DeliveryOrderController() {
        this(OcrConfig.defaults());
    }

    /** 注入自定义配置（推荐：指定 tessDataPath 等） */
    public DeliveryOrderController(OcrConfig config) {
        this.parser = new DeliveryOrderParser(config);
    }

    /**
     * 解析上传的 DO 单据。
     *
     * @param file 必填，PDF 或扫描图片
     * @param dpi  可选，图片型扫描件识别 DPI（默认 300）
     */
    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> parse(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "dpi", defaultValue = "300") int dpi) {
        if (file == null || file.isEmpty()) {
            return bad(HttpStatus.BAD_REQUEST, "missing_file", "未上传文件或文件为空");
        }

        String name = file.getOriginalFilename();
        byte[] data;
        try {
            data = file.getBytes();
        } catch (IOException e) {
            return bad(HttpStatus.INTERNAL_SERVER_ERROR, "read_error", "读取上传文件失败: " + e.getMessage());
        }
        if (data.length == 0) {
            return bad(HttpStatus.BAD_REQUEST, "empty_file", "文件内容为空");
        }

        try {
            ParseResult result;
            if (isPdf(name, data)) {
                result = parser.parse(data);                 // 文本层优先，稀疏自动降级 OCR
            } else {
                BufferedImage img;
                try {
                    img = ImageIO.read(new ByteArrayInputStream(data));
                } catch (IOException e) {
                    return bad(HttpStatus.UNPROCESSABLE_ENTITY, "decode_error",
                            "图片解码失败: " + e.getMessage());
                }
                if (img == null) {
                    return bad(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "unsupported_type",
                            "无法识别的图片格式: " + name);
                }
                result = parser.parse(img, dpi);             // 直接走 OCR 通道
            }
            return ResponseEntity.ok(result);
        } catch (DocumentParseException e) {
            // 解析失败（如扫描件但本机无 Tesseract / 无有效文本层）
            return bad(HttpStatus.UNPROCESSABLE_ENTITY, "parse_failed", e.getMessage());
        }
    }

    /** 存活探针 */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(java.util.Map.of("status", "UP", "service", "delivery-order-ocr"));
    }

    // ==================== 内部 ====================

    private static boolean isPdf(String name, byte[] data) {
        if (name != null && name.toLowerCase().endsWith(".pdf")) {
            return true;
        }
        return data.length >= 4
                && data[0] == '%' && data[1] == 'P' && data[2] == 'D' && data[3] == 'F';
    }

    private static ResponseEntity<ApiError> bad(HttpStatus status, String code, String detail) {
        return ResponseEntity.status(status).body(new ApiError(code, detail));
    }

    /** 统一错误体 */
    public record ApiError(String code, String detail) {
    }
}
