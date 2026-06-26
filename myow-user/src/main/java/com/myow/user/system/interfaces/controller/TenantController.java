package com.myow.user.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateTenantReqDTO;
import com.myow.user.system.application.dto.PageTenantReqDTO;
import com.myow.user.system.application.dto.TenantRespDTO;
import com.myow.user.system.application.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-租户")
@RestController
@RequestMapping("/system/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("/register")
    @Operation(summary = "注册租户")
    public Result<String> register(@RequestBody CreateTenantReqDTO createReqDTO) {
        return Result.success(tenantService.createTenant(createReqDTO));
    }

    @PostMapping("/updateStatus/{id}")
    @Operation(summary = "更新租户状态")
    public Result<Boolean> updateTenantStatus(@PathVariable("id") Long id) {
        return Result.success(tenantService.updateTenantStatus(id));
    }

    @PostMapping("/page")
    @Operation(summary = "获取租户分页")
    public Result<PageResult<TenantRespDTO>> getTenantPage(@Valid @RequestBody PageTenantReqDTO pageTenantReqDTO) {
        return Result.success(tenantService.getTenantPage(pageTenantReqDTO));
    }
}
