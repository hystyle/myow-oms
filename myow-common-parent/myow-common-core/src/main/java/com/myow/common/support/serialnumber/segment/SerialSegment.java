package com.myow.common.support.serialnumber.segment;

import com.myow.common.support.serialnumber.SerialContext;

/**
 * @author: yss
 * @date: 2026-01-29 21:39
 * @description: 单号的一个组成部分（段）
 */
public interface SerialSegment {

    /**
     * 段的唯一标识（用于调试、日志、配置）
     */
    String getKey();

    /**
     * 是否需要依赖序列号上下文（只有 SEQ 段需要）
     */
    default boolean requiresSequence() {
        return false;
    }

    /**
     * 生成该段的内容
     *
     * @param context 上下文（包含 type、date、businessObject、currentSeq 等）
     * @return 该段的字符串值
     */
    String generate(SerialContext context);
}
