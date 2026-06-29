package com.myow.user.system.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDeptIdReqDTO {

    @NotNull(message = "roleId cannot be null")
    private Long roleId;

    @NotNull(message = "deptId cannot be null")
    private Long deptId;
}
