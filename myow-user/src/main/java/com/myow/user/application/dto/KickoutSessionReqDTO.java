package com.myow.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Kickout session request")
public class KickoutSessionReqDTO {

    @Schema(description = "Token value")
    @NotBlank(message = "token cannot be blank")
    private String token;
}
