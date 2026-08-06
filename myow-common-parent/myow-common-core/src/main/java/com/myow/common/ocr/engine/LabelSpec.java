package com.myow.common.ocr.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 字段抽取声明：<b>用"标签"定位，而不是用固定坐标或行号</b>。
 * <p>
 * 这是模板可维护性的关键——换一家船公司的 DO 版式，只需增删 alias，无需改一行解析代码。
 */
public final class LabelSpec {

    public enum MatchMode {
        /** 归一化后完全相等 */
        EXACT,
        /** 归一化后以 alias 开头（用于超长表头，如 DESCRIPTION OF ARTICLES...） */
        PREFIX,
        /** 归一化后包含 alias */
        CONTAINS
    }

    /** 字段唯一键 */
    private final String key;
    /** 标签同义词（原文书写即可，内部会归一化） */
    private final List<String> aliases;
    private final MatchMode matchMode;
    /**
     * 同行锚点标签：要求命中标签所在行内还存在该标签。
     * <p>用于消歧，例如 WEIGHT 同时出现在货物表头与集装箱子表头。
     */
    private final String anchorLabel;
    /** 取值最多向下取几行 */
    private final int maxValueLines;
    /** 是否必填（缺失时计入 missingFields） */
    private final boolean required;

    private LabelSpec(Builder b) {
        this.key = b.key;
        this.aliases = Collections.unmodifiableList(new ArrayList<>(b.aliases));
        this.matchMode = b.matchMode;
        this.anchorLabel = b.anchorLabel;
        this.maxValueLines = b.maxValueLines;
        this.required = b.required;
    }

    public static Builder key(String key) {
        return new Builder(key);
    }

    public String getKey() {
        return key;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public MatchMode getMatchMode() {
        return matchMode;
    }

    public String getAnchorLabel() {
        return anchorLabel;
    }

    public int getMaxValueLines() {
        return maxValueLines;
    }

    public boolean isRequired() {
        return required;
    }

    /**
     * 标签归一化：大写、去除 . : 等噪声标点、折叠空白。
     * <p>让 "Cust. Ref. No." / "CUST REF NO" / "cust.ref.no" 归一到同一形态。
     */
    public static String normalize(String raw) {
        if (raw == null) {
            return "";
        }
        String t = raw.toUpperCase(Locale.ROOT);
        t = t.replaceAll("[.:;：；]", " ");
        t = t.replaceAll("\\s+", " ").trim();
        return t;
    }

    public static final class Builder {
        private final String key;
        private List<String> aliases = new ArrayList<>();
        private MatchMode matchMode = MatchMode.EXACT;
        private String anchorLabel;
        private int maxValueLines = 1;
        private boolean required = false;

        private Builder(String key) {
            this.key = key;
        }

        public Builder aliases(String... values) {
            this.aliases = new ArrayList<>(Arrays.asList(values));
            return this;
        }

        public Builder mode(MatchMode mode) {
            this.matchMode = mode;
            return this;
        }

        public Builder anchor(String anchorLabel) {
            this.anchorLabel = anchorLabel;
            return this;
        }

        public Builder maxValueLines(int n) {
            this.maxValueLines = n;
            return this;
        }

        public Builder required() {
            this.required = true;
            return this;
        }

        public LabelSpec build() {
            return new LabelSpec(this);
        }
    }
}
