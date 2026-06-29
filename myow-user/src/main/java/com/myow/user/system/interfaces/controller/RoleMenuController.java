package com.myow.user.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateRoleMenuReqDTO;
import com.myow.user.system.application.dto.PageRoleMenuReqDTO;
import com.myow.user.system.application.dto.RoleMenuIdReqDTO;
import com.myow.user.system.application.dto.RoleMenuRespDTO;
import com.myow.user.system.application.dto.UpdateRoleMenuReqDTO;
import com.myow.user.system.application.service.RoleMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-角色菜单关联")
@RestController
@RequestMapping("/system/role-menu")
@RequiredArgsConstructor
public class RoleMenuController {

    private final RoleMenuService roleMenuService;

    @PostMapping("/create")
    @Operation(summary = "创建角色菜单关联")
    public Result<Boolean> createRoleMenu(@RequestBody CreateRoleMenuReqDTO createReqDTO) {
        return Result.success(roleMenuService.createRoleMenu(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新角色菜单关联")
    public Result<Boolean> updateRoleMenu(@RequestBody @Validated UpdateRoleMenuReqDTO updateReqDTO) {
        roleMenuService.updateRoleMenu(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除角色菜单关联")
    public Result<Boolean> deleteRoleMenu(@RequestBody @Validated RoleMenuIdReqDTO reqDTO) {
        roleMenuService.deleteRoleMenu(reqDTO.getRoleId(), reqDTO.getMenuId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取角色菜单关联")
    public Result<RoleMenuRespDTO> getRoleMenu(@RequestBody @Validated RoleMenuIdReqDTO reqDTO) {
        return Result.success(roleMenuService.getRoleMenu(reqDTO.getRoleId(), reqDTO.getMenuId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取角色菜单关联分页")
    public Result<PageResult<RoleMenuRespDTO>> getRoleMenuPage(@RequestBody PageRoleMenuReqDTO pageRoleMenuReqDTO) {
        return Result.success(roleMenuService.getRoleMenuPage(pageRoleMenuReqDTO));
    }
}
