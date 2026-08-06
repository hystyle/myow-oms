package com.myow.common.ocr;

import com.myow.common.ocr.config.OcrConfig;
import com.myow.common.ocr.engine.DeliveryOrderMapper;
import com.myow.common.ocr.extractor.PdfTextLayoutExtractor;
import com.myow.common.ocr.layout.LineAssembler;
import com.myow.common.ocr.layout.PageLayout;
import com.myow.common.ocr.layout.TextLine;
import com.myow.common.ocr.layout.Word;
import com.myow.common.ocr.model.ParseResult;
import com.myow.common.ocr.model.ParseSource;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 鲁棒性回归：验证解析引擎在<b>逐步剥夺辅助信息</b>的条件下是否仍然正确。
 *
 * <ol>
 *   <li>基线：文本层 + 矢量表格线</li>
 *   <li>去掉矢量表格线（等价于 OCR 通道的几何条件）</li>
 *   <li>叠加<b>拟真 OCR 噪声</b>：页面倾斜 + 逐行基线偏移 + 逐词残差</li>
 *   <li>叠加<b>逐词独立抖动</b>：刻意夸张的对抗性上界，用来找引擎的断裂点</li>
 * </ol>
 *
 * <p>场景 3 与场景 4 的区别很重要：真实 OCR 的纵向误差<b>在同一行内高度相关</b>
 * （同一条文本行共用一次基线检测），主要表现为整页倾斜和逐行偏移；
 * 而场景 4 让每个词各自乱跳，破坏的正是"同一行的词共处一个 Y 带"这一版面前提，
 * 因此它衡量的是引擎的极限，而非扫描件的实际表现。
 */
public class DeliveryOrderRobustnessCheck {

    private static final String DEFAULT_PDF = "/Volumes/yss/agentic/file/EZI-0022777-9-DO-1.pdf";
    private static final int ROUNDS = 60;

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : DEFAULT_PDF;
        DeliveryOrderParser parser = new DeliveryOrderParser();

        System.out.println("=== 场景 1：文本层 + 矢量表格线（基线） ===");
        String baseline = parser.toJson(parser.parse(new File(path)));
        System.out.println(baseline);

        System.out.println("=== 场景 2：剥离矢量表格线（模拟 OCR 几何条件） ===");
        DeliveryOrderParser noRules = new DeliveryOrderParser(
                OcrConfig.defaults().setUseVectorRules(false));
        boolean same = baseline.equals(noRules.toJson(noRules.parse(new File(path))));
        System.out.println(">>> 与基线一致: " + same);
        System.out.println();

        System.out.println("=== 场景 3：拟真 OCR 噪声（倾斜 + 逐行偏移 + 逐词残差） ===");
        System.out.println("    倾斜角  行偏移  词残差");
        double[][] realistic = {
                {0.10, 0.3, 0.3},
                {0.25, 0.5, 0.5},
                {0.50, 0.8, 0.8},
                {0.75, 1.0, 1.0},
                {1.00, 1.2, 1.2},
        };
        for (double[] p : realistic) {
            int pass = run(path, baseline, (page, rnd) ->
                    realisticNoise(page, rnd, p[0], (float) p[1], (float) p[2]));
            System.out.printf("    ±%.2f°  ±%.1fpt ±%.1fpt : %2d/%d 与基线完全一致 (%3.0f%%)%n",
                    p[0], p[1], p[2], pass, ROUNDS, 100.0 * pass / ROUNDS);
        }
        System.out.println();

        System.out.println("=== 场景 4：逐词独立抖动（对抗性上界，非真实 OCR 分布） ===");
        for (float amp : new float[]{0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f}) {
            int pass = run(path, baseline, (page, rnd) -> independentJitter(page.getWords(), rnd, amp));
            System.out.printf("    ±%.1fpt : %2d/%d 与基线完全一致 (%3.0f%%)%n",
                    amp, pass, ROUNDS, 100.0 * pass / ROUNDS);
        }
    }

    private static int run(String path, String baseline,
                           BiFunction<PageLayout, Random, List<Word>> noise) throws Exception {
        int pass = 0;
        for (int seed = 0; seed < ROUNDS; seed++) {
            if (baseline.equals(parseWithNoise(path, noise, seed))) {
                pass++;
            }
        }
        return pass;
    }

    // ==================== 噪声模型 ====================

    /**
     * 拟真 OCR 噪声。
     *
     * @param skewDegrees   页面残余倾斜角（度）——扫描件即使经过纠偏也常残留零点几度
     * @param lineOffset    逐行基线偏移上界（pt）——<b>同一行内所有词共享</b>，因为它们共用一次基线检测
     * @param wordResidual  逐词残差上界（pt）——单个词包围盒的独立误差，量级最小
     */
    private static List<Word> realisticNoise(PageLayout page, Random rnd,
                                             double skewDegrees, float lineOffset, float wordResidual) {
        double slope = Math.tan(Math.toRadians((rnd.nextDouble() * 2 - 1) * skewDegrees));
        float pivotX = page.getWidth() / 2f;

        List<Word> out = new ArrayList<>(page.getWords().size());
        // 以干净版面的真实行为单位分配基线偏移，确保"同行相关"这一性质被如实建模
        for (TextLine line : page.getLines()) {
            float shift = (rnd.nextFloat() * 2 - 1) * lineOffset;
            for (Word w : line.getWords()) {
                float dy = (float) ((w.centerX() - pivotX) * slope) + shift
                        + (rnd.nextFloat() * 2 - 1) * wordResidual * 0.5f;
                float dx = (rnd.nextFloat() * 2 - 1) * wordResidual;
                float dw = (rnd.nextFloat() * 2 - 1) * wordResidual * 0.5f;
                out.add(new Word(w.getText(), w.getX0() + dx, w.getTop() + dy,
                        w.getX1() + dx + dw, w.getBottom() + dy, w.getFontSize(), 92f));
            }
        }
        return out;
    }

    /** 逐词独立抖动：破坏"同行共处一个 Y 带"的前提，属于对抗性上界 */
    private static List<Word> independentJitter(List<Word> words, Random rnd, float amplitude) {
        List<Word> out = new ArrayList<>(words.size());
        for (Word w : words) {
            float dx = (rnd.nextFloat() * 2 - 1) * amplitude;
            float dy = (rnd.nextFloat() * 2 - 1) * amplitude;
            float dw = (rnd.nextFloat() * 2 - 1) * amplitude * 0.5f;
            out.add(new Word(w.getText(), w.getX0() + dx, w.getTop() + dy,
                    w.getX1() + dx + dw, w.getBottom() + dy, w.getFontSize(), 92f));
        }
        return out;
    }

    /** 供诊断工具复用的拟真噪声解析入口（字段级失配定位时使用） */
    public static String parseRealisticNoise(String path, double skewDegrees, float lineOffset,
                                             float wordResidual, long seed) throws Exception {
        return parseWithNoise(path,
                (page, rnd) -> realisticNoise(page, rnd, skewDegrees, lineOffset, wordResidual), seed);
    }

    // ==================== 执行 ====================

    /** 取真实文本层坐标后注入噪声，重建版面并解析（不提供矢量表格线，等同 OCR 通道） */
    private static String parseWithNoise(String path,
                                         BiFunction<PageLayout, Random, List<Word>> noise,
                                         long seed) throws Exception {
        OcrConfig config = OcrConfig.defaults().setUseVectorRules(false);
        ParseResult result = new ParseResult();
        result.setSource(ParseSource.OCR);

        try (PDDocument doc = PDDocument.load(new File(path))) {
            DeliveryOrderMapper mapper = new DeliveryOrderMapper(config);
            Random random = new Random(seed);
            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                PageLayout origin = PdfTextLayoutExtractor.extractPage(doc, i, config);
                List<Word> noisy = noise.apply(origin, random);
                List<TextLine> lines = LineAssembler.assemble(noisy, config.getLineOverlapRatio());
                mapper.mapPage(new PageLayout(i, origin.getWidth(), origin.getHeight(),
                        noisy, lines, Collections.emptyList(), ParseSource.OCR), result);
            }
        }
        result.setPageCount(1);
        return new DeliveryOrderParser(config).toJson(result);
    }
}
