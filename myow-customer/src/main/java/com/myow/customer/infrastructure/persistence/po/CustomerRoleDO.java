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
@TableName("cm_customer_role")
public class CustomerRoleDO {
    @TableId(value = "customer_role_id", type = IdType.ASSIGN_ID)
    private Long customerRoleId;
    private Long tenantId;
    private Long customerId;
    private String roleType;
    private String roleStatus;
    private String roleCode;
    private Boolean offsetEnabled;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
