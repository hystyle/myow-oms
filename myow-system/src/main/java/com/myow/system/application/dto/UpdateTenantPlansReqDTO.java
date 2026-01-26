package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class UpdateTenantPlansReqDTO {
    /**
     * 租户套餐id
     */
    private Long plansId;

    /**
     * 套餐名称
     */
    private String plansName;

    /**
     * 价格类型
     */
    private String priceType;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 套餐代码
     */
    private String plansCode;
}
