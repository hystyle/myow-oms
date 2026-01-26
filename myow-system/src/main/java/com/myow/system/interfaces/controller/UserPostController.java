package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateUserPostReqDTO;
import com.myow.system.application.dto.PageUserPostReqDTO;
import com.myow.system.application.dto.UpdateUserPostReqDTO;
import com.myow.system.application.dto.UserPostRespDTO;
import com.myow.system.application.service.UserPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @PutMapping("/update")
    @Operation(summary = "更新用户岗位关联")
    public Result<Boolean> updateUserPost(@RequestBody UpdateUserPostReqDTO updateReqDTO) {
        userPostService.updateUserPost(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{userId}/{postId}")
    @Operation(summary = "删除用户岗位关联")
    public Result<Boolean> deleteUserPost(
            @PathVariable("userId") Long userId,
            @PathVariable("postId") Long postId) {
        userPostService.deleteUserPost(userId, postId);
        return Result.success(true);
    }

    @GetMapping("/get/{userId}/{postId}")
    @Operation(summary = "获取用户岗位关联")
    public Result<UserPostRespDTO> getUserPost(
            @PathVariable("userId") Long userId,
            @PathVariable("postId") Long postId) {
        return Result.success(userPostService.getUserPost(userId, postId));
    }

    @GetMapping("/page")
    @Operation(summary = "获取用户岗位关联分页")
    public Result<PageResult<UserPostRespDTO>> getUserPostPage(PageUserPostReqDTO pageUserPostReqDTO) {
        return Result.success(userPostService.getUserPostPage(pageUserPostReqDTO));
    }
}
