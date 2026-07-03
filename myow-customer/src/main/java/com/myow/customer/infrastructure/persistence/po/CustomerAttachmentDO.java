package com.myow.customer.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("cm_customer_attachment")
public class CustomerAttachmentDO {
    @TableId(value = "attachment_id", type = IdType.ASSIGN_ID)
    private Long attachmentId;
    private Long tenantId;
    private Long customerId;
    private String attachmentType;
    private Long fileId;
    private String fileName;
    private LocalDate expireDate;
    private String auditStatus;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
