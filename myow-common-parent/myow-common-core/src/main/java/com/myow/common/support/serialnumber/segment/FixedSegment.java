package com.myow.common.support.serialnumber.segment;

import com.myow.common.support.serialnumber.SerialContext;

/**
 * @author: yss
 * @date: 2026-01-29 21:46
 * @description:
 */
public class FixedSegment implements SerialSegment {
    private final String value;

    public FixedSegment(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return "fixed:" + value;
    }

    @Override
    public String generate(SerialContext context) {
        return value;
    }
}
