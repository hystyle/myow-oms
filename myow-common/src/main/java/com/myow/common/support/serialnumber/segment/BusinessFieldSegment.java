package com.myow.common.support.serialnumber.segment;

import com.myow.common.support.serialnumber.SerialContext;

import java.util.function.Function;

/**
 * @author: yss
 * @date: 2026-01-29 21:49
 * @description:
 */
public class BusinessFieldSegment implements SerialSegment {
    private final String fieldName;  // 如 "customerCode", "userId"

    private final Function<Object, String> valueExtractor;

    public BusinessFieldSegment(String fieldName, Function<Object, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    @Override
    public String getKey() {
        return "field:" + fieldName;
    }

    @Override
    public String generate(SerialContext context) {
        Object obj = context.getBusinessObject();
        if (obj == null) return "";
        return valueExtractor.apply(obj);
    }
}
