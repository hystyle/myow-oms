package com.myow.user.system.application.dto;

import com.myow.common.constant.RegexConst;
import com.myow.common.validator.CheckEnum;
import com.myow.user.system.domain.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author: yss
 * @date: 2026-01-30 23:13
 * @description: 创建用户
 */
@Data
@NoArgsConstructor
public class CreateUserDTO {

    @Schema(description = "登录账号")
    @NotNull(message = "登录账号不能为空")
    @Length(max = 30, message = "登录账号最多30字符")
    private String loginName;

    @Schema(description = "用户昵称")
    @NotNull(message = "姓名不能为空")
    @Length(max = 30, message = "姓名最多30字符")
    private String nickName;

    @Schema(description = "用户性别")
    @CheckEnum(value = GenderEnum.class, message = "性别错误")
    private Integer gender;

    @Schema(description = "用户邮箱")
    @Pattern(regexp = RegexConst.EMAIL, message = "邮箱账号格式不正确")
    private String email;

    @Schema(description = "手机号码")
    @Pattern(regexp = RegexConst.PHONE_REGEXP, message = "手机号码格式错误")
    private String phone;

    @Schema(description = "备注")
    @Length(max = 200, message = "备注最多200字符")
    private String remark;

    public CreateUserDTO(String loginName, String nickName, String  phone) {
        this.loginName = loginName;
        this.nickName = nickName;
        this.phone = phone;
    }
}
