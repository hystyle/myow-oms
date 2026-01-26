package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateUserReqDTO;
import com.myow.system.application.dto.PageUserReqDTO;
import com.myow.system.application.dto.UpdateUserReqDTO;
import com.myow.system.application.dto.UserRespDTO;
import com.myow.system.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-用户")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    @Operation(summary = "创建用户")
    public Result<Long> createUser(@RequestBody CreateUserReqDTO createReqDTO) {
        return Result.success(userService.createUser(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新用户")
    public Result<Boolean> updateUser(@RequestBody UpdateUserReqDTO updateReqDTO) {
        userService.updateUser(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除用户")
    public Result<Boolean> deleteUser(Long id) {
        userService.deleteUser(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取用户")
    public Result<UserRespDTO> getUser(@PathVariable("id") Long id) {
        return Result.success(userService.getUser(id));
    }

    @PostMapping("/page")
    @Operation(summary = "获取用户分页")
    public Result<PageResult<UserRespDTO>> getUserPage(@RequestBody PageUserReqDTO pageUserReqDTO) {
        return Result.success(userService.getUserPage(pageUserReqDTO));
    }
}
