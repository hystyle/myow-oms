package com.myow.common.support.serialnumber.segment;

import com.myow.common.support.serialnumber.SerialContext;

import java.time.format.DateTimeFormatter;

/**
 * @author: yss
 * @date: 2026-01-29 21:46
 * @description:
 */
public class DateSegment implements SerialSegment {

    private final String pattern;
    private final DateTimeFormatter formatter;

    public DateSegment(String pattern) {
        this.pattern = pattern;
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public String getKey() {
        return "date:" + pattern;
    }

    @Override
    public String generate(SerialContext context) {
        return context.getDate().format(formatter);
    }
}