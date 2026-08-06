package com.myow.common.ocr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.myow.common.ocr.config.OcrConfig;
import com.myow.common.ocr.engine.DeliveryOrderMapper;
import com.myow.common.ocr.extractor.OcrLayoutExtractor;
import com.myow.common.ocr.extractor.PdfTextLayoutExtractor;
import com.myow.common.ocr.layout.PageLayout;
import com.myow.common.ocr.model.ParseResult;
import com.myow.common.ocr.model.ParseSource;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: Delivery Order 解析门面。
 *
 * <p>典型用法：
 * <pre>{@code
 * DeliveryOrderParser parser = new DeliveryOrderParser(
 *         OcrConfig.defaults().setTessDataPath("/opt/homebrew/share/tessdata"));
 *
 * String json = parser.parseToJson(Paths.get("EZI-0022777-9-DO-1.pdf"));
 * }</pre>
 *
 * <p><b>通道选择策略</b>：优先走 PDF 文本层（零误差、毫秒级）；
 * 当文本层词数低于阈值（扫描件 / 图片型 PDF）时自动降级 OCR。
 * 两条通道产出同构的 {@link PageLayout}，下游解析逻辑完全一致。
 */
public class DeliveryOrderParser {

    private final OcrConfig config;
    private final ObjectMapper objectMapper;

    public DeliveryOrderParser() {
        this(OcrConfig.defaults());
    }

    public DeliveryOrderParser(OcrConfig config) {
        this.config = config == null ? OcrConfig.defaults() : config;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // ==================== 对外 API ====================

    public ParseResult parse(Path pdfPath) {
        try (InputStream in = Files.newInputStream(pdfPath)) {
            return parse(in);
        } catch (IOException e) {
            throw new DocumentParseException("读取文件失败: " + pdfPath, e);
        }
    }

    public ParseResult parse(File pdfFile) {
        return parse(pdfFile.toPath());
    }

    public ParseResult parse(byte[] pdfBytes) {
        long start = System.currentTimeMillis();
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            return parseDocument(document, start);
        } catch (IOException e) {
            throw new DocumentParseException("加载 PDF 失败", e);
        }
    }

    public ParseResult parse(InputStream pdfStream) {
        long start = System.currentTimeMillis();
        try (PDDocument document = PDDocument.load(pdfStream, MemoryUsageSetting.setupMainMemoryOnly())) {
            return parseDocument(document, start);
        } catch (IOException e) {
            throw new DocumentParseException("加载 PDF 失败", e);
        }
    }

    /**
     * 解析图片型 DO（扫描件 JPG/PNG）。
     */
    public ParseResult parse(BufferedImage image, int assumedDpi) {
        long start = System.currentTimeMillis();
        PageLayout layout = OcrLayoutExtractor.extract(image, config, assumedDpi);
        return assemble(Collections.singletonList(layout), ParseSource.OCR, start);
    }

    /**
     * 直接得到目标 JSON 字符串。
     */
    public String parseToJson(Path pdfPath) {
        return toJson(parse(pdfPath));
    }

    public String toJson(ParseResult result) {
        try {
            return objectMapper.writeValueAsString(result.getDocument());
        } catch (JsonProcessingException e) {
            throw new DocumentParseException("序列化 JSON 失败", e);
        }
    }

    // ==================== 内部流程 ====================

    private ParseResult parseDocument(PDDocument document, long start) {
        List<PageLayout> layouts = PdfTextLayoutExtractor.extract(document, config);
        ParseSource source = ParseSource.PDF_TEXT_LAYER;

        if (needOcrFallback(layouts)) {
            if (!config.isOcrFallbackEnabled()) {
                throw new DocumentParseException("PDF 无有效文本层且未开启 OCR 回退，无法解析");
            }
            source = ParseSource.OCR_FALLBACK;
            layouts = OcrLayoutExtractor.extract(document, config, source);
        }
        return assemble(layouts, source, start);
    }

    /** 文本层是否稀疏到需要走 OCR */
    private boolean needOcrFallback(List<PageLayout> layouts) {
        int words = 0;
        for (PageLayout layout : layouts) {
            words += layout.getWords().size();
        }
        return words < config.getTextLayerMinWords();
    }

    private ParseResult assemble(List<PageLayout> layouts, ParseSource source, long start) {
        ParseResult result = new ParseResult();
        result.setSource(source);
        result.setPageCount(layouts.size());

        DeliveryOrderMapper mapper = new DeliveryOrderMapper(config);
        List<Float> confidences = new ArrayList<>();
        for (PageLayout layout : layouts) {
            mapper.mapPage(layout, result);
            confidences.add(layout.getAvgConfidence());
        }
        float avg = 0f;
        for (Float c : confidences) {
            avg += c;
        }
        result.setAvgConfidence(confidences.isEmpty() ? 0f : avg / confidences.size());
        result.setCostMillis(System.currentTimeMillis() - start);
        return result;
    }

    public OcrConfig getConfig() {
        return config;
    }
}
