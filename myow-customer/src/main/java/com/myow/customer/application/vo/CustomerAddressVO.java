package com.myow.customer.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Customer address detail")
public record CustomerAddressVO(
        Long addressId,
        Long tenantId,
        Long customerId,
        String addressType,
        String contactName,
        String phone,
        String country,
        String countryCode,
        String province,
        String city,
        String district,
        String street,
        String zipCode,
        Boolean defaultAddress,
        Integer status,
        LocalDateTime createTime,
        LocalDateTime updateTime) {
}
