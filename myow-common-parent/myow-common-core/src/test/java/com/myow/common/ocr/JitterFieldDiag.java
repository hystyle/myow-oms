package com.myow.common.ocr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 字段级鲁棒性诊断：在拟真 OCR 噪声下逐字段统计失配次数，定位解析引擎的短板字段。
 *
 * <p>与 {@link DeliveryOrderRobustnessCheck}（整单通过率）互补——当某张新样本 PDF 的
 * 抗抖动表现下滑时，本工具能直接指出是<b>哪个字段</b>在失败、以及它实际被误抽成了什么值，
 * 比只看"整单通过率跌了几个百分点"更利于定位根因。</p>
 *
 * <p>用法：<code>java JitterFieldDiag [pdf] [skew°] [lineOffsetPt] [residualPt] [rounds]</code></p>
 */
public class JitterFieldDiag {

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "/Volumes/yss/agentic/file/EZI-0022777-9-DO-1.pdf";
        double skew = args.length > 1 ? Double.parseDouble(args[1]) : 0.1;
        float lineOffset = args.length > 2 ? Float.parseFloat(args[2]) : 0.3f;
        float residual = args.length > 3 ? Float.parseFloat(args[3]) : 0.3f;
        int rounds = args.length > 4 ? Integer.parseInt(args[4]) : 60;

        DeliveryOrderParser parser = new DeliveryOrderParser();
        String baseline = parser.toJson(parser.parse(new File(path)));
        Map<String, String> base = flatten(baseline);

        Map<String, Integer> fail = new TreeMap<>();
        Map<String, Map<String, Integer>> wrongValues = new LinkedHashMap<>();
        int allPass = 0;
        for (int seed = 0; seed < rounds; seed++) {
            Map<String, String> cur = flatten(
                    DeliveryOrderRobustnessCheck.parseRealisticNoise(path, skew, lineOffset, residual, seed));
            boolean ok = true;
            for (Map.Entry<String, String> e : base.entrySet()) {
                String got = cur.get(e.getKey());
                if (!eq(e.getValue(), got)) {
                    ok = false;
                    fail.merge(e.getKey(), 1, Integer::sum);
                    wrongValues.computeIfAbsent(e.getKey(), k -> new TreeMap<>())
                            .merge(String.valueOf(got), 1, Integer::sum);
                }
            }
            if (ok) {
                allPass++;
            }
        }
        System.out.printf("倾斜±%.2f° 行偏移±%.1fpt 词残差±%.1fpt, %d 轮：整单全对 %d 轮 (%.0f%%)%n",
                skew, lineOffset, residual, rounds, allPass, 100.0 * allPass / rounds);
        System.out.println("--- 字段级失配次数（按失败频次降序） ---");
        if (fail.isEmpty()) {
            System.out.println("(无)");
        }
        fail.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .forEach(e -> {
                    System.out.printf("%-38s %3d 次  期望=%s%n",
                            e.getKey(), e.getValue(), base.get(e.getKey()));
                    wrongValues.get(e.getKey()).entrySet().stream()
                            .sorted((x, y) -> y.getValue() - x.getValue())
                            .limit(4)
                            .forEach(w -> System.out.printf("        实际=%-40s x%d%n", w.getKey(), w.getValue()));
                });
    }

    private static boolean eq(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }

    private static Map<String, String> flatten(String json) throws Exception {
        Map<String, String> flat = new LinkedHashMap<>();
        JsonNode root = new ObjectMapper().readTree(json);
        walk("", root, flat);
        return flat;
    }

    private static void walk(String prefix, JsonNode node, Map<String, String> out) {
        if (node.isObject()) {
            Iterator<String> it = node.fieldNames();
            while (it.hasNext()) {
                String name = it.next();
                walk(prefix.isEmpty() ? name : prefix + "." + name, node.get(name), out);
            }
        } else {
            out.put(prefix, node.isNull() ? null : node.asText());
        }
    }
}
