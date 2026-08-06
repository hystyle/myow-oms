package com.myow.common.ocr.extractor;

import com.myow.common.ocr.config.OcrConfig;
import com.myow.common.ocr.layout.RuleLine;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: PDF 矢量表格线抽取器。
 * <p>
 * 通过重放页面内容流的图形算子，捕获所有描边/填充路径，从中筛出<b>近水平线</b>与<b>近垂直线</b>。
 * 这些线是 DO 表格的真实单元格边界，可让列切分从"几何推断"升级为"精确切分"。
 * <p>
 * 注意：这是<b>增强能力</b>。扫描件走 OCR 通道时本抽取器返回空列表，
 * 解析引擎会自动退化为标签中点推断，功能不受影响。
 */
public final class PdfRuleLineExtractor {

    private static final float MIN_LINE_LENGTH = 3.0f;

    private PdfRuleLineExtractor() {
    }

    public static List<RuleLine> extract(PDPage page, OcrConfig config) {
        try {
            Engine engine = new Engine(page, config);
            engine.processPage(page);
            return engine.rules;
        } catch (Exception e) {
            // 图形流异常不应阻断整体解析，降级为无表格线
            return new ArrayList<>();
        }
    }

    /**
     * 内部图形流引擎：把路径算子还原成线段。
     * <p>
     * PDFBox 传入的坐标已应用 CTM，位于 PDF 用户空间（左下原点），此处统一翻转为左上原点。
     */
    private static final class Engine extends PDFGraphicsStreamEngine {

        private final List<RuleLine> rules = new ArrayList<>();
        private final List<float[]> segments = new ArrayList<>();
        private final float offsetX;
        private final float topY;
        private final float tolerance;

        private Point2D currentPoint;
        private Point2D startPoint;

        private Engine(PDPage page, OcrConfig config) {
            super(page);
            PDRectangle box = page.getCropBox() != null ? page.getCropBox() : page.getMediaBox();
            this.offsetX = box.getLowerLeftX();
            this.topY = box.getUpperRightY();
            this.tolerance = 0.8f;
        }

        private float toTop(float y) {
            return topY - y;
        }

        private float toX(float x) {
            return x - offsetX;
        }

        private void addSegment(Point2D from, Point2D to) {
            if (from == null || to == null) {
                return;
            }
            segments.add(new float[]{
                    toX((float) from.getX()), toTop((float) from.getY()),
                    toX((float) to.getX()), toTop((float) to.getY())
            });
        }

        /** 把累计线段转成 RuleLine（仅保留近水平/近垂直且有足够长度的） */
        private void flushPath() {
            for (float[] s : segments) {
                float dx = Math.abs(s[2] - s[0]);
                float dy = Math.abs(s[3] - s[1]);
                if (dy <= tolerance && dx >= MIN_LINE_LENGTH) {
                    rules.add(new RuleLine(RuleLine.Orientation.HORIZONTAL, s[0], s[1], s[2], s[3]));
                } else if (dx <= tolerance && dy >= MIN_LINE_LENGTH) {
                    rules.add(new RuleLine(RuleLine.Orientation.VERTICAL, s[0], s[1], s[2], s[3]));
                }
            }
            segments.clear();
        }

        @Override
        public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
            addSegment(p0, p1);
            addSegment(p1, p2);
            addSegment(p2, p3);
            addSegment(p3, p0);
            currentPoint = p0;
            startPoint = p0;
        }

        @Override
        public void drawImage(PDImage pdImage) {
            // 表格线抽取不关心图像
        }

        @Override
        public void clip(int windingRule) {
            // no-op
        }

        @Override
        public void moveTo(float x, float y) {
            currentPoint = new Point2D.Float(x, y);
            startPoint = currentPoint;
        }

        @Override
        public void lineTo(float x, float y) {
            Point2D next = new Point2D.Float(x, y);
            addSegment(currentPoint, next);
            currentPoint = next;
        }

        @Override
        public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
            // 曲线不可能是表格线，仅更新当前点
            currentPoint = new Point2D.Float(x3, y3);
        }

        @Override
        public Point2D getCurrentPoint() {
            return currentPoint;
        }

        @Override
        public void closePath() {
            addSegment(currentPoint, startPoint);
            currentPoint = startPoint;
        }

        @Override
        public void endPath() {
            segments.clear();
        }

        @Override
        public void strokePath() {
            flushPath();
        }

        @Override
        public void fillPath(int windingRule) {
            // 细长填充矩形在很多 PDF 生成器中被用作表格线
            flushPath();
        }

        @Override
        public void fillAndStrokePath(int windingRule) {
            flushPath();
        }

        @Override
        public void shadingFill(COSName shadingName) throws IOException {
            // no-op
        }
    }
}
