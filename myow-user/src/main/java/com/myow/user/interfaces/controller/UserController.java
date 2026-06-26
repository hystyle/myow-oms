package com.myow.user.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.application.dto.CreateUserReqDTO;
import com.myow.user.application.dto.PageUserReqDTO;
import com.myow.user.application.dto.UpdateUserReqDTO;
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
    public Result<String> createUser(@RequestBody @Validated CreateUserReqDTO createReqDTO) {
        return Result.success(userManagementService.createUser(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "Update user")
    public Result<Boolean> updateUser(@RequestBody @Validated UpdateUserReqDTO updateReqDTO) {
        userManagementService.updateUser(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "Delete user")
    public Result<Boolean> deleteUser(Long id) {
        userManagementService.deleteUser(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get user")
    public Result<UserRespVO> getUser(@PathVariable("id") Long id) {
        return Result.success(userManagementService.getUser(id));
    }

    @PostMapping("/page")
    @Operation(summary = "Page users")
    public Result<PageResult<UserRespVO>> getUserPage(@RequestBody PageUserReqDTO pageUserReqDTO) {
        return Result.success(userManagementService.getUserPage(pageUserReqDTO));
    }
}
