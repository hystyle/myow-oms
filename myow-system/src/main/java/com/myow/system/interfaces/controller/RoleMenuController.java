package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateRoleMenuReqDTO;
import com.myow.system.application.dto.PageRoleMenuReqDTO;
import com.myow.system.application.dto.RoleMenuRespDTO;
import com.myow.system.application.dto.UpdateRoleMenuReqDTO;
import com.myow.system.application.service.RoleMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author gemini
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

    @PutMapping("/update")
    @Operation(summary = "更新角色菜单关联")
    public Result<Boolean> updateRoleMenu(@RequestBody UpdateRoleMenuReqDTO updateReqDTO) {
        roleMenuService.updateRoleMenu(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{roleId}/{menuId}")
    @Operation(summary = "删除角色菜单关联")
    public Result<Boolean> deleteRoleMenu(
            @PathVariable("roleId") Long roleId,
            @PathVariable("menuId") Long menuId) {
        roleMenuService.deleteRoleMenu(roleId, menuId);
        return Result.success(true);
    }

    @GetMapping("/get/{roleId}/{menuId}")
    @Operation(summary = "获取角色菜单关联")
    public Result<RoleMenuRespDTO> getRoleMenu(
            @PathVariable("roleId") Long roleId,
            @PathVariable("menuId") Long menuId) {
        return Result.success(roleMenuService.getRoleMenu(roleId, menuId));
    }

    @GetMapping("/page")
    @Operation(summary = "获取角色菜单关联分页")
    public Result<PageResult<RoleMenuRespDTO>> getRoleMenuPage(PageRoleMenuReqDTO pageRoleMenuReqDTO) {
        return Result.success(roleMenuService.getRoleMenuPage(pageRoleMenuReqDTO));
    }
}
