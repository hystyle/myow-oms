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
@TableName("cm_customer_relation")
public class CustomerRelationDO {
    @TableId(value = "relation_id", type = IdType.ASSIGN_ID)
    private Long relationId;
    private Long tenantId;
    private Long parentCustomerId;
    private Long childCustomerId;
    private String relationType;
    private Boolean settlementIndependent;
    private Integer status;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
