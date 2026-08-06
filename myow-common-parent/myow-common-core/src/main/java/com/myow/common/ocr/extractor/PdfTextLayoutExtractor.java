package com.myow.common.ocr.extractor;

import com.myow.common.ocr.DocumentParseException;
import com.myow.common.ocr.config.OcrConfig;
import com.myow.common.ocr.layout.LineAssembler;
import com.myow.common.ocr.layout.PageLayout;
import com.myow.common.ocr.layout.RuleLine;
import com.myow.common.ocr.layout.TextLine;
import com.myow.common.ocr.layout.Word;
import com.myow.common.ocr.model.ParseSource;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 原生 PDF 文本层版面抽取器（PDFBox）。
 * <p>
 * 不使用 PDFTextStripper 默认的分词/排版输出，而是<b>下沉到字形（TextPosition）级别</b>自行聚合：
 * <ol>
 *     <li>逐字形采集包围盒，转换为统一的左上原点坐标</li>
 *     <li>按垂直重叠聚类成行</li>
 *     <li>行内按"间距 &gt; max(绝对阈值, 系数×字号)"切分成词</li>
 * </ol>
 * 好处是分词阈值可控、与 OCR 通道行为一致，避免了 PDFTextStripper 在表单类文档上
 * 把跨单元格的文字串成一行导致的字段错位。
 */
public final class PdfTextLayoutExtractor {

    private PdfTextLayoutExtractor() {
    }

    /**
     * 抽取全文档所有页的版面。
     */
    public static List<PageLayout> extract(PDDocument document, OcrConfig config) {
        List<PageLayout> layouts = new ArrayList<>();
        int pageCount = document.getNumberOfPages();
        for (int i = 0; i < pageCount; i++) {
            layouts.add(extractPage(document, i, config));
        }
        return layouts;
    }

    /**
     * 抽取指定页的版面（含矢量表格线）。
     */
    public static PageLayout extractPage(PDDocument document, int pageIndex, OcrConfig config) {
        PDPage page = document.getPage(pageIndex);
        PDRectangle box = page.getCropBox() != null ? page.getCropBox() : page.getMediaBox();

        List<Word> glyphs = collectGlyphs(document, pageIndex);
        List<TextLine> glyphLines = LineAssembler.assemble(glyphs, config.getLineOverlapRatio());

        List<Word> words = new ArrayList<>();
        for (TextLine glyphLine : glyphLines) {
            words.addAll(mergeGlyphsToWords(glyphLine.getWords(), config));
        }

        List<TextLine> lines = LineAssembler.assemble(words, config.getLineOverlapRatio());
        List<RuleLine> rules = config.isUseVectorRules()
                ? PdfRuleLineExtractor.extract(page, config)
                : new ArrayList<>();

        return new PageLayout(pageIndex, box.getWidth(), box.getHeight(), words, lines, rules,
                ParseSource.PDF_TEXT_LAYER);
    }

    /**
     * 采集单页所有字形并转换为统一坐标（左上原点、Y 向下、单位 pt）。
     */
    private static List<Word> collectGlyphs(PDDocument document, int pageIndex) {
        final List<Word> glyphs = new ArrayList<>();
        try {
            PDFTextStripper stripper = new PDFTextStripper() {
                @Override
                protected void processTextPosition(TextPosition text) {
                    String unicode = text.getUnicode();
                    if (unicode == null || unicode.isEmpty()) {
                        return;
                    }
                    float x0 = text.getXDirAdj();
                    float width = text.getWidthDirAdj();
                    float height = text.getHeightDir();
                    // getYDirAdj() 为字形下沿（已换算为左上原点坐标系）
                    float bottom = text.getYDirAdj();
                    float top = bottom - height;
                    float fontSize = text.getFontSizeInPt();
                    if (fontSize <= 0) {
                        fontSize = height;
                    }
                    glyphs.add(new Word(unicode, x0, top, x0 + width, bottom, fontSize, 100f));
                }
            };
            stripper.setSortByPosition(true);
            stripper.setStartPage(pageIndex + 1);
            stripper.setEndPage(pageIndex + 1);
            stripper.writeText(document, new StringWriter());
        } catch (IOException e) {
            throw new DocumentParseException("读取 PDF 文本层失败, page=" + pageIndex, e);
        }
        return glyphs;
    }

    /**
     * 行内字形 → 词。遇到显式空白或超过阈值的间距即断词。
     */
    private static List<Word> mergeGlyphsToWords(List<Word> glyphsInLine, OcrConfig config) {
        List<Word> result = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        float x0 = 0f;
        float x1 = 0f;
        float top = 0f;
        float bottom = 0f;
        float fontSizeSum = 0f;
        int glyphCount = 0;
        float prevX1 = Float.NaN;

        for (Word g : glyphsInLine) {
            boolean blank = g.getText().trim().isEmpty();
            float gap = Float.isNaN(prevX1) ? 0f : g.getX0() - prevX1;
            boolean breakWord = blank
                    || (glyphCount > 0 && gap > config.wordGapThreshold(g.getFontSize()));

            if (breakWord && glyphCount > 0) {
                result.add(flush(buf, x0, top, x1, bottom, fontSizeSum, glyphCount));
                buf.setLength(0);
                glyphCount = 0;
                fontSizeSum = 0f;
            }
            if (blank) {
                prevX1 = g.getX1();
                continue;
            }
            if (glyphCount == 0) {
                x0 = g.getX0();
                top = g.getTop();
                bottom = g.getBottom();
            } else {
                top = Math.min(top, g.getTop());
                bottom = Math.max(bottom, g.getBottom());
            }
            buf.append(g.getText());
            x1 = Math.max(g.getX1(), glyphCount == 0 ? g.getX1() : x1);
            fontSizeSum += g.getFontSize();
            glyphCount++;
            prevX1 = g.getX1();
        }
        if (glyphCount > 0) {
            result.add(flush(buf, x0, top, x1, bottom, fontSizeSum, glyphCount));
        }
        return result;
    }

    private static Word flush(StringBuilder buf, float x0, float top, float x1, float bottom,
                              float fontSizeSum, int glyphCount) {
        return new Word(buf.toString(), x0, top, x1, bottom, fontSizeSum / glyphCount, 100f);
    }
}
