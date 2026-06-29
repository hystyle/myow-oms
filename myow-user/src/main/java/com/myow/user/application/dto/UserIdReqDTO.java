package com.myow.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User id request")
public class UserIdReqDTO {

    @Schema(description = "User id")
    @NotNull(message = "userId cannot be null")
    private Long userId;
}
