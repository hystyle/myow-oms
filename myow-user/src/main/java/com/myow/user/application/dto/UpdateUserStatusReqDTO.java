package com.myow.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Update user status request")
public class UpdateUserStatusReqDTO {

    @Schema(description = "User id")
    @NotNull(message = "userId cannot be null")
    private Long userId;

    @Schema(description = "Enabled status")
    @NotNull(message = "status cannot be null")
    private Boolean status;
}
