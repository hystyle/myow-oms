package com.myow.common.ocr.layout;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 版面中的一个"词"（带包围盒）。
 * <p>
 * 这是整个解析框架的最小原子单位，也是 <b>原生 PDF 文本层</b> 与 <b>OCR 识别结果</b> 的统一交汇点：
 * <ul>
 *     <li>PDFBox 文本层：由 TextPosition 字形聚合而成</li>
 *     <li>Tess4J OCR：由 RIL_WORD 级别的识别框转换而成（像素 → PDF 点）</li>
 * </ul>
 * 坐标系统一为：<b>原点左上角、X 向右、Y 向下、单位 PDF 点（1/72 英寸）</b>。
 */
public final class Word {

    /** 词文本（已去除首尾空白） */
    private final String text;
    /** 左边界 */
    private final float x0;
    /** 上边界 */
    private final float top;
    /** 右边界 */
    private final float x1;
    /** 下边界 */
    private final float bottom;
    /** 字号（OCR 通道用包围盒高度估算） */
    private final float fontSize;
    /** 置信度 0~100，文本层固定 100 */
    private final float confidence;

    public Word(String text, float x0, float top, float x1, float bottom, float fontSize, float confidence) {
        this.text = text;
        this.x0 = x0;
        this.top = top;
        this.x1 = x1;
        this.bottom = bottom;
        this.fontSize = fontSize;
        this.confidence = confidence;
    }

    public static Word of(String text, float x0, float top, float x1, float bottom, float fontSize) {
        return new Word(text, x0, top, x1, bottom, fontSize, 100f);
    }

    public String getText() {
        return text;
    }

    public float getX0() {
        return x0;
    }

    public float getTop() {
        return top;
    }

    public float getX1() {
        return x1;
    }

    public float getBottom() {
        return bottom;
    }

    public float getFontSize() {
        return fontSize;
    }

    public float getConfidence() {
        return confidence;
    }

    public float centerX() {
        return (x0 + x1) / 2f;
    }

    public float centerY() {
        return (top + bottom) / 2f;
    }

    public float width() {
        return x1 - x0;
    }

    public float height() {
        return bottom - top;
    }

    /**
     * 与另一个词在垂直方向的重叠比例（相对较矮者），用于行聚类。
     */
    public float verticalOverlapRatio(Word other) {
        float overlap = Math.min(this.bottom, other.bottom) - Math.max(this.top, other.top);
        if (overlap <= 0) {
            return 0f;
        }
        float minHeight = Math.min(this.height(), other.height());
        return minHeight <= 0 ? 0f : overlap / minHeight;
    }

    @Override
    public String toString() {
        return text + "[" + Math.round(x0) + "," + Math.round(top) + "-" + Math.round(x1) + "," + Math.round(bottom) + "]";
    }
}
