package com.myow.common.ocr.layout;

import com.myow.common.ocr.model.ParseSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 一页的完整版面：词、行、矢量表格线、页面尺寸与来源通道。
 * <p>
 * 无论是 PDFBox 文本层还是 Tess4J OCR，最终都产出该对象，供上层解析引擎消费——
 * 这是"扫描件与原生 PDF 共用一套解析逻辑"的核心设计。
 */
public final class PageLayout {

    private final int pageIndex;
    private final float width;
    private final float height;
    private final List<Word> words;
    private final List<TextLine> lines;
    private final List<RuleLine> ruleLines;
    private final ParseSource source;
    /** 平均识别置信度（文本层为 100） */
    private final float avgConfidence;

    public PageLayout(int pageIndex, float width, float height,
                      List<Word> words, List<TextLine> lines,
                      List<RuleLine> ruleLines, ParseSource source) {
        this.pageIndex = pageIndex;
        this.width = width;
        this.height = height;
        this.words = Collections.unmodifiableList(new ArrayList<>(words));
        this.lines = Collections.unmodifiableList(new ArrayList<>(lines));
        this.ruleLines = Collections.unmodifiableList(new ArrayList<>(ruleLines == null ? Collections.emptyList() : ruleLines));
        this.source = source;
        float sum = 0f;
        for (Word w : this.words) {
            sum += w.getConfidence();
        }
        this.avgConfidence = this.words.isEmpty() ? 0f : sum / this.words.size();
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public List<Word> getWords() {
        return words;
    }

    public List<TextLine> getLines() {
        return lines;
    }

    public List<RuleLine> getRuleLines() {
        return ruleLines;
    }

    public ParseSource getSource() {
        return source;
    }

    public float getAvgConfidence() {
        return avgConfidence;
    }

    /** 所有竖线（按 X 升序） */
    public List<RuleLine> verticalRules() {
        List<RuleLine> list = new ArrayList<>();
        for (RuleLine r : ruleLines) {
            if (r.getOrientation() == RuleLine.Orientation.VERTICAL) {
                list.add(r);
            }
        }
        list.sort((a, b) -> Float.compare(a.verticalX(), b.verticalX()));
        return list;
    }

    /** 全页纯文本（调试与关键字兜底用） */
    public String plainText() {
        StringBuilder sb = new StringBuilder();
        for (TextLine line : lines) {
            sb.append(line.getText()).append('\n');
        }
        return sb.toString();
    }
}
