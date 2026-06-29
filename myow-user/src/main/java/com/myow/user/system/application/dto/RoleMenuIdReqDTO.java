package com.myow.user.system.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleMenuIdReqDTO {

    @NotNull(message = "roleId cannot be null")
    private Long roleId;

    @NotNull(message = "menuId cannot be null")
    private Long menuId;
}
