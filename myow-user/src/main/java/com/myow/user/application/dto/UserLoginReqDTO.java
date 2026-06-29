package com.myow.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "User login request")
public class UserLoginReqDTO {

    @Schema(description = "Login name")
    @NotBlank(message = "Login name cannot be blank")
    private String loginName;

    @Schema(description = "Password")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Schema(description = "Login client")
    @NotBlank(message = "Login client cannot be blank")
    private String loginClient;

    @Schema(description = "Captcha uuid")
    private String captchaUuid;

    @Schema(description = "Captcha code")
    private String captchaCode;
}
