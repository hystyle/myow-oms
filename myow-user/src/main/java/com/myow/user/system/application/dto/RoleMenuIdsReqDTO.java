package com.myow.user.system.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "角色菜单权限 ID 列表请求")
public class RoleMenuIdsReqDTO {

    @Schema(description = "角色 ID", example = "100")
    @NotNull(message = "roleId cannot be null")
    private Long roleId;

    @Schema(description = "菜单 ID 列表；包含目录、菜单和按钮权限点", example = "[1000,1001,1002]")
    private List<Long> menuIdList;
}
