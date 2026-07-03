package com.myow.overseas.application.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Logistics product detail")
public record LogisticsProductVO(
        Long productId,
        Long tenantId,
        String productCode,
        String productName,
        Long carrierCustomerId,
        String productType,
        Long defaultChannelId,
        String defaultDecisionStrategy,
        String status,
        String remark,
        LocalDateTime createTime,
        LocalDateTime updateTime) {
}
