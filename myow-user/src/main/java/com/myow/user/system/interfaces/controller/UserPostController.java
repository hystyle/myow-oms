package com.myow.user.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateUserPostReqDTO;
import com.myow.user.system.application.dto.PageUserPostReqDTO;
import com.myow.user.system.application.dto.UpdateUserPostReqDTO;
import com.myow.user.system.application.dto.UserPostIdReqDTO;
import com.myow.user.system.application.dto.UserPostRespDTO;
import com.myow.user.system.application.service.UserPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-用户岗位关联")
@RestController
@RequestMapping("/system/user-post")
@RequiredArgsConstructor
public class UserPostController {

    private final UserPostService userPostService;

    @PostMapping("/create")
    @Operation(summary = "创建用户岗位关联")
    public Result<Boolean> createUserPost(@RequestBody CreateUserPostReqDTO createReqDTO) {
        return Result.success(userPostService.createUserPost(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新用户岗位关联")
    public Result<Boolean> updateUserPost(@RequestBody @Validated UpdateUserPostReqDTO updateReqDTO) {
        userPostService.updateUserPost(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除用户岗位关联")
    public Result<Boolean> deleteUserPost(@RequestBody @Validated UserPostIdReqDTO reqDTO) {
        userPostService.deleteUserPost(reqDTO.getUserId(), reqDTO.getPositionId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取用户岗位关联")
    public Result<UserPostRespDTO> getUserPost(@RequestBody @Validated UserPostIdReqDTO reqDTO) {
        return Result.success(userPostService.getUserPost(reqDTO.getUserId(), reqDTO.getPositionId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取用户岗位关联分页")
    public Result<PageResult<UserPostRespDTO>> getUserPostPage(@RequestBody PageUserPostReqDTO pageUserPostReqDTO) {
        return Result.success(userPostService.getUserPostPage(pageUserPostReqDTO));
    }
}
