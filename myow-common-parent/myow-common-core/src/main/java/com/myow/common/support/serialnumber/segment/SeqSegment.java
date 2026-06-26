package com.myow.common.support.serialnumber.segment;

import com.myow.common.support.serialnumber.SerialContext;

/**
 * @author: yss
 * @date: 2026-01-29 21:47
 * @description:
 */
public class SeqSegment implements SerialSegment {
    private final int length;       // 位数
    private final char padChar;     // 补位字符，通常 '0'
    private final String resetScope;// "DAY","MONTH","YEAR","NONE","CUSTOMER"等

    public SeqSegment(int length, char padChar, String resetScope) {
        this.length = length;
        this.padChar = padChar;
        this.resetScope = resetScope;
    }

    @Override
    public String getKey() {
        return "seq:" + length + ":" + resetScope;
    }

    @Override
    public boolean requiresSequence() {
        return true;
    }

    @Override
    public String generate(SerialContext context) {
        if (context.getCurrentSeq() == null) {
            throw new IllegalStateException("SEQ 段需要 currentSeq");
        }
        return String.format("%0" + length + "d", context.getCurrentSeq());
    }
}
