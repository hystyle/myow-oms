package com.myow.common.ocr.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 解析结果包装体：业务数据 + 解析元信息。
 * <p>
 * 元信息不进入对外 JSON，但对<b>人工复核队列</b>与<b>准确率监控</b>至关重要：
 * 例如 OCR 通道下 avgConfidence &lt; 80 或 missingFields 非空时，应自动转人工。
 */
@Data
public class ParseResult {

    /** 标准单证数据（即目标 JSON） */
    private DeliveryOrderDoc document = new DeliveryOrderDoc();

    /** 数据来源通道 */
    private ParseSource source;

    /** 页数 */
    private int pageCount;

    /** 耗时（毫秒） */
    private long costMillis;

    /** OCR 平均置信度（文本层为 100） */
    private float avgConfidence;

    /** 多箱场景下的全部集装箱行（document.container 取第一条） */
    private List<ContainerInfo> containers = new ArrayList<>();

    /** 命中的原始字段值（key -> 原文），便于排查与回溯 */
    private Map<String, String> rawFields = new LinkedHashMap<>();

    /** 未能提取到的必填字段 */
    private List<String> missingFields = new ArrayList<>();

    /** 解析告警（如箱号校验位不符、日期格式异常等） */
    private List<String> warnings = new ArrayList<>();

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    /** 是否建议人工复核 */
    public boolean needsManualReview() {
        return !missingFields.isEmpty()
                || !warnings.isEmpty()
                || (source != ParseSource.PDF_TEXT_LAYER && avgConfidence < 80f);
    }
}
