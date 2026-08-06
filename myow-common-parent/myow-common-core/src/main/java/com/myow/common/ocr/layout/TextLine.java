package com.myow.common.ocr.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 一"行"（由若干 Word 按垂直重叠聚类而成），并可按横向间距进一步切分为若干"单元格"。
 * <p>
 * DO 这类表单的本质是网格：同一行里既可能是若干个 <b>标签</b>（CARRIER / LOCATION / ORIGIN...PORT），
 * 也可能是若干个 <b>取值</b>。因此"行 → 单元格"的二级切分，是后续列边界推断的基础。
 */
public final class TextLine {

    /** 行内的一个视觉单元格（一簇横向连续的词） */
    public static final class Cell {
        private final List<Word> words;
        private final String text;
        private final float x0;
        private final float x1;

        Cell(List<Word> words) {
            this.words = Collections.unmodifiableList(new ArrayList<>(words));
            this.text = words.stream().map(Word::getText).collect(Collectors.joining(" "));
            float min = Float.MAX_VALUE;
            float max = -Float.MAX_VALUE;
            for (Word w : words) {
                min = Math.min(min, w.getX0());
                max = Math.max(max, w.getX1());
            }
            this.x0 = min;
            this.x1 = max;
        }

        public List<Word> getWords() {
            return words;
        }

        public String getText() {
            return text;
        }

        public float getX0() {
            return x0;
        }

        public float getX1() {
            return x1;
        }

        public float centerX() {
            return (x0 + x1) / 2f;
        }

        @Override
        public String toString() {
            return "[" + Math.round(x0) + "-" + Math.round(x1) + "]" + text;
        }
    }

    private final List<Word> words;
    private final float top;
    private final float bottom;
    private final float avgFontSize;
    private List<Cell> cellCache;
    private float cellCacheGap = -1f;

    public TextLine(List<Word> words) {
        List<Word> sorted = new ArrayList<>(words);
        sorted.sort((a, b) -> Float.compare(a.getX0(), b.getX0()));
        this.words = Collections.unmodifiableList(sorted);

        float t = Float.MAX_VALUE;
        float b = -Float.MAX_VALUE;
        float fs = 0f;
        for (Word w : sorted) {
            t = Math.min(t, w.getTop());
            b = Math.max(b, w.getBottom());
            fs += w.getFontSize();
        }
        this.top = t;
        this.bottom = b;
        this.avgFontSize = sorted.isEmpty() ? 0f : fs / sorted.size();
    }

    public List<Word> getWords() {
        return words;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }

    public float centerY() {
        return (top + bottom) / 2f;
    }

    public float height() {
        return bottom - top;
    }

    public float getAvgFontSize() {
        return avgFontSize;
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    /** 整行拼接文本（词间单空格） */
    public String getText() {
        return words.stream().map(Word::getText).collect(Collectors.joining(" "));
    }

    /**
     * 按横向间距把行切分为单元格。
     *
     * @param minCellGap 判定为"跨单元格"的间距<b>下限</b>（调用方通常传 max(绝对下限, 系数 * 字号)）；
     *                   实际判据由 {@link #adaptiveGapThreshold(float)} 依据本行间距分布自适应给出
     */
    public List<Cell> splitCells(float minCellGap) {
        if (cellCache != null && Math.abs(cellCacheGap - minCellGap) < 0.001f) {
            return cellCache;
        }
        float threshold = adaptiveGapThreshold(minCellGap);

        List<Cell> cells = new ArrayList<>();
        List<Word> buffer = new ArrayList<>();
        float prevX1 = Float.NaN;
        for (Word w : words) {
            if (!buffer.isEmpty() && (w.getX0() - prevX1) > threshold) {
                cells.add(new Cell(buffer));
                buffer = new ArrayList<>();
            }
            buffer.add(w);
            // 同一单元格内可能有重叠词（如上下错行的 DO/NOT/USE），取最大右边界
            prevX1 = Float.isNaN(prevX1) || buffer.size() == 1 ? w.getX1() : Math.max(prevX1, w.getX1());
        }
        if (!buffer.isEmpty()) {
            cells.add(new Cell(buffer));
        }
        cellCache = Collections.unmodifiableList(cells);
        cellCacheGap = minCellGap;
        return cellCache;
    }

    /**
     * 自适应单元格间距阈值。
     *
     * <p><b>为什么不能用固定阈值。</b>表单行的词间距天然是<b>双峰分布</b>：
     * 单元格<i>内部</i>是词距（约 1 个空格宽），单元格<i>之间</i>是版面留白（往往几十 pt）。
     * 固定阈值等于在两峰之间随手划一刀——刀口离某一峰太近时，OCR 的包围盒误差就能把点推过界。
     * 实测中 {@code OUR REF. NO.} 正是这样被劈成 {@code OUR REF.} 和 {@code NO.} 两格，
     * 进而让依赖它做同行锚点的 date 字段整个丢失。
     *
     * <p><b>做法。</b>把本行所有间距升序排列，取<b>最低的一道量级跳变</b>，以跳变的中点为阈值。
     * 中点是两簇之间的最大间隔边界（max-margin），离两簇都最远，因此对抖动最不敏感。
     *
     * <p>注意必须取<b>最低</b>而非最大的一道跳变：表单行的间距往往有三层——
     * 词距（约 3pt）、单元格留白（10~50pt）、版面留白（可达 300pt）。
     * 取最大跳变会抓到最外层的版面留白，把 {@code 412 | STORAGE BOX} 也粘成一格。
     * 真正要找的是"词距簇"的上沿：比词距明显大的任何间距，都已经是单元格边界了。
     *
     * <p>若本行不存在这样的跳变（整行只有一簇间距——要么就是一个单元格，
     * 要么每个词都自成单元格），则退回调用方给的下限。
     */
    private float adaptiveGapThreshold(float minCellGap) {
        if (words.size() < 3) {
            return minCellGap;
        }
        List<Float> gaps = new ArrayList<>();
        float prevX1 = words.get(0).getX1();
        for (int i = 1; i < words.size(); i++) {
            Word w = words.get(i);
            gaps.add(Math.max(0f, w.getX0() - prevX1));
            prevX1 = Math.max(prevX1, w.getX1());
        }
        Collections.sort(gaps);

        // 单元格留白的绝对下限：它至少要明显宽于一个空格，但不必达到调用方的保守下限
        float absMin = Math.max(3f, 0.4f * minCellGap);
        for (int i = 0; i + 1 < gaps.size(); i++) {
            float lo = gaps.get(i);
            float hi = gaps.get(i + 1);
            if (hi >= absMin && hi >= GAP_CLUSTER_RATIO * Math.max(lo, 0.8f)) {
                return (lo + hi) / 2f;
            }
        }
        return minCellGap;
    }

    /** 判定"跨簇"所需的间距倍率：上簇间距至少是下簇的这个倍数 */
    private static final float GAP_CLUSTER_RATIO = 2.2f;

    @Override
    public String toString() {
        return "y=" + Math.round(top) + " " + getText();
    }
}
