package com.myow.customer.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("cm_customer_contact")
public class CustomerContactDO {
    @TableId(value = "contact_id", type = IdType.ASSIGN_ID)
    private Long contactId;
    private Long tenantId;
    private Long customerId;
    private String contactName;
    private String contactRole;
    private String position;
    private String phone;
    private String email;
    private String socialAccount;
    @TableField("is_primary")
    private Boolean primary;
    private Integer status;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
