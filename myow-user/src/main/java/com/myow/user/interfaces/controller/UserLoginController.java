package com.myow.user.interfaces.controller;

import com.myow.common.constant.RequestHeaderConst;
import com.myow.common.response.Result;
import com.myow.user.application.dto.ChangePasswordReqDTO;
import com.myow.common.web.util.JakartaServletUtil;
import com.myow.user.application.dto.KickoutSessionReqDTO;
import com.myow.user.application.dto.UserLoginReqDTO;
import com.myow.user.application.service.ProfileService;
import com.myow.user.application.service.UserLoginService;
import com.myow.user.application.service.UserSessionService;
import com.myow.user.application.vo.UserSessionRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
@RequiredArgsConstructor
public class UserLoginController {

    private final UserLoginService userLoginService;
    private final ProfileService profileService;
    private final UserSessionService userSessionService;

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

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token")
    public Result<String> refresh() {
        return Result.success(userLoginService.refresh());
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change current user password")
    public Result<Boolean> changePassword(@RequestBody @Validated ChangePasswordReqDTO reqDTO) {
        profileService.changePassword(reqDTO);
        return Result.success(true);
    }

    @PostMapping("/session/page")
    @Operation(summary = "List current user sessions")
    public Result<List<UserSessionRespVO>> sessionPage() {
        return Result.success(userSessionService.listCurrentUserSessions());
    }

    @PostMapping("/session/kickout")
    @Operation(summary = "Kickout current user session")
    public Result<Boolean> kickoutSession(@RequestBody @Validated KickoutSessionReqDTO reqDTO) {
        userSessionService.kickoutCurrentUserSession(reqDTO.getToken());
        return Result.success(true);
    }
}
