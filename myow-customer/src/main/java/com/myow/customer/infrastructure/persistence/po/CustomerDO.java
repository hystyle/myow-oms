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
@TableName("cm_customer")
public class CustomerDO {
    @TableId(value = "customer_id", type = IdType.ASSIGN_ID)
    private Long customerId;
    private Long tenantId;
    private String customerCode;
    private String customerName;
    private String customerType;
    private String customerLevel;
    private String bizLicenseNo;
    private String taxNo;
    private String settlementType;
    private String defaultCurrency;
    private String status;
    private Long salesOwnerId;
    private Long ownerDeptId;
    private String poolStatus;
    private LocalDateTime registerTime;
    private LocalDateTime auditTime;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
