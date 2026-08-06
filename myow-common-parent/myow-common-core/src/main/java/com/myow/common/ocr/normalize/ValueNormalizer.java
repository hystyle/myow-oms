package com.myow.common.ocr.normalize;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 取值归一化与清洗。
 * <p>
 * 原则：<b>能确定含义的做结构化（件数转 int、箱号去分隔符），不能确定的保留单据原文</b>
 * （如日期与重量单位），避免归一化过程本身引入信息损失或错误假设。
 */
public final class ValueNormalizer {

    /** 空值占位符：表单里常见的划线、N/A 等 */
    private static final Set<String> PLACEHOLDERS = new HashSet<>(Arrays.asList(
            "-", "--", "---", "/", "//", ",", ".", "N/A", "NA", "NIL", "NONE", "TBA", "TBD", "X"));

    private static final Pattern INT_PATTERN = Pattern.compile("(\\d[\\d,\\s]*)");

    private ValueNormalizer() {
    }

    /**
     * 基础清洗：折叠空白、去首尾标点噪声；空值/占位符统一返回 null。
     */
    public static String clean(String raw) {
        if (raw == null) {
            return null;
        }
        String t = raw.replaceAll("\\s+", " ").trim();
        // 去掉首尾孤立的逗号 / 冒号
        t = t.replaceAll("^[,;:\\-\\s]+", "").replaceAll("[,;:\\s]+$", "").trim();
        if (t.isEmpty()) {
            return null;
        }
        if (PLACEHOLDERS.contains(t.toUpperCase(Locale.ROOT))) {
            return null;
        }
        return t;
    }

    /**
     * 提取整数（剥离千分位）。如 "412 CTNS" -> 412、"1,250" -> 1250。
     */
    public static Integer toInteger(String raw) {
        String t = clean(raw);
        if (t == null) {
            return null;
        }
        Matcher m = INT_PATTERN.matcher(t);
        if (!m.find()) {
            return null;
        }
        String digits = m.group(1).replaceAll("[,\\s]", "");
        if (digits.isEmpty() || digits.length() > 9) {
            return null;
        }
        try {
            return Integer.valueOf(digits);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 箱号归一化：去除空格/连字符并大写。
     */
    public static String normalizeContainerNo(String raw) {
        String t = clean(raw);
        if (t == null) {
            return null;
        }
        return t.replaceAll("[\\s\\-]", "").toUpperCase(Locale.ROOT);
    }

    /**
     * 单据号归一化：去除内部空格（OCR 常把 EZI-0022777-9 断成两段）。
     */
    public static String normalizeRefNo(String raw) {
        String t = clean(raw);
        if (t == null) {
            return null;
        }
        return t.toUpperCase(Locale.ROOT);
    }

    /**
     * 重量：保留原文（含千分位与单位），仅折叠空白。
     */
    public static String normalizeWeight(String raw) {
        return clean(raw);
    }

    /**
     * 日期：保留单据原始书写格式，仅做基本清洗。
     * <p>是否转 ISO 交由下游按业务时区/日期序（MM/DD vs DD/MM）决定，解析层不臆测。
     */
    public static String normalizeDate(String raw) {
        String t = clean(raw);
        if (t == null) {
            return null;
        }
        // 剥离常见前缀
        return t.replaceAll("(?i)^(DATE|ETA)\\s*[:：]?\\s*", "").trim();
    }

    /**
     * 判断是否为日期样式（用于告警，不改写值）。
     */
    public static boolean looksLikeDate(String value) {
        return value != null && value.matches(".*\\d{1,4}[/\\-.]\\d{1,2}[/\\-.]\\d{2,4}.*");
    }
}
