package com.myow.common.ocr.extractor;

import com.myow.common.ocr.DocumentParseException;
import com.myow.common.ocr.config.OcrConfig;
import com.myow.common.ocr.layout.LineAssembler;
import com.myow.common.ocr.layout.PageLayout;
import com.myow.common.ocr.layout.RuleLine;
import com.myow.common.ocr.layout.TextLine;
import com.myow.common.ocr.layout.Word;
import com.myow.common.ocr.model.ParseSource;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: OCR 版面抽取器（Tess4J / Tesseract）。
 * <p>
 * 关键点在于<b>不使用整页纯文本输出</b>，而是取 RIL_WORD 级别的识别框，
 * 再把图像像素坐标换算为 PDF 点（pt = px × 72 / dpi）。
 * 这样 OCR 结果与 PDF 文本层落在<b>同一坐标空间</b>，
 * 上层的网格解析引擎完全无需区分数据来源。
 * <p>
 * 运行前置条件：本机安装 Tesseract native 库，并提供 tessdata 语言包目录。
 */
public final class OcrLayoutExtractor {

    private OcrLayoutExtractor() {
    }

    /**
     * 渲染 PDF 每一页并做 OCR。
     */
    public static List<PageLayout> extract(PDDocument document, OcrConfig config, ParseSource source) {
        PDFRenderer renderer = new PDFRenderer(document);
        List<PageLayout> layouts = new ArrayList<>();
        ITesseract tesseract = createTesseract(config);
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image;
            try {
                image = renderer.renderImageWithDPI(i, config.getOcrDpi(), ImageType.GRAY);
            } catch (IOException e) {
                throw new DocumentParseException("PDF 渲染为图片失败, page=" + i, e);
            }
            layouts.add(recognize(tesseract, image, config, i, source));
        }
        return layouts;
    }

    /**
     * 直接对图片做 OCR（图片型 DO）。
     *
     * @param assumedDpi 图片的假定 DPI，用于像素 → pt 换算，未知时传 config.getOcrDpi()
     */
    public static PageLayout extract(BufferedImage image, OcrConfig config, int assumedDpi) {
        return recognize(createTesseract(config), image, config.setOcrDpi(assumedDpi), 0, ParseSource.OCR);
    }

    private static ITesseract createTesseract(OcrConfig config) {
        Tesseract tesseract = new Tesseract();
        if (config.getTessDataPath() != null && !config.getTessDataPath().isEmpty()) {
            tesseract.setDatapath(config.getTessDataPath());
        }
        tesseract.setLanguage(config.getLanguage());
        tesseract.setPageSegMode(config.getPageSegMode());
        tesseract.setOcrEngineMode(config.getEngineMode());
        // 保留标点与斜杠：DO 中大量存在 B/L、7/31/26 这类内容
        tesseract.setVariable("preserve_interword_spaces", "1");
        return tesseract;
    }

    private static PageLayout recognize(ITesseract tesseract, BufferedImage rawImage, OcrConfig config,
                                        int pageIndex, ParseSource source) {
        BufferedImage image = config.isBinarize() ? binarize(rawImage) : rawImage;
        List<net.sourceforge.tess4j.Word> ocrWords;
        try {
            ocrWords = tesseract.getWords(image, ITessAPI.TessPageIteratorLevel.RIL_WORD);
        } catch (Throwable t) {
            throw new DocumentParseException(
                    "Tesseract OCR 执行失败（请确认已安装 native 库并正确设置 tessDataPath）, page=" + pageIndex, t);
        }

        float scale = 72f / config.getOcrDpi();
        List<Word> words = new ArrayList<>();
        for (net.sourceforge.tess4j.Word w : ocrWords) {
            String text = w.getText() == null ? "" : w.getText().trim();
            if (text.isEmpty() || w.getConfidence() < config.getMinWordConfidence()) {
                continue;
            }
            Rectangle box = w.getBoundingBox();
            float x0 = box.x * scale;
            float top = box.y * scale;
            float x1 = (box.x + box.width) * scale;
            float bottom = (box.y + box.height) * scale;
            // OCR 无字号信息，用包围盒高度近似（大写字母高度 ≈ 0.72 × 字号）
            float fontSize = (bottom - top) / 0.72f;
            words.add(new Word(text, x0, top, x1, bottom, fontSize, w.getConfidence()));
        }

        List<TextLine> lines = LineAssembler.assemble(words, config.getLineOverlapRatio());
        List<RuleLine> rules = Collections.emptyList();
        return new PageLayout(pageIndex, image.getWidth() * scale, image.getHeight() * scale,
                words, lines, rules, source);
    }

    /**
     * Otsu 全局阈值二值化，显著提升低质量扫描件的识别率。
     */
    static BufferedImage binarize(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        int[] gray = new int[w * h];
        int[] histogram = new int[256];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = src.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int v = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                gray[y * w + x] = v;
                histogram[v]++;
            }
        }

        int total = w * h;
        float sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += (float) i * histogram[i];
        }
        float sumB = 0;
        int wB = 0;
        float maxVariance = -1f;
        int threshold = 128;
        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) {
                continue;
            }
            int wF = total - wB;
            if (wF == 0) {
                break;
            }
            sumB += (float) i * histogram[i];
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;
            float variance = (float) wB * wF * (mB - mF) * (mB - mF);
            if (variance > maxVariance) {
                maxVariance = variance;
                threshold = i;
            }
        }

        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int v = gray[y * w + x] > threshold ? 0xFFFFFF : 0x000000;
                out.setRGB(x, y, v);
            }
        }
        return out;
    }
}
