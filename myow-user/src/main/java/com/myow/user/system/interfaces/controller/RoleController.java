package com.myow.user.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateRoleReqDTO;
import com.myow.user.system.application.dto.IdReqDTO;
import com.myow.user.system.application.dto.PageRoleReqDTO;
import com.myow.user.system.application.dto.RoleRespDTO;
import com.myow.user.system.application.dto.UpdateRoleReqDTO;
import com.myow.user.system.application.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-角色")
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    @Operation(summary = "创建角色")
    @SaCheckPermission("system:role:add")
    public Result<Long> createRole(@RequestBody @Validated CreateRoleReqDTO createReqDTO) {
        return Result.success(roleService.createRole(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新角色")
    @SaCheckPermission("system:role:update")
    public Result<Boolean> updateRole(@RequestBody @Validated UpdateRoleReqDTO updateReqDTO) {
        roleService.updateRole(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除角色")
    @SaCheckPermission("system:role:delete")
    public Result<Boolean> deleteRole(@RequestBody @Validated IdReqDTO reqDTO) {
        roleService.deleteRole(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取角色")
    @SaCheckPermission("system:role:query")
    public Result<RoleRespDTO> getRole(@RequestBody @Validated IdReqDTO reqDTO) {
        return Result.success(roleService.getRole(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取角色分页")
    @SaCheckPermission("system:role:query")
    public Result<PageResult<RoleRespDTO>> getRolePage(@RequestBody PageRoleReqDTO pageRoleReqDTO) {
        return Result.success(roleService.getRolePage(pageRoleReqDTO));
    }
}
