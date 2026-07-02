package com.myow.user.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateRoleMenuReqDTO;
import com.myow.user.system.application.dto.PageRoleMenuReqDTO;
import com.myow.user.system.application.dto.RoleMenuIdsReqDTO;
import com.myow.user.system.application.dto.RoleMenuIdReqDTO;
import com.myow.user.system.application.dto.RoleMenuRespDTO;
import com.myow.user.system.application.dto.UpdateRoleMenuReqDTO;
import com.myow.user.system.application.service.RoleMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yss
 */
@Tag(name = "用户中心-角色菜单权限", description = "角色与菜单、按钮权限点的授权关系接口")
@RestController
@RequestMapping("/system/role-menu")
@RequiredArgsConstructor
public class RoleMenuController {

    private final RoleMenuService roleMenuService;

    @PostMapping("/create")
    @Operation(summary = "创建角色菜单关联", description = "为角色新增一条菜单或按钮权限点授权关系。")
    @SaCheckPermission("system:role:update")
    public Result<Boolean> createRoleMenu(@RequestBody CreateRoleMenuReqDTO createReqDTO) {
        return Result.success(roleMenuService.createRoleMenu(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新角色菜单关联", description = "更新一条角色菜单关联记录。通常建议使用批量保存接口维护权限树。")
    @SaCheckPermission("system:role:update")
    public Result<Boolean> updateRoleMenu(@RequestBody @Validated UpdateRoleMenuReqDTO updateReqDTO) {
        roleMenuService.updateRoleMenu(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除角色菜单关联", description = "删除角色与某个菜单或按钮权限点的授权关系。")
    @SaCheckPermission("system:role:update")
    public Result<Boolean> deleteRoleMenu(@RequestBody @Validated RoleMenuIdReqDTO reqDTO) {
        roleMenuService.deleteRoleMenu(reqDTO.getRoleId(), reqDTO.getMenuId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取角色菜单关联", description = "获取角色与指定菜单的授权关系。")
    @SaCheckPermission("system:role:query")
    public Result<RoleMenuRespDTO> getRoleMenu(@RequestBody @Validated RoleMenuIdReqDTO reqDTO) {
        return Result.success(roleMenuService.getRoleMenu(reqDTO.getRoleId(), reqDTO.getMenuId()));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询角色菜单关联", description = "分页查询角色菜单授权关系，适用于审计和关联明细查看。")
    @SaCheckPermission("system:role:query")
    public Result<PageResult<RoleMenuRespDTO>> getRoleMenuPage(@RequestBody PageRoleMenuReqDTO pageRoleMenuReqDTO) {
        return Result.success(roleMenuService.getRoleMenuPage(pageRoleMenuReqDTO));
    }

    @PostMapping("/ids")
    @Operation(summary = "查询角色已授权菜单 ID", description = "根据角色 ID 返回已勾选的菜单和按钮权限点 ID 列表，用于前端权限树回显。")
    @SaCheckPermission("system:role:query")
    public Result<List<Long>> listRoleMenuIds(@RequestBody @Validated RoleMenuIdsReqDTO reqDTO) {
        return Result.success(roleMenuService.listMenuIdsByRoleId(reqDTO.getRoleId()));
    }

    @PostMapping("/ids/save")
    @Operation(summary = "保存角色菜单权限树", description = "一次性覆盖保存角色的菜单和按钮权限点授权关系，用于角色权限页的权限树保存。")
    @SaCheckPermission("system:role:update")
    public Result<Boolean> saveRoleMenuIds(@RequestBody @Validated RoleMenuIdsReqDTO reqDTO) {
        roleMenuService.saveRoleMenus(reqDTO.getRoleId(), reqDTO.getMenuIdList());
        return Result.success(true);
    }
}
