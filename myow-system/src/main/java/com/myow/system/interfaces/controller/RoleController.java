package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateRoleReqDTO;
import com.myow.system.application.dto.PageRoleReqDTO;
import com.myow.system.application.dto.RoleRespDTO;
import com.myow.system.application.dto.UpdateRoleReqDTO;
import com.myow.system.application.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public Result<Long> createRole(@RequestBody CreateRoleReqDTO createReqDTO) {
        return Result.success(roleService.createRole(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新角色")
    public Result<Boolean> updateRole(@RequestBody UpdateRoleReqDTO updateReqDTO) {
        roleService.updateRole(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除角色")
    public Result<Boolean> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取角色")
    public Result<RoleRespDTO> getRole(@PathVariable("id") Long id) {
        return Result.success(roleService.getRole(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取角色分页")
    public Result<PageResult<RoleRespDTO>> getRolePage(PageRoleReqDTO pageRoleReqDTO) {
        return Result.success(roleService.getRolePage(pageRoleReqDTO));
    }
}
