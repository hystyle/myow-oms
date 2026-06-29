package com.myow.user.interfaces.controller;

import com.myow.common.response.Result;
import com.myow.user.application.dto.ChangePasswordReqDTO;
import com.myow.user.application.dto.UpdateProfileReqDTO;
import com.myow.user.application.service.ProfileService;
import com.myow.user.application.vo.UserMenuRespVO;
import com.myow.user.application.vo.UserRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profile")
@Tag(name = "Current user profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/current")
    @Operation(summary = "Get current user")
    public Result<UserRespVO> current() {
        return Result.success(profileService.getCurrentUser());
    }

    @PostMapping("/menus")
    @Operation(summary = "Get current user menus")
    public Result<List<UserMenuRespVO>> menus() {
        return Result.success(profileService.getCurrentMenus());
    }

    @PostMapping("/permissions")
    @Operation(summary = "Get current user permissions")
    public Result<List<String>> permissions() {
        return Result.success(profileService.getCurrentPermissions());
    }

    @PostMapping("/update")
    @Operation(summary = "Update current user profile")
    public Result<Boolean> update(@RequestBody @Valid UpdateProfileReqDTO reqDTO) {
        profileService.updateProfile(reqDTO);
        return Result.success(true);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change current user password")
    public Result<Boolean> changePassword(@RequestBody @Valid ChangePasswordReqDTO reqDTO) {
        profileService.changePassword(reqDTO);
        return Result.success(true);
    }
}
