package com.myow.customer.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Customer detail")
public record CustomerVO(
        Long customerId,
        Long tenantId,
        String customerCode,
        String customerName,
        String customerType,
        String customerLevel,
        String bizLicenseNo,
        String taxNo,
        String settlementType,
        String defaultCurrency,
        String status,
        Long salesOwnerId,
        Long ownerDeptId,
        String poolStatus,
        LocalDateTime registerTime,
        LocalDateTime auditTime,
        String remark,
        LocalDateTime createTime,
        LocalDateTime updateTime) {
}
