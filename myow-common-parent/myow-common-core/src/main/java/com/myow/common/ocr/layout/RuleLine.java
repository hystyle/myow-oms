package com.myow.common.ocr.layout;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 页面中的矢量表格线（横线 / 竖线）。
 * <p>
 * 仅原生 PDF 通道可获得；OCR 通道为空列表，此时解析引擎自动退化为
 * "标签中点推断列边界"的几何策略，因此表格线属于 <b>增强项</b> 而非必需项。
 */
public final class RuleLine {

    public enum Orientation {
        /** 横线 */
        HORIZONTAL,
        /** 竖线 */
        VERTICAL
    }

    private final Orientation orientation;
    private final float x0;
    private final float top;
    private final float x1;
    private final float bottom;

    public RuleLine(Orientation orientation, float x0, float top, float x1, float bottom) {
        this.orientation = orientation;
        this.x0 = Math.min(x0, x1);
        this.x1 = Math.max(x0, x1);
        this.top = Math.min(top, bottom);
        this.bottom = Math.max(top, bottom);
    }

    public Orientation getOrientation() {
        return orientation;
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

    /** 竖线的 X 位置 */
    public float verticalX() {
        return (x0 + x1) / 2f;
    }

    /** 横线的 Y 位置 */
    public float horizontalY() {
        return (top + bottom) / 2f;
    }

    /** 竖线是否覆盖给定的纵向区间（允许 tolerance 容差） */
    public boolean coversVertically(float yFrom, float yTo, float tolerance) {
        return this.top - tolerance <= yFrom && this.bottom + tolerance >= yTo;
    }

    /** 横线是否横跨给定的横向区间 */
    public boolean spansHorizontally(float xFrom, float xTo) {
        return this.x0 <= xFrom && this.x1 >= xTo;
    }

    @Override
    public String toString() {
        return orientation + "(" + Math.round(x0) + "," + Math.round(top) + " -> " + Math.round(x1) + "," + Math.round(bottom) + ")";
    }
}
