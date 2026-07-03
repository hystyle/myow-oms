package com.myow.customer.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer option for role based selectors")
public record CustomerOptionVO(
        Long customerId,
        String customerCode,
        String customerName,
        String status) {
}
