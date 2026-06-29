package com.myow.user.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.application.dto.CreateUserReqDTO;
import com.myow.user.application.dto.PageUserReqDTO;
import com.myow.user.application.dto.UpdateUserReqDTO;
import com.myow.user.application.dto.UpdateUserStatusReqDTO;
import com.myow.user.application.dto.UserIdReqDTO;
import com.myow.user.application.service.UserManagementService;
import com.myow.user.application.vo.UserRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;

    @PostMapping("/create")
    @Operation(summary = "Create user")
    @SaCheckPermission("system:user:add")
    public Result<String> createUser(@RequestBody @Validated CreateUserReqDTO createReqDTO) {
        return Result.success(userManagementService.createUser(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "Update user")
    @SaCheckPermission("system:user:update")
    public Result<Boolean> updateUser(@RequestBody @Validated UpdateUserReqDTO updateReqDTO) {
        userManagementService.updateUser(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "Delete user")
    @SaCheckPermission("system:user:delete")
    public Result<Boolean> deleteUser(@RequestBody @Validated UserIdReqDTO reqDTO) {
        userManagementService.deleteUser(reqDTO.getUserId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "Get user")
    @SaCheckPermission("system:user:query")
    public Result<UserRespVO> getUser(@RequestBody @Validated UserIdReqDTO reqDTO) {
        return Result.success(userManagementService.getUser(reqDTO.getUserId()));
    }

    @PostMapping("/page")
    @Operation(summary = "Page users")
    @SaCheckPermission("system:user:query")
    public Result<PageResult<UserRespVO>> getUserPage(@RequestBody @Validated PageUserReqDTO pageUserReqDTO) {
        return Result.success(userManagementService.getUserPage(pageUserReqDTO));
    }

    @PostMapping("/status")
    @Operation(summary = "Update user status")
    @SaCheckPermission("system:user:update")
    public Result<Boolean> updateUserStatus(@RequestBody @Validated UpdateUserStatusReqDTO reqDTO) {
        userManagementService.updateUserStatus(reqDTO);
        return Result.success(true);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset user password")
    @SaCheckPermission("system:user:update")
    public Result<String> resetPassword(@RequestBody @Validated UserIdReqDTO reqDTO) {
        return Result.success(userManagementService.resetPassword(reqDTO.getUserId()));
    }

    @PostMapping("/unlock")
    @Operation(summary = "Unlock user")
    @SaCheckPermission("system:user:update")
    public Result<Boolean> unlockUser(@RequestBody @Validated UserIdReqDTO reqDTO) {
        userManagementService.unlockUser(reqDTO.getUserId());
        return Result.success(true);
    }

    @PostMapping("/force-change-password")
    @Operation(summary = "Force user to change password")
    @SaCheckPermission("system:user:update")
    public Result<Boolean> forceChangePassword(@RequestBody @Validated UserIdReqDTO reqDTO) {
        userManagementService.forceChangePassword(reqDTO.getUserId());
        return Result.success(true);
    }
}
