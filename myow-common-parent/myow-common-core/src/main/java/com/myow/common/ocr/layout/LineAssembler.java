package com.myow.common.ocr.layout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 行聚类器：把散乱的 Word 按垂直重叠关系聚合成 TextLine。
 * <p>
 * 采用"垂直重叠比例"而非"Y 坐标相等"，原因是同一视觉行内不同单元格的基线常有 1~2pt 偏差
 * （例如 DO 单中 <i>APM TERMINALS (E425)</i> 与 <i>MAEU MAERSK LUZ 625E</i> 相差 1pt），
 * OCR 通道的抖动更大，必须用容差聚类。
 */
public final class LineAssembler {

    private LineAssembler() {
    }

    /**
     * @param words             词列表
     * @param minOverlapRatio   判定同行的最小垂直重叠比例（建议 0.4）
     */
    public static List<TextLine> assemble(List<Word> words, float minOverlapRatio) {
        List<TextLine> result = new ArrayList<>();
        if (words == null || words.isEmpty()) {
            return result;
        }

        List<Word> sorted = new ArrayList<>(words);
        sorted.sort(Comparator.comparingDouble(Word::getTop).thenComparingDouble(Word::getX0));

        List<Word> current = new ArrayList<>();
        float bandTop = 0f;
        float bandBottom = 0f;

        for (Word w : sorted) {
            if (current.isEmpty()) {
                current.add(w);
                bandTop = w.getTop();
                bandBottom = w.getBottom();
                continue;
            }
            float overlap = Math.min(bandBottom, w.getBottom()) - Math.max(bandTop, w.getTop());
            float minHeight = Math.min(bandBottom - bandTop, w.height());
            float ratio = minHeight <= 0 ? 0f : overlap / minHeight;

            if (ratio >= minOverlapRatio) {
                current.add(w);
                bandTop = Math.min(bandTop, w.getTop());
                bandBottom = Math.max(bandBottom, w.getBottom());
            } else {
                result.add(new TextLine(current));
                current = new ArrayList<>();
                current.add(w);
                bandTop = w.getTop();
                bandBottom = w.getBottom();
            }
        }
        if (!current.isEmpty()) {
            result.add(new TextLine(current));
        }
        result.sort(Comparator.comparingDouble(TextLine::getTop));
        return mergeSideBySide(result);
    }

    /** 同行并排单元格的合并容差（相对较矮行高的比例） */
    private static final float SIDE_BY_SIDE_TOLERANCE = 1.0f;

    /**
     * 并排单元格补救合并。
     *
     * <p>基线聚类是顺序增长的，当同一视觉行的两个单元格基线偏差略大时（扫描件尤其常见），
     * 会被错误拆成两行，导致"同行锚点"失效。
     *
     * <p>判据基于一条稳定的版面事实：
     * <b>同一行内的内容必然左右并排（横向不重叠），而上下堆叠的内容必然横向重叠。</b>
     * 因此仅当两行纵向中心接近<b>且横向完全不重叠</b>时才合并——
     * 这样既能救回被拆散的并排单元格，又绝不会把"标签"和它下方的"取值"错误粘连。
     */
    private static List<TextLine> mergeSideBySide(List<TextLine> lines) {
        List<TextLine> work = new ArrayList<>(lines);
        boolean merged = true;
        while (merged) {
            merged = false;
            for (int i = 0; i + 1 < work.size(); i++) {
                TextLine a = work.get(i);
                TextLine b = work.get(i + 1);
                float tolerance = SIDE_BY_SIDE_TOLERANCE * Math.min(a.height(), b.height());
                if (Math.abs(a.centerY() - b.centerY()) > tolerance) {
                    continue;
                }
                if (horizontallyOverlaps(a, b)) {
                    continue;
                }
                List<Word> combined = new ArrayList<>(a.getWords());
                combined.addAll(b.getWords());
                work.set(i, new TextLine(combined));
                work.remove(i + 1);
                merged = true;
                break;
            }
        }
        work.sort(Comparator.comparingDouble(TextLine::getTop));
        return work;
    }

    private static boolean horizontallyOverlaps(TextLine a, TextLine b) {
        for (Word wa : a.getWords()) {
            for (Word wb : b.getWords()) {
                if (wa.getX0() < wb.getX1() && wb.getX0() < wa.getX1()) {
                    return true;
                }
            }
        }
        return false;
    }
}
