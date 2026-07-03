package com.myow.customer.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("cm_customer_kyc")
public class CustomerKycDO {
    @TableId(value = "kyc_id", type = IdType.ASSIGN_ID)
    private Long kycId;
    private Long tenantId;
    private Long customerId;
    private String kycType;
    private String auditStatus;
    private Long auditBy;
    private LocalDateTime auditTime;
    private String rejectReason;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
