package com.myow.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "更新用户状态请求")
public class UpdateUserStatusReqDTO {

    @Schema(description = "用户 ID", example = "10001")
    @NotNull(message = "userId cannot be null")
    private Long userId;

    @Schema(description = "启用状态，true 表示启用，false 表示停用", example = "true")
    @NotNull(message = "status cannot be null")
    private Boolean status;
}
