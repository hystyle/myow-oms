package com.myow.customer.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Customer blacklist detail")
public record CustomerBlacklistVO(
        Long blacklistId,
        Long tenantId,
        String targetType,
        String targetValue,
        String riskLevel,
        String reason,
        Long sourceCustomerId,
        String status,
        LocalDateTime effectiveTime,
        LocalDateTime expireTime,
        LocalDateTime createTime,
        LocalDateTime updateTime) {
}
