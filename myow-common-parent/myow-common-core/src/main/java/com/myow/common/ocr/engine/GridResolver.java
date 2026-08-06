package com.myow.common.ocr.engine;

import com.myow.common.ocr.config.OcrConfig;
import com.myow.common.ocr.layout.PageLayout;
import com.myow.common.ocr.layout.RuleLine;
import com.myow.common.ocr.layout.TextLine;
import com.myow.common.ocr.layout.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 网格解析引擎——本方案的核心。
 *
 * <p><b>为什么不用"正则扫全文"？</b>DO 是二维表单，同一行里横向并列多个字段；
 * 一维文本流会把 <i>MAEU MAERSK LUZ 625E</i> 和 <i>APM TERMINALS (E425)</i> 串在一起，
 * 靠正则切分极其脆弱且换个版式就崩。因此这里做的是<b>几何解析</b>：
 *
 * <ol>
 *   <li><b>定位标签</b>：在行内单元格中匹配标签（支持同义词 / 前缀 / 同行锚点消歧）</li>
 *   <li><b>推断列边界</b>：以相邻标签单元格的中点为默认边界；若存在矢量表格线，
 *       用其<b>收窄</b>该区间（只收不放，避免粗粒度外框吞并子表格列）</li>
 *   <li><b>向下取值</b>：在标签行下方的取值行中，按"词中心落入列区间"归属</li>
 *   <li><b>内聚修复</b>：把紧邻却被边界误切到空列的碎词（如 "14,830 | LB"）并回原列</li>
 * </ol>
 *
 * <p>整个过程只依赖 {@link PageLayout}，因此对<b>原生 PDF 与扫描件 OCR 完全一视同仁</b>。
 */
public class GridResolver {

    private final PageLayout page;
    private final OcrConfig config;
    /** 全部已知标签（含仅用于断行的停用标签），归一化形态 */
    private final Set<String> vocabulary;

    private final List<TextLine> lines;
    private final List<List<TextLine.Cell>> lineCells = new ArrayList<>();
    private final boolean[] isLabelRow;
    private final Map<Integer, RowBlock> rowCache = new HashMap<>();

    public GridResolver(PageLayout page, OcrConfig config, Set<String> vocabulary) {
        this.page = page;
        this.config = config;
        this.vocabulary = vocabulary;
        this.lines = page.getLines();
        this.isLabelRow = new boolean[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            TextLine line = lines.get(i);
            List<TextLine.Cell> cells = line.splitCells(config.cellGapThreshold(line.getAvgFontSize()));
            lineCells.add(cells);
            for (TextLine.Cell c : cells) {
                if (vocabulary.contains(LabelSpec.normalize(c.getText()))) {
                    isLabelRow[i] = true;
                    break;
                }
            }
        }
    }

    // ==================== 对外 API ====================

    /**
     * 解析单值字段。未命中返回 null。
     */
    public String resolve(LabelSpec spec) {
        List<String> values = resolveList(spec);
        if (values.isEmpty()) {
            return null;
        }
        int limit = Math.min(spec.getMaxValueLines(), values.size());
        String joined = String.join(" ", values.subList(0, limit)).trim();
        return joined.isEmpty() ? null : joined;
    }

    /**
     * 解析多行值字段（每个取值行一条），用于多箱等场景。
     */
    public List<String> resolveList(LabelSpec spec) {
        int[] hit = locate(spec);
        if (hit == null) {
            return Collections.emptyList();
        }
        RowBlock block = rowBlock(hit[0]);
        List<String> raw = block.columnValues.get(hit[1]);
        List<String> result = new ArrayList<>();
        for (String s : raw) {
            String t = s == null ? "" : s.trim();
            if (!t.isEmpty()) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * 区域取值：返回落在指定矩形内的所有行文本（按 Y 升序）。
     * <p>用于抬头公司这类"没有标签"的自由文本块。
     */
    public List<String> resolveRegionLines(float top, float bottom, float left, float right) {
        List<String> result = new ArrayList<>();
        for (TextLine line : lines) {
            if (line.centerY() < top || line.centerY() > bottom) {
                continue;
            }
            List<Word> in = line.getWords().stream()
                    .filter(w -> w.centerX() >= left && w.centerX() <= right)
                    .collect(Collectors.toList());
            if (in.isEmpty()) {
                continue;
            }
            String text = in.stream().map(Word::getText).collect(Collectors.joining(" ")).trim();
            if (!text.isEmpty()) {
                result.add(text);
            }
        }
        return result;
    }

    /**
     * 找到包含给定标签的行的纵向位置（返回该行 top），未找到返回 -1。
     */
    public float labelLineTop(String label) {
        String target = LabelSpec.normalize(label);
        for (int i = 0; i < lines.size(); i++) {
            for (TextLine.Cell c : lineCells.get(i)) {
                if (LabelSpec.normalize(c.getText()).equals(target)) {
                    return lines.get(i).getTop();
                }
            }
        }
        return -1f;
    }

    /** 找到包含给定文本片段的行的下沿，未找到返回 -1 */
    public float lineBottomContaining(String fragment) {
        String target = LabelSpec.normalize(fragment);
        for (TextLine line : lines) {
            if (LabelSpec.normalize(line.getText()).contains(target)) {
                return line.getBottom();
            }
        }
        return -1f;
    }

    // ==================== 内部实现 ====================

    /**
     * 定位标签所在的 [行下标, 单元格下标]。
     */
    private int[] locate(LabelSpec spec) {
        List<String> aliases = spec.getAliases().stream()
                .map(LabelSpec::normalize).collect(Collectors.toList());
        String anchor = spec.getAnchorLabel() == null ? null : LabelSpec.normalize(spec.getAnchorLabel());

        // 第一轮：精确策略
        for (int i = 0; i < lines.size(); i++) {
            List<TextLine.Cell> cells = lineCells.get(i);
            if (anchor != null && !containsCell(cells, anchor)) {
                continue;
            }
            for (int j = 0; j < cells.size(); j++) {
                if (matches(LabelSpec.normalize(cells.get(j).getText()), aliases, spec.getMatchMode())) {
                    return new int[]{i, j};
                }
            }
        }
        // 第二轮：模糊匹配（容忍 OCR 字符错误）
        for (int i = 0; i < lines.size(); i++) {
            List<TextLine.Cell> cells = lineCells.get(i);
            if (anchor != null && !containsCellFuzzy(cells, anchor)) {
                continue;
            }
            for (int j = 0; j < cells.size(); j++) {
                String text = LabelSpec.normalize(cells.get(j).getText());
                for (String alias : aliases) {
                    String candidate = text;
                    if (spec.getMatchMode() != LabelSpec.MatchMode.EXACT && text.length() > alias.length()) {
                        candidate = text.substring(0, alias.length());
                    }
                    if (fuzzyEquals(candidate, alias)) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        return null;
    }

    private boolean containsCell(List<TextLine.Cell> cells, String normalized) {
        for (TextLine.Cell c : cells) {
            if (LabelSpec.normalize(c.getText()).equals(normalized)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsCellFuzzy(List<TextLine.Cell> cells, String normalized) {
        for (TextLine.Cell c : cells) {
            if (fuzzyEquals(LabelSpec.normalize(c.getText()), normalized)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matches(String text, List<String> aliases, LabelSpec.MatchMode mode) {
        for (String alias : aliases) {
            switch (mode) {
                case EXACT:
                    if (text.equals(alias)) {
                        return true;
                    }
                    break;
                case PREFIX:
                    if (text.startsWith(alias)) {
                        return true;
                    }
                    break;
                case CONTAINS:
                    if (text.contains(alias)) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    /** 允许约 15% 字符误差的模糊相等，用于 OCR 误识别 */
    static boolean fuzzyEquals(String a, String b) {
        if (a.equals(b)) {
            return true;
        }
        int maxLen = Math.max(a.length(), b.length());
        if (maxLen == 0 || Math.abs(a.length() - b.length()) > 2) {
            return false;
        }
        int allowed = Math.max(1, (int) Math.floor(maxLen * 0.15));
        return levenshtein(a, b) <= allowed;
    }

    private static int levenshtein(String a, String b) {
        int[] prev = new int[b.length() + 1];
        int[] cur = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) {
            prev[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            cur[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                cur[j] = Math.min(Math.min(cur[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev;
            prev = cur;
            cur = tmp;
        }
        return prev[b.length()];
    }

    /**
     * 解析（并缓存）某个标签行对应的整个"行块"：列边界 + 各列取值。
     * <p>整行一起解析，才能做跨列的内聚修复。
     */
    private RowBlock rowBlock(int labelLineIndex) {
        RowBlock cached = rowCache.get(labelLineIndex);
        if (cached != null) {
            return cached;
        }

        TextLine labelLine = lines.get(labelLineIndex);
        List<TextLine.Cell> cells = lineCells.get(labelLineIndex);
        List<TextLine> valueLines = collectValueLines(labelLineIndex);

        RowBlock block = new RowBlock();
        block.bands = computeBands(cells, valueLines, labelLine);
        block.columnValues = new ArrayList<>();
        for (int i = 0; i < cells.size(); i++) {
            block.columnValues.add(new ArrayList<>());
        }

        for (TextLine valueLine : valueLines) {
            List<List<Word>> assigned = assignWords(valueLine, block.bands);
            cohesionRepair(valueLine, assigned, cells);
            for (int i = 0; i < cells.size(); i++) {
                String text = assigned.get(i).stream()
                        .map(Word::getText).collect(Collectors.joining(" ")).trim();
                block.columnValues.get(i).add(text);
            }
        }

        rowCache.put(labelLineIndex, block);
        return block;
    }

    /**
     * 收集标签行下方的取值行：遇到下一个标签行、或纵向间距过大即停止。
     */
    private List<TextLine> collectValueLines(int labelLineIndex) {
        List<TextLine> result = new ArrayList<>();
        TextLine labelLine = lines.get(labelLineIndex);
        TextLine prev = labelLine;
        float fontSize = Math.max(prev.getAvgFontSize(), 5f);
        float maxGap = config.getValueRowMaxGapRatio() * fontSize;

        for (int i = labelLineIndex + 1; i < lines.size(); i++) {
            TextLine line = lines.get(i);
            // 与标签行纵向交叠的不是取值行，而是同一视觉行里被拆出来的残片——
            // 水印、印章、斜排文字最容易造成这种拆分（本样例的 "DO NOT USE" 水印
            // 就会掉出一个孤立的 USE，若当成取值行会直接顶掉 WEIGHT 列的真值）。
            // 阈值刻意远低于行聚类阈值：取值行应当<b>明确位于</b>标签行下方，
            // 只要与标签行有可观交叠，就说明它其实是标签行的残片而非下一行。
            if (verticalOverlapRatio(line, labelLine) >= MAX_LABEL_ROW_OVERLAP) {
                continue;
            }
            if (line.getTop() - prev.getBottom() > maxGap) {
                break;
            }
            if (isLabelRow[i]) {
                break;
            }
            result.add(line);
            prev = line;
            if (result.size() >= 8) {
                break;
            }
        }
        return result;
    }

    /** 取值行允许与标签行发生的最大垂直交叠比例——超过即判定为标签行残片 */
    private static final float MAX_LABEL_ROW_OVERLAP = 0.35f;

    /** 两行的垂直重叠比例（以较矮者为基准） */
    private static float verticalOverlapRatio(TextLine a, TextLine b) {
        float overlap = Math.min(a.getBottom(), b.getBottom()) - Math.max(a.getTop(), b.getTop());
        float minHeight = Math.min(a.height(), b.height());
        return minHeight <= 0 ? 0f : overlap / minHeight;
    }

    /**
     * 计算每个标签单元格的列区间 [left, right]。
     * <p>基准：相邻标签单元格中点；再用取值区间内的矢量竖线<b>收窄</b>。
     */
    private List<float[]> computeBands(List<TextLine.Cell> cells, List<TextLine> valueLines, TextLine labelLine) {
        float pageLeft = 0f;
        float pageRight = page.getWidth();

        // 只以"第一条取值行"作为竖线覆盖判据：列边界必须存在于值所在的高度，
        // 若把整个行块（可能延伸很长）都算进来，会把有效竖线误判为未覆盖。
        float valueTop = valueLines.isEmpty() ? labelLine.getBottom() : valueLines.get(0).getTop();
        float valueBottom = valueLines.isEmpty() ? labelLine.getBottom() : valueLines.get(0).getBottom();

        // 仅保留纵向覆盖"取值区间"的竖线——列边界必须存在于值所在的位置
        List<Float> ruleXs = new ArrayList<>();
        for (RuleLine r : page.verticalRules()) {
            if (r.coversVertically(valueTop, valueBottom, config.getRuleLineTolerance())) {
                ruleXs.add(r.verticalX());
            }
        }
        Collections.sort(ruleXs);

        List<float[]> bands = new ArrayList<>();
        for (int i = 0; i < cells.size(); i++) {
            TextLine.Cell cell = cells.get(i);
            float left = (i == 0) ? pageLeft : (cells.get(i - 1).getX1() + cell.getX0()) / 2f;
            float right = (i == cells.size() - 1) ? pageRight : (cell.getX1() + cells.get(i + 1).getX0()) / 2f;

            float center = cell.centerX();
            for (Float x : ruleXs) {
                if (x <= center && x > left) {
                    left = x;
                }
                if (x >= center && x < right) {
                    right = x;
                }
            }
            bands.add(new float[]{left, right});
        }
        return bands;
    }

    /**
     * 把取值行分配到各列。
     *
     * <p><b>以"单元格"而非"词"为分配单位</b>，这是抗 OCR 抖动的关键。
     * 逐词按中心点归列时，落在列边界附近的词会被误判翻列，产生两个方向的错误：
     * 既可能把 {@code STORAGE BOX} 削成 {@code BOX}（本列的词跑掉），
     * 也可能让邻列的 {@code NEW YORK/NEWARK AREA} 渗进 LOCATION 列（外列的词跑进来）。
     *
     * <p>而单元格是由"词距远小于留白"这一版面事实聚出来的原子——
     * 抖动量级远小于留白，不足以改变这个序关系，因此单元格边界比列边界稳定得多。
     * 以整格为单位取<b>与列区间重叠最大</b>者归属，值就不会再被拆开或串列。
     */
    private List<List<Word>> assignWords(TextLine valueLine, List<float[]> bands) {
        List<List<Word>> assigned = new ArrayList<>();
        for (int i = 0; i < bands.size(); i++) {
            assigned.add(new ArrayList<>());
        }
        float gap = config.cellGapThreshold(Math.max(valueLine.getAvgFontSize(), 5f));
        List<List<Word>> fragments = new ArrayList<>();
        for (TextLine.Cell cell : valueLine.splitCells(gap)) {
            splitAcrossBands(cell.getWords(), bands, fragments, 0);
        }
        for (List<Word> fragment : fragments) {
            int idx = columnOfSpan(spanLeft(fragment), spanRight(fragment), bands);
            if (idx >= 0) {
                assigned.get(idx).addAll(fragment);
            }
        }
        for (List<Word> col : assigned) {
            col.sort((a, b) -> Float.compare(a.getX0(), b.getX0()));
        }
        return assigned;
    }

    /** 网格先验切分的递归深度上限 */
    private static final int MAX_SPLIT_DEPTH = 3;
    /** 认定"主间距"所需的领先倍率 */
    private static final float DOMINANT_GAP_RATIO = 2.2f;

    /**
     * 网格先验修复：若一簇词<b>横跨列边界</b>，尝试在其"主间距"处切开。
     *
     * <p>取值行的单元格切分只看间距分布，看不到列结构；而列边界是由标签行给出的独立信息。
     * 当两者矛盾——一整格压在边界上——说明间距判据在这里失灵了，此时应当采信网格。
     * 典型如 {@code 412 STORAGE BOX}：{@code 412} 与 {@code STORAGE} 之间只有 10pt，
     * 词距 2.8pt，抖动足以抹平这个差；但列边界明确落在两者之间。
     *
     * <p>切分需同时满足两个条件，避免误伤：
     * <ol>
     *   <li><b>存在主间距</b>——最大内部间距显著领先次大间距，说明确有一处结构性断点；</li>
     *   <li><b>切后更贴合网格</b>——两段落入单列的总宽度大于整体，即切分确实降低了跨界量。</li>
     * </ol>
     */
    private void splitAcrossBands(List<Word> cluster, List<float[]> bands,
                                  List<List<Word>> out, int depth) {
        float x0 = spanLeft(cluster);
        float x1 = spanRight(cluster);
        float inBand = inBandMass(x0, x1, bands);
        if (depth >= MAX_SPLIT_DEPTH || cluster.size() < 2 || inBand >= (x1 - x0) - 0.01f) {
            out.add(cluster);
            return;
        }

        int cutAt = -1;
        float largest = -1f;
        float second = -1f;
        for (int i = 1; i < cluster.size(); i++) {
            float g = cluster.get(i).getX0() - cluster.get(i - 1).getX1();
            if (g > largest) {
                second = largest;
                largest = g;
                cutAt = i;
            } else if (g > second) {
                second = g;
            }
        }
        if (cutAt < 0 || largest < DOMINANT_GAP_RATIO * Math.max(second, 0.8f)) {
            out.add(cluster);
            return;
        }

        List<Word> left = new ArrayList<>(cluster.subList(0, cutAt));
        List<Word> right = new ArrayList<>(cluster.subList(cutAt, cluster.size()));
        float after = inBandMass(spanLeft(left), spanRight(left), bands)
                + inBandMass(spanLeft(right), spanRight(right), bands);
        if (after <= inBand + 0.01f) {
            out.add(cluster);
            return;
        }
        splitAcrossBands(left, bands, out, depth + 1);
        splitAcrossBands(right, bands, out, depth + 1);
    }

    /** [x0,x1] 与单一列区间的最大重叠长度——衡量它"落在一列之内"的程度 */
    private float inBandMass(float x0, float x1, List<float[]> bands) {
        float best = 0f;
        for (float[] band : bands) {
            best = Math.max(best, Math.min(x1, band[1]) - Math.max(x0, band[0]));
        }
        return best;
    }

    private static float spanLeft(List<Word> words) {
        float min = Float.MAX_VALUE;
        for (Word w : words) {
            min = Math.min(min, w.getX0());
        }
        return min;
    }

    private static float spanRight(List<Word> words) {
        float max = -Float.MAX_VALUE;
        for (Word w : words) {
            max = Math.max(max, w.getX1());
        }
        return max;
    }

    /** 取与 [x0,x1] 横向重叠最大的列；完全落在所有列之外时退化为"离中心最近的列" */
    private int columnOfSpan(float x0, float x1, List<float[]> bands) {
        int best = -1;
        float bestOverlap = 0f;
        for (int i = 0; i < bands.size(); i++) {
            float overlap = Math.min(x1, bands.get(i)[1]) - Math.max(x0, bands.get(i)[0]);
            if (overlap > bestOverlap) {
                bestOverlap = overlap;
                best = i;
            }
        }
        return best >= 0 ? best : columnOf((x0 + x1) / 2f, bands);
    }

    private int columnOf(float x, List<float[]> bands) {
        for (int i = 0; i < bands.size(); i++) {
            if (x >= bands.get(i)[0] && x <= bands.get(i)[1]) {
                return i;
            }
        }
        // 落在所有列之外时归入最近列
        int best = -1;
        float bestDist = Float.MAX_VALUE;
        for (int i = 0; i < bands.size(); i++) {
            float d = Math.min(Math.abs(x - bands.get(i)[0]), Math.abs(x - bands.get(i)[1]));
            if (d < bestDist) {
                bestDist = d;
                best = i;
            }
        }
        return best;
    }

    /**
     * 内聚修复：相邻两词若几乎贴在一起却被判到不同列，且后一列只有这一个词，
     * 说明它是被列边界误切的碎片（典型如 "14,830" | "LB"），应并回前一列。
     *
     * <p><b>关键约束</b>：只有当该词的中心<b>不在目标列标签本身的横向跨度内</b>时才允许搬移。
     * 否则会发生级联误判——例如把 "STORAGE BOX" 中的 BOX 从 DESCRIPTION 列拽走，
     * 导致 total_packages 变成 "412 STORAGE BOX"。
     * 词若正落在某个标签的正下方，它就属于那一列，任何"贴得近"都不能推翻这一点。
     */
    private void cohesionRepair(TextLine valueLine, List<List<Word>> assigned, List<TextLine.Cell> labelCells) {
        List<Word> words = valueLine.getWords();
        float cohesionGap = 0.6f * config.cellGapThreshold(Math.max(valueLine.getAvgFontSize(), 5f));

        for (int i = 1; i < words.size(); i++) {
            Word prev = words.get(i - 1);
            Word cur = words.get(i);
            if (cur.getX0() - prev.getX1() > cohesionGap) {
                continue;
            }
            int prevCol = indexOfWord(assigned, prev);
            int curCol = indexOfWord(assigned, cur);
            if (prevCol < 0 || curCol < 0 || prevCol == curCol) {
                continue;
            }
            if (assigned.get(curCol).size() != 1) {
                continue;
            }
            // 落在目标列标签正下方 => 它本就属于该列，不搬移
            TextLine.Cell curLabel = labelCells.get(curCol);
            float center = cur.centerX();
            if (center >= curLabel.getX0() && center <= curLabel.getX1()) {
                continue;
            }
            assigned.get(curCol).remove(cur);
            assigned.get(prevCol).add(cur);
            assigned.get(prevCol).sort((a, b) -> Float.compare(a.getX0(), b.getX0()));
        }
    }

    private int indexOfWord(List<List<Word>> assigned, Word w) {
        for (int i = 0; i < assigned.size(); i++) {
            if (assigned.get(i).contains(w)) {
                return i;
            }
        }
        return -1;
    }

    /** 行块：某个标签行 + 其下方取值的完整解析结果 */
    private static final class RowBlock {
        private List<float[]> bands;
        /** 外层=列，内层=取值行 */
        private List<List<String>> columnValues;
    }

    /** 便捷构造：从模板词表建立解析器 */
    public static GridResolver of(PageLayout page, OcrConfig config, Set<String> vocabulary) {
        return new GridResolver(page, config, new HashSet<>(vocabulary));
    }
}
