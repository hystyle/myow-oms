package com.myow.user.system.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Id request")
public class IdReqDTO {

    @Schema(description = "Primary id")
    @NotNull(message = "id cannot be null")
    private Long id;
}
