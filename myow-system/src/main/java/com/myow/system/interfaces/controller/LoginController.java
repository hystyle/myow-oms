package com.myow.system.interfaces.controller;

import com.myow.common.constant.RequestHeaderConst;
import com.myow.common.response.Result;
import com.myow.common.util.JakartaServletUtil;
import com.myow.system.application.dto.LoginReqDTO;
import com.myow.system.application.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证授权接口
 *
 * @author yss
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证授权接口")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    @Operation(summary = "登录")
    public Result<?> login(@RequestBody @Validated LoginReqDTO loginRequest, HttpServletRequest request) {
        String ip = JakartaServletUtil.getClientIP(request);
        String userAgent = JakartaServletUtil.getHeaderIgnoreCase(request, RequestHeaderConst.USER_AGENT);
        return Result.success(loginService.login(loginRequest, ip, userAgent));
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    public Result<?> logout() {
        loginService.logout();
        return Result.success();
    }
}
