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
@TableName("cm_customer_address")
public class CustomerAddressDO {
    @TableId(value = "address_id", type = IdType.ASSIGN_ID)
    private Long addressId;
    private Long tenantId;
    private Long customerId;
    private String addressType;
    private String contactName;
    private String phone;
    private String country;
    private String countryCode;
    private String province;
    private String city;
    private String district;
    private String street;
    private String zipCode;
    @TableField("is_default")
    private Boolean defaultAddress;
    private Integer status;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
