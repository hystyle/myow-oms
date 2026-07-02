package com.myow.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.Result;
import com.myow.system.application.dto.SystemModels.EmptyCommand;
import com.myow.system.application.service.SystemSupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "System Monitor", description = "Server, Redis and database monitoring APIs")
@RestController
@RequestMapping("/api/v1/system/monitor")
public class MonitorController {

    private final SystemSupportService service;

    public MonitorController(SystemSupportService service) {
        this.service = service;
    }

    @Operation(summary = "Get server metrics", description = "Returns JVM and runtime metrics.")
    @PostMapping("/server")
    @SaCheckPermission("system:monitor:view")
    public Result<Map<String, Object>> server(@RequestBody(required = false) EmptyCommand command) {
        return Result.success(service.serverMetrics());
    }

    @Operation(summary = "Get Redis metrics", description = "Returns Redis metrics when Redis adapter is connected.")
    @PostMapping("/redis")
    @SaCheckPermission("system:monitor:view")
    public Result<Map<String, Object>> redis(@RequestBody(required = false) EmptyCommand command) {
        return Result.success(service.redisMetrics());
    }

    @Operation(summary = "Get database metrics", description = "Returns database connection metrics when datasource adapter is connected.")
    @PostMapping("/db")
    @SaCheckPermission("system:monitor:view")
    public Result<Map<String, Object>> db(@RequestBody(required = false) EmptyCommand command) {
        return Result.success(service.dbMetrics());
    }
}
