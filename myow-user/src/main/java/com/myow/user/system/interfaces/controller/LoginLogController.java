package com.myow.user.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateLoginLogReqDTO;
import com.myow.user.system.application.dto.LoginLogRespDTO;
import com.myow.user.system.application.dto.PageLoginLogReqDTO;
import com.myow.user.system.application.service.LoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "系统模块-登录日志", description = "记录和查询用户登录行为，包括登录账号、客户端、IP、状态和失败原因。")
@RestController
@RequestMapping("/system/login-log")
@RequiredArgsConstructor
public class LoginLogController {

    private final LoginLogService loginLogService;

    @PostMapping("/create")
    @Operation(summary = "创建登录日志", description = "写入一条登录日志记录。通常由认证流程内部调用，管理端页面不直接创建登录日志。")
    @SaCheckPermission("system:login-log:query")
    public Result<Long> createLoginLog(@RequestBody CreateLoginLogReqDTO createReqDTO) {
        return Result.success(loginLogService.createLoginLog(createReqDTO));
    }

    @PostMapping("/get/{id}")
    @Operation(summary = "获取登录日志详情", description = "按登录日志 ID 获取单次登录行为详情，用于安全排查和审计查看。")
    @SaCheckPermission("system:login-log:query")
    public Result<LoginLogRespDTO> getLoginLog(@PathVariable("id") Long id) {
        return Result.success(loginLogService.getLoginLog(id));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询登录日志", description = "按租户、用户、登录账号、状态和登录时间范围分页查询登录日志。")
    @SaCheckPermission("system:login-log:query")
    public Result<PageResult<LoginLogRespDTO>> getLoginLogPage(@RequestBody PageLoginLogReqDTO pageLoginLogReqDTO) {
        return Result.success(loginLogService.getLoginLogPage(pageLoginLogReqDTO));
    }
}
