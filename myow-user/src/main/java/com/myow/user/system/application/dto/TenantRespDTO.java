package com.myow.user.system.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author yss
 */
@Getter
@Setter
public class TenantRespDTO {

    @Schema(description = "租户id")
    private Long tenantId;

    @Schema(description = "租户编码（唯一业务标识）")
    private String tenantCode;

    @Schema(description = "租户名称")
    private String name;

    @Schema(description = "租户套餐ID")
    private Long plansId;

    @Schema(description = "租户套餐名称")
    private String plansName;

    @Schema(description = "服务到期时间")
    private LocalDateTime expireTime;

    @Schema(description = "用户数量限制（-1表示不限制）")
    private Integer accountCount;

    @Schema(description = "状态")
    private Boolean status;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "联系地址")
    private String address;

    @Schema(description = "统一社会信用代码")
    private String licenseNumber;

    @Schema(description = "企业简介")
    private String intro;

    @Schema(description = "系统访问域名")
    private String domain;

    @Schema(description = "备注信息")
    private String remark;

    @Schema(description = "用户数量")
    private Long userCount;
}
