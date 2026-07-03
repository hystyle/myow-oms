package com.myow.customer.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Customer business role detail")
public record CustomerRoleVO(
        Long customerRoleId,
        Long tenantId,
        Long customerId,
        String roleType,
        String roleStatus,
        String roleCode,
        Boolean offsetEnabled,
        String remark,
        LocalDateTime createTime,
        LocalDateTime updateTime) {
}
