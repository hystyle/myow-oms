package com.myow.overseas.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.overseas.application.base.dto.OverseasBaseModels.IdCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsProductCreateCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsProductPageQuery;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsProductUpdateCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.StatusCommand;
import com.myow.overseas.application.base.service.OverseasBaseDataService;
import com.myow.overseas.application.base.vo.LogisticsProductVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Overseas Logistics Product", description = "Logistics product base data APIs.")
@RestController
@RequestMapping("/api/v1/overseas/base/logistics-product")
public class LogisticsProductController {

    private final OverseasBaseDataService service;

    public LogisticsProductController(OverseasBaseDataService service) {
        this.service = service;
    }

    @Operation(summary = "Create logistics product", description = "Creates a logistics product and validates that carrierCustomerId owns the CARRIER role.")
    @PostMapping("/create")
    @SaCheckPermission("overseas:base:logistics-product:manage")
    public Result<LogisticsProductVO> create(@RequestBody LogisticsProductCreateCommand command) {
        return Result.success(service.createProduct(command));
    }

    @Operation(summary = "Update logistics product", description = "Updates logistics product base information, carrier, product type and default routing strategy.")
    @PostMapping("/update")
    @SaCheckPermission("overseas:base:logistics-product:manage")
    public Result<LogisticsProductVO> update(@RequestBody LogisticsProductUpdateCommand command) {
        return Result.success(service.updateProduct(command));
    }

    @Operation(summary = "Get logistics product detail", description = "Returns logistics product detail by product id.")
    @PostMapping("/detail")
    @SaCheckPermission("overseas:base:logistics-product:list")
    public Result<LogisticsProductVO> detail(@RequestBody IdCommand command) {
        return Result.success(service.productDetail(command));
    }

    @Operation(summary = "Page logistics products", description = "Returns logistics product page data.")
    @PostMapping("/page")
    @SaCheckPermission("overseas:base:logistics-product:list")
    public Result<PageResult<LogisticsProductVO>> page(@RequestBody LogisticsProductPageQuery query) {
        return Result.success(service.pageProducts(query));
    }

    @Operation(summary = "Change logistics product status", description = "Changes logistics product status. Enabling requires an active CARRIER role.")
    @PostMapping("/change-status")
    @SaCheckPermission("overseas:base:logistics-product:manage")
    public Result<Boolean> changeStatus(@RequestBody StatusCommand command) {
        return Result.success(service.changeProductStatus(command));
    }

    @Operation(summary = "Delete logistics product", description = "Soft deletes a logistics product.")
    @PostMapping("/delete")
    @SaCheckPermission("overseas:base:logistics-product:manage")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(service.deleteProduct(command));
    }
}
