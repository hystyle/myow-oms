package com.myow.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.SystemModels.KickOnlineUserCommand;
import com.myow.system.application.dto.SystemModels.OnlineUserPageQuery;
import com.myow.system.application.service.SystemSupportService;
import com.myow.system.application.vo.SystemRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "System Online User", description = "Online user session APIs")
@RestController
@RequestMapping("/api/v1/system/online-users")
public class OnlineUserController {

    private final SystemSupportService service;

    public OnlineUserController(SystemSupportService service) {
        this.service = service;
    }

    @Operation(summary = "Page online users", description = "Returns online user sessions.")
    @PostMapping("/page")
    @SaCheckPermission("system:online-user:list")
    public Result<PageResult<SystemRecordVO>> page(@RequestBody OnlineUserPageQuery query) {
        return Result.success(service.onlineUsers(query));
    }

    @Operation(summary = "Kick online user", description = "Forces an online user token to logout.")
    @PostMapping("/kick")
    @SaCheckPermission("system:online-user:kick")
    public Result<Boolean> kick(@RequestBody KickOnlineUserCommand command) {
        return Result.success(service.kickOnlineUser(command.token()));
    }
}
