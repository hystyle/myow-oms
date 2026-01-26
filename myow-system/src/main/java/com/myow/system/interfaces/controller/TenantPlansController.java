package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateTenantPlansReqDTO;
import com.myow.system.application.dto.PageTenantPlansReqDTO;
import com.myow.system.application.dto.TenantPlansRespDTO;
import com.myow.system.application.dto.UpdateTenantPlansReqDTO;
import com.myow.system.application.service.TenantPlansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-租户套餐")
@RestController
@RequestMapping("/system/tenant-plans")
@RequiredArgsConstructor
public class TenantPlansController {

    private final TenantPlansService tenantPlansService;

    @PostMapping("/create")
    @Operation(summary = "创建租户套餐")
    public Result<Long> createTenantPlans(@RequestBody CreateTenantPlansReqDTO createReqDTO) {
        return Result.success(tenantPlansService.createTenantPlans(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新租户套餐")
    public Result<Boolean> updateTenantPlans(@RequestBody UpdateTenantPlansReqDTO updateReqDTO) {
        tenantPlansService.updateTenantPlans(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除租户套餐")
    public Result<Boolean> deleteTenantPlans(@PathVariable("id") Long id) {
        tenantPlansService.deleteTenantPlans(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取租户套餐")
    public Result<TenantPlansRespDTO> getTenantPlans(@PathVariable("id") Long id) {
        return Result.success(tenantPlansService.getTenantPlans(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取租户套餐分页")
    public Result<PageResult<TenantPlansRespDTO>> getTenantPlansPage(PageTenantPlansReqDTO pageTenantPlansReqDTO) {
        return Result.success(tenantPlansService.getTenantPlansPage(pageTenantPlansReqDTO));
    }
}
