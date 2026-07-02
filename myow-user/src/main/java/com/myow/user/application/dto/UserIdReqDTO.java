package com.myow.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "用户 ID 请求")
public class UserIdReqDTO {

    @Schema(description = "用户 ID", example = "10001")
    @NotNull(message = "userId cannot be null")
    private Long userId;
}
