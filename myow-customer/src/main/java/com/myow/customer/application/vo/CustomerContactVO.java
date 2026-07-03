package com.myow.customer.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Customer contact detail")
public record CustomerContactVO(
        Long contactId,
        Long tenantId,
        Long customerId,
        String contactName,
        String contactRole,
        String position,
        String phone,
        String email,
        String socialAccount,
        Boolean primary,
        Integer status,
        LocalDateTime createTime,
        LocalDateTime updateTime) {
}
