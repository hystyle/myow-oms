package com.myow.system.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(description = "用户登录请求")
public class LoginReqDTO {

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String loginName;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "登录端")
    @NotBlank(message = "登录端不能为空")
    private String loginClient;
}
