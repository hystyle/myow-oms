package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author gemini
 */
@Getter
@Setter
public class CreateTenantReqDTO {
    /**
     * 租户编码（唯一业务标识）
     */
    private String tenantCode;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户套餐ID
     */
    private Long plansId;

    /**
     * 服务到期时间
     */
    private LocalDateTime expireTime;

    /**
     * 用户数量限制（-1表示不限制）
     */
    private Integer accountCount;

    /**
     * 状态：0-正常 1-停用
     */
    private String status;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 统一社会信用代码
     */
    private String licenseNumber;

    /**
     * 企业简介
     */
    private String intro;

    /**
     * 系统访问域名
     */
    private String domain;

    /**
     * 备注信息
     */
    private String remark;
}
