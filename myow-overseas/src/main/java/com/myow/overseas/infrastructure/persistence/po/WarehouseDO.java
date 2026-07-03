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
@TableName("owh_physical_warehouse")
public class WarehouseDO {
    @TableId(value = "warehouse_id", type = IdType.ASSIGN_ID)
    private Long warehouseId;
    private Long tenantId;
    private String warehouseCode;
    private String warehouseName;
    private Long serviceProviderCustomerId;
    private String cooperationType;
    private Long wmsSystemId;
    private String externalWarehouseCode;
    private String countryCode;
    private String state;
    private String city;
    private String postalCode;
    private String addressLine1;
    private String addressLine2;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String timezone;
    private String status;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
