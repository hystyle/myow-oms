package com.myow.user.system.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "角色自定义部门数据范围 ID 列表请求")
public class RoleDeptIdsReqDTO {

    @Schema(description = "角色 ID", example = "100")
    @NotNull(message = "roleId cannot be null")
    private Long roleId;

    @Schema(description = "部门 ID 列表；当角色数据范围为自定义部门时生效", example = "[10,11,12]")
    private List<Long> deptIdList;
}
