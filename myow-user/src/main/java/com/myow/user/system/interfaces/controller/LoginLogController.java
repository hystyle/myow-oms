package com.myow.user.system.interfaces.controller;

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

@Tag(name = "Login Log")
@RestController
@RequestMapping("/system/login-log")
@RequiredArgsConstructor
public class LoginLogController {

    private final LoginLogService loginLogService;

    @PostMapping("/create")
    @Operation(summary = "Create login log")
    public Result<Long> createLoginLog(@RequestBody CreateLoginLogReqDTO createReqDTO) {
        return Result.success(loginLogService.createLoginLog(createReqDTO));
    }

    @PostMapping("/get/{id}")
    @Operation(summary = "Get login log")
    public Result<LoginLogRespDTO> getLoginLog(@PathVariable("id") Long id) {
        return Result.success(loginLogService.getLoginLog(id));
    }

    @PostMapping("/page")
    @Operation(summary = "Page login logs")
    public Result<PageResult<LoginLogRespDTO>> getLoginLogPage(@RequestBody PageLoginLogReqDTO pageLoginLogReqDTO) {
        return Result.success(loginLogService.getLoginLogPage(pageLoginLogReqDTO));
    }
}
