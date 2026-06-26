package com.myow.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateUserReqDTO extends CreateUserDTO {

    @Schema(description = "Department id")
    @NotNull(message = "deptId cannot be null")
    private Long deptId;

    @Schema(description = "Position id")
    private Long positionId;

    @Schema(description = "Role id list")
    private List<Long> roleIdList;
}
