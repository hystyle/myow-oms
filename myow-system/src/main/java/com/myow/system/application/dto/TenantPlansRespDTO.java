package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author gemini
 */
@Getter
@Setter
public class TenantPlansRespDTO {
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
     * 删除标志（0代表存在 1代表删除）
     */
    private String deletedFlag;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 套餐代码
     */
    private String plansCode;
}
