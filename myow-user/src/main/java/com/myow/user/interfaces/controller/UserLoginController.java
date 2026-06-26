package com.myow.user.interfaces.controller;

import com.myow.common.constant.RequestHeaderConst;
import com.myow.common.response.Result;
import com.myow.common.web.util.JakartaServletUtil;
import com.myow.user.application.dto.UserLoginReqDTO;
import com.myow.user.application.service.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
@RequiredArgsConstructor
public class UserLoginController {

    private final UserLoginService userLoginService;

    @PostMapping("/login")
    @Operation(summary = "Login")
    public Result<?> login(@RequestBody @Validated UserLoginReqDTO loginRequest, HttpServletRequest request) {
        String ip = JakartaServletUtil.getClientIP(request);
        String userAgent = JakartaServletUtil.getHeaderIgnoreCase(request, RequestHeaderConst.USER_AGENT);
        return Result.success(userLoginService.login(loginRequest, ip, userAgent));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public Result<?> logout() {
        userLoginService.logout();
        return Result.success();
    }
}
