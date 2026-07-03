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
@TableName("cm_customer_blacklist")
public class CustomerBlacklistDO {
    @TableId(value = "blacklist_id", type = IdType.ASSIGN_ID)
    private Long blacklistId;
    private Long tenantId;
    private String targetType;
    private String targetValue;
    private String riskLevel;
    private String reason;
    private Long sourceCustomerId;
    private String status;
    private LocalDateTime effectiveTime;
    private LocalDateTime expireTime;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
