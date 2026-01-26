package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateTenantReqDTO;
import com.myow.system.application.dto.PageTenantReqDTO;
import com.myow.system.application.dto.TenantRespDTO;
import com.myow.system.application.dto.UpdateTenantReqDTO;
import com.myow.system.application.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author gemini
 */
@Tag(name = "系统模块-租户")
@RestController
@RequestMapping("/system/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("/create")
    @Operation(summary = "创建租户")
    public Result<Long> createTenant(@RequestBody CreateTenantReqDTO createReqDTO) {
        return Result.success(tenantService.createTenant(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新租户")
    public Result<Boolean> updateTenant(@RequestBody UpdateTenantReqDTO updateReqDTO) {
        tenantService.updateTenant(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除租户")
    public Result<Boolean> deleteTenant(@PathVariable("id") Long id) {
        tenantService.deleteTenant(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取租户")
    public Result<TenantRespDTO> getTenant(@PathVariable("id") Long id) {
        return Result.success(tenantService.getTenant(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取租户分页")
    public Result<PageResult<TenantRespDTO>> getTenantPage(PageTenantReqDTO pageTenantReqDTO) {
        return Result.success(tenantService.getTenantPage(pageTenantReqDTO));
    }
}
