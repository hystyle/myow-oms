package com.myow.system.application.dto;

import com.myow.common.constant.RegexConst;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * @author yss
 */
@Getter
@Setter
public class CreateTenantReqDTO {

    @Schema(description = "租户编码（唯一业务标识）")
    @NotNull(message = "租户编码不能为空")
    private String tenantCode;

    @Schema(description = "租户名称")
    @NotNull(message = "租户名称不能为空")
    @Length(max = 30, message = "租户名称最多30字符")
    private String name;

    @Schema(description = "租户套餐ID")
    @NotNull(message = "租户套餐id不能为空")
    private Long plansId;

    @Schema(description = "服务到期时间")
    private LocalDateTime expireTime;

    @Schema(description = "用户数量限制（-1表示不限制）")
    private Integer accountCount;

    @Schema(description = "联系人姓名")
    @NotNull(message = "联系人姓名不能为空")
    private String contactName;

    @Schema(description = "联系人电话")
    @NotNull(message = "联系人电话不能为空")
    @Pattern(regexp = RegexConst.PHONE_REGEXP, message = "手机号码格式错误")
    private String contactPhone;

    @Schema(description = "联系地址")
    private String address;

    @Schema(description = "统一社会信用代码")
    private String licenseNumber;

    @Schema(description = "企业简介")
    @Length(max = 200, message = "企业简介最多200字符")
    private String intro;

    @Schema(description = "系统访问域名")
    private String domain;

    @Schema(description = "备注信息")
    @Length(max = 200, message = "备注最多200字符")
    private String remark;
}
