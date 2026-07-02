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
@Tag(name = "用户中心-角色权限", description = "内部角色、功能权限和数据范围管理接口")
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    @Operation(summary = "创建角色", description = "创建内部角色，配置角色编码、名称、状态、排序和数据范围。")
    @SaCheckPermission("system:role:add")
    public Result<Long> createRole(@RequestBody @Validated CreateRoleReqDTO createReqDTO) {
        return Result.success(roleService.createRole(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新角色", description = "更新角色基础信息和数据范围。角色权限树保存应通过角色菜单聚合接口接入。")
    @SaCheckPermission("system:role:update")
    public Result<Boolean> updateRole(@RequestBody @Validated UpdateRoleReqDTO updateReqDTO) {
        roleService.updateRole(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除角色", description = "删除指定角色。删除前应确认该角色是否仍被用户引用。")
    @SaCheckPermission("system:role:delete")
    public Result<Boolean> deleteRole(@RequestBody @Validated IdReqDTO reqDTO) {
        roleService.deleteRole(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取角色详情", description = "根据角色 ID 获取角色基础信息、状态和数据范围。")
    @SaCheckPermission("system:role:query")
    public Result<RoleRespDTO> getRole(@RequestBody @Validated IdReqDTO reqDTO) {
        return Result.success(roleService.getRole(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询角色", description = "分页查询角色列表，用于管理端角色权限左右分栏页面。")
    @SaCheckPermission("system:role:query")
    public Result<PageResult<RoleRespDTO>> getRolePage(@RequestBody PageRoleReqDTO pageRoleReqDTO) {
        return Result.success(roleService.getRolePage(pageRoleReqDTO));
    }
}
