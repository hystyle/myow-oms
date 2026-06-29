package com.myow.user.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateTenantPlansReqDTO;
import com.myow.user.system.application.dto.IdReqDTO;
import com.myow.user.system.application.dto.PageTenantPlansReqDTO;
import com.myow.user.system.application.dto.TenantPlansRespDTO;
import com.myow.user.system.application.dto.UpdateTenantPlansReqDTO;
import com.myow.user.system.application.service.TenantPlansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping("/update")
    @Operation(summary = "更新租户套餐")
    public Result<Boolean> updateTenantPlans(@RequestBody @Validated UpdateTenantPlansReqDTO updateReqDTO) {
        tenantPlansService.updateTenantPlans(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除租户套餐")
    public Result<Boolean> deleteTenantPlans(@RequestBody @Validated IdReqDTO reqDTO) {
        tenantPlansService.deleteTenantPlans(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取租户套餐")
    public Result<TenantPlansRespDTO> getTenantPlans(@RequestBody @Validated IdReqDTO reqDTO) {
        return Result.success(tenantPlansService.getTenantPlans(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取租户套餐分页")
    public Result<PageResult<TenantPlansRespDTO>> getTenantPlansPage(@RequestBody PageTenantPlansReqDTO pageTenantPlansReqDTO) {
        return Result.success(tenantPlansService.getTenantPlansPage(pageTenantPlansReqDTO));
    }
}
