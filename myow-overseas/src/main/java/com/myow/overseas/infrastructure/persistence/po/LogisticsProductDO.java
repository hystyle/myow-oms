package com.myow.overseas.infrastructure.persistence.po;

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
@TableName("owh_logistics_product")
public class LogisticsProductDO {
    @TableId(value = "product_id", type = IdType.ASSIGN_ID)
    private Long productId;
    private Long tenantId;
    private String productCode;
    private String productName;
    private Long carrierCustomerId;
    private String productType;
    private Long defaultChannelId;
    private String defaultDecisionStrategy;
    private String status;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
