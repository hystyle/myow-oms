package com.myow.customer.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Customer relation detail")
public record CustomerRelationVO(
        Long relationId,
        Long tenantId,
        Long parentCustomerId,
        String parentCustomerName,
        Long childCustomerId,
        String childCustomerName,
        String relationType,
        Boolean settlementIndependent,
        Integer status,
        String remark,
        LocalDateTime createTime,
        LocalDateTime updateTime) {
}
