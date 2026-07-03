package com.myow.customer.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Customer attachment index detail")
public record CustomerAttachmentVO(
        Long attachmentId,
        Long tenantId,
        Long customerId,
        String attachmentType,
        Long fileId,
        String fileName,
        LocalDate expireDate,
        String auditStatus,
        String remark,
        LocalDateTime createTime,
        LocalDateTime updateTime) {
}
