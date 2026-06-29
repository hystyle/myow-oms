package com.myow.user.application.dto;

import com.myow.common.constant.RegexConst;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Schema(description = "Update current user profile request")
public class UpdateProfileReqDTO {

    @Schema(description = "User nickname")
    @Length(max = 30, message = "nickName max length is 30")
    private String nickName;

    @Schema(description = "Email")
    @Pattern(regexp = RegexConst.EMAIL, message = "email format is invalid")
    private String email;

    @Schema(description = "Phone")
    @Pattern(regexp = RegexConst.PHONE_REGEXP, message = "phone format is invalid")
    private String phone;

    @Schema(description = "Gender")
    private Integer gender;

    @Schema(description = "Avatar file id")
    private Long avatar;
}
