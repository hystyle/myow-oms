package com.myow.user.application.dto;

import com.myow.common.constant.RegexConst;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class CreateUserDTO {

    @Schema(description = "Login account")
    @NotNull(message = "loginName cannot be null")
    @Length(max = 30, message = "loginName max length is 30")
    private String loginName;

    @Schema(description = "User nickname")
    @NotNull(message = "nickName cannot be null")
    @Length(max = 30, message = "nickName max length is 30")
    private String nickName;

    @Schema(description = "User gender")
    private Integer gender;

    @Schema(description = "Email")
    @Pattern(regexp = RegexConst.EMAIL, message = "email format is invalid")
    private String email;

    @Schema(description = "Phone")
    @Pattern(regexp = RegexConst.PHONE_REGEXP, message = "phone format is invalid")
    private String phone;

    @Schema(description = "Remark")
    @Length(max = 200, message = "remark max length is 200")
    private String remark;

    public CreateUserDTO(String loginName, String nickName, String phone) {
        this.loginName = loginName;
        this.nickName = nickName;
        this.phone = phone;
    }
}
