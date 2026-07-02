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

@Tag(name = "用户中心-内部用户", description = "内部员工账号、状态、密码和登录安全管理接口")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;

    @PostMapping("/create")
    @Operation(summary = "创建内部用户", description = "创建内部员工账号，生成初始随机密码并强制首次登录修改密码。用于管理端用户管理页面的新增用户抽屉。")
    @SaCheckPermission("system:user:add")
    public Result<String> createUser(@RequestBody @Validated CreateUserReqDTO createReqDTO) {
        return Result.success(userManagementService.createUser(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新内部用户", description = "更新内部员工的姓名、部门、岗位、联系方式、角色等资料；保存后清理该用户登录缓存。")
    @SaCheckPermission("system:user:update")
    public Result<Boolean> updateUser(@RequestBody @Validated UpdateUserReqDTO updateReqDTO) {
        userManagementService.updateUser(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除内部用户", description = "删除内部员工账号并解除角色关系；删除后该用户无法继续登录。")
    @SaCheckPermission("system:user:delete")
    public Result<Boolean> deleteUser(@RequestBody @Validated UserIdReqDTO reqDTO) {
        userManagementService.deleteUser(reqDTO.getUserId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取内部用户详情", description = "根据用户 ID 获取用户详情，包含部门、岗位、角色、账号安全状态等字段。")
    @SaCheckPermission("system:user:query")
    public Result<UserRespVO> getUser(@RequestBody @Validated UserIdReqDTO reqDTO) {
        return Result.success(userManagementService.getUser(reqDTO.getUserId()));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询内部用户", description = "按关键词、部门、状态分页查询内部员工账号。传入部门 ID 时会包含该部门及其子部门用户。")
    @SaCheckPermission("system:user:query")
    public Result<PageResult<UserRespVO>> getUserPage(@RequestBody @Validated PageUserReqDTO pageUserReqDTO) {
        return Result.success(userManagementService.getUserPage(pageUserReqDTO));
    }

    @PostMapping("/status")
    @Operation(summary = "启用或停用内部用户", description = "更新用户启停状态；停用后清理登录缓存，用户无法继续使用管理端。")
    @SaCheckPermission("system:user:update")
    public Result<Boolean> updateUserStatus(@RequestBody @Validated UpdateUserStatusReqDTO reqDTO) {
        userManagementService.updateUserStatus(reqDTO);
        return Result.success(true);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "重置内部用户密码", description = "生成新的随机密码、清空锁定状态并强制用户下次登录修改密码。")
    @SaCheckPermission("system:user:update")
    public Result<String> resetPassword(@RequestBody @Validated UserIdReqDTO reqDTO) {
        return Result.success(userManagementService.resetPassword(reqDTO.getUserId()));
    }

    @PostMapping("/unlock")
    @Operation(summary = "解锁内部用户", description = "清空用户失败登录次数和锁定截止时间，用于账号被登录策略锁定后的人工解锁。")
    @SaCheckPermission("system:user:update")
    public Result<Boolean> unlockUser(@RequestBody @Validated UserIdReqDTO reqDTO) {
        userManagementService.unlockUser(reqDTO.getUserId());
        return Result.success(true);
    }

    @PostMapping("/force-change-password")
    @Operation(summary = "强制内部用户修改密码", description = "设置用户下次登录必须修改密码，用于安全策略或管理员人工要求。")
    @SaCheckPermission("system:user:update")
    public Result<Boolean> forceChangePassword(@RequestBody @Validated UserIdReqDTO reqDTO) {
        userManagementService.forceChangePassword(reqDTO.getUserId());
        return Result.success(true);
    }
}
