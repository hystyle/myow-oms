package com.myow.user.system.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntIdReqDTO {

    @NotNull(message = "id cannot be null")
    private Integer id;
}
