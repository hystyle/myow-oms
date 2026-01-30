package com.myow.common.support.serialnumber;

import com.myow.common.support.serialnumber.segment.SerialSegment;

import java.util.List;

/**
 * @author: yss
 * @date: 2026-01-29 21:42
 * @description: 单号模板：定义了单号由哪些段组成，以及拼接顺序
 */
public interface SerialTemplate {
    /**
     * 该模板适用的单号类型（可支持多个别名）
     */
    List<String> getSupportedTypes();

    /**
     * 段列表（有序）
     */
    List<SerialSegment> getSegments();

    /**
     * 默认拼接实现（业务可以覆盖以实现自定义分隔符、校验等）
     */
    default String render(SerialContext context) {
        StringBuilder sb = new StringBuilder();
        for (SerialSegment segment : getSegments()) {
            String part = segment.generate(context);
            if (part != null) {
                sb.append(part);
            }
        }
        return sb.toString();
    }
}
