package com.myow.overseas.application.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Physical warehouse detail")
public record WarehouseVO(
        Long warehouseId,
        Long tenantId,
        String warehouseCode,
        String warehouseName,
        Long serviceProviderCustomerId,
        String cooperationType,
        Long wmsSystemId,
        String externalWarehouseCode,
        String countryCode,
        String state,
        String city,
        String postalCode,
        String addressLine1,
        String addressLine2,
        String contactName,
        String contactPhone,
        String contactEmail,
        String timezone,
        String status,
        String remark,
        LocalDateTime createTime,
        LocalDateTime updateTime) {
}
