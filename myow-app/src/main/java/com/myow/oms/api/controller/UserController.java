package com.myow.oms.api.controller;

import com.myow.infrastructure.persistence.entity.UserDO;
import com.myow.infrastructure.persistence.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: yss
 * @date: 2026-01-19 23:10
 * @description:
 */
@Tag(name = "用户管理", description = "系统用户相关接口")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "获取用户列表", description = "查询所有用户（示例接口）")
    @GetMapping("/list")
    public List<UserDO> listUsers() {
        return userService.list();
    }
}