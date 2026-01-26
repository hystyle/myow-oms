package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateUserRoleReqDTO;
import com.myow.system.application.dto.PageUserRoleReqDTO;
import com.myow.system.application.dto.UpdateUserRoleReqDTO;
import com.myow.system.application.dto.UserRoleRespDTO;
import com.myow.system.application.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author gemini
 */
@Tag(name = "系统模块-用户角色关联")
@RestController
@RequestMapping("/system/user-role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping("/create")
    @Operation(summary = "创建用户角色关联")
    public Result<Boolean> createUserRole(@RequestBody CreateUserRoleReqDTO createReqDTO) {
        return Result.success(userRoleService.createUserRole(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户角色关联")
    public Result<Boolean> updateUserRole(@RequestBody UpdateUserRoleReqDTO updateReqDTO) {
        userRoleService.updateUserRole(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{userId}/{roleId}")
    @Operation(summary = "删除用户角色关联")
    public Result<Boolean> deleteUserRole(
            @PathVariable("userId") Long userId,
            @PathVariable("roleId") Long roleId) {
        userRoleService.deleteUserRole(userId, roleId);
        return Result.success(true);
    }

    @GetMapping("/get/{userId}/{roleId}")
    @Operation(summary = "获取用户角色关联")
    public Result<UserRoleRespDTO> getUserRole(
            @PathVariable("userId") Long userId,
            @PathVariable("roleId") Long roleId) {
        return Result.success(userRoleService.getUserRole(userId, roleId));
    }

    @GetMapping("/page")
    @Operation(summary = "获取用户角色关联分页")
    public Result<PageResult<UserRoleRespDTO>> getUserRolePage(PageUserRoleReqDTO pageUserRoleReqDTO) {
        return Result.success(userRoleService.getUserRolePage(pageUserRoleReqDTO));
    }
}
