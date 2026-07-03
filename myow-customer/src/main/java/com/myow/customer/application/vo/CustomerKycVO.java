package com.myow.customer.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Customer KYC audit detail")
public record CustomerKycVO(
        Long kycId,
        Long tenantId,
        Long customerId,
        String kycType,
        String auditStatus,
        Long auditBy,
        LocalDateTime auditTime,
        String rejectReason,
        String remark,
        LocalDateTime createTime,
        LocalDateTime updateTime) {
}
