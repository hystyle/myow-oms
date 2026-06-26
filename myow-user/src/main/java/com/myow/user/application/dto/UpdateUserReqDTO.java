package com.myow.user.application.dto;

import com.myow.common.constant.RegexConst;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
public class UpdateUserReqDTO {

    @Schema(description = "User id")
    @NotNull(message = "userId cannot be null")
    private Long userId;

    @Schema(description = "User nickname")
    @NotNull(message = "nickName cannot be null")
    @Length(max = 30, message = "nickName max length is 30")
    private String nickName;

    @Schema(description = "Avatar file id")
    private String avatar;

    @Schema(description = "Department id")
    @NotNull(message = "deptId cannot be null")
    private Long deptId;

    @Schema(description = "User gender")
    private Integer gender;

    @Schema(description = "Email")
    @Pattern(regexp = RegexConst.EMAIL, message = "email format is invalid")
    private String email;

    @Schema(description = "Phone")
    private String phone;

    @Schema(description = "Position id")
    private Long positionId;

    @Schema(description = "Role id list")
    private List<Long> roleIdList;

    @Schema(description = "Remark")
    @Length(max = 200, message = "remark max length is 200")
    private String remark;
}
