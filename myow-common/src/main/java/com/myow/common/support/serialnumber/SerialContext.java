package com.myow.common.support.serialnumber;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @author: yss
 * @date: 2026-01-29 21:40
 * @description: 单号生成上下文
 */
@Getter
@Builder
public class SerialContext {
    private final String type;                // ORDER, CONTRACT, USER 等
    private final LocalDate date;             // 业务日期
    private final Object businessObject;      // 可选：订单实体、用户实体等，供自定义段读取字段
    private final Long currentSeq;            // 当前要使用的序列号（从1开始）
    // 可扩展：tenantId, branchId, operatorId 等
}
