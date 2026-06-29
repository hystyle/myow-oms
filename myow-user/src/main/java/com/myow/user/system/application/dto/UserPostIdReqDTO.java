package com.myow.user.system.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPostIdReqDTO {

    @NotNull(message = "userId cannot be null")
    private Long userId;

    @NotNull(message = "positionId cannot be null")
    private Long positionId;
}
