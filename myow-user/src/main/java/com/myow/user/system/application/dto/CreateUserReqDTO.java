package com.myow.user.system.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author yss
 */
@Getter
@Setter
public class CreateUserReqDTO extends CreateUserDTO{

    @Schema(description = "部门ID")
    @NotNull(message = "部门id不能为空")
    private Long deptId;

    @Schema(description = "岗位ID")
    private Long positionId;

    @Schema(description = "角色列表")
    private List<Long> roleIdList;

}
