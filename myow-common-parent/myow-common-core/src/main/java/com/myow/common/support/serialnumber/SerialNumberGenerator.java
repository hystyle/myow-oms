package com.myow.common.support.serialnumber;

import org.springframework.lang.Nullable;

import java.time.LocalDate;

/**
 * @author: yss
 * @date: 2026-01-29 20:45
 * @description: 单号生成器
 */
public interface SerialNumberGenerator {

    /**
     * 获取该类型使用的模板
     */
    SerialTemplate getTemplate();

    /**
     * 获取上一个完整单号（仍由业务决定怎么查）
     */
    @Nullable
    String getLastNumber(String type, LocalDate businessDate);

    /**
     * 可选：从上一个完整单号中解析出当时的序列值
     * 默认实现：尝试取最后一段数字
     */
    default Long parsePreviousSeq(String lastFullNo, SerialTemplate template) {
        if (lastFullNo == null) return null;
        // 简单实现：取最后一段数字（可被业务覆盖）
        String[] parts = lastFullNo.split("[-_]");
        if (parts.length == 0) return null;
        String lastPart = parts[parts.length - 1];
        try {
            return Long.parseLong(lastPart);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
