package com.myow.user.system.application.dto;

import com.myow.common.constant.RegexConst;
import com.myow.common.validator.CheckEnum;
import com.myow.user.system.domain.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * @author yss
 */
@Getter
@Setter
public class UpdateUserReqDTO {

    @Schema(description = "用户ID")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "用户昵称")
    @NotNull(message = "姓名不能为空")
    @Length(max = 30, message = "姓名最多30字符")
    private String nickName;

    @Schema(description = "头像")
    @NotBlank(message = "头像不能为空哦")
    private String avatar;

    @Schema(description = "部门ID")
    @NotNull(message = "部门id不能为空")
    private Long deptId;

    @Schema(description = "用户性别")
    @CheckEnum(value = GenderEnum.class, message = "性别错误")
    private Integer gender;

    @Schema(description = "用户邮箱")
    @Pattern(regexp = RegexConst.EMAIL, message = "邮箱账号格式不正确")
    private String email;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "岗位ID")
    private Long positionId;

    @Schema(description = "角色列表")
    private List<Long> roleIdList;

    @Schema(description = "备注")
    @Length(max = 200, message = "备注最多200字符")
    private String remark;
}
