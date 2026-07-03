package com.myow.overseas.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.overseas.application.base.dto.OverseasBaseModels.IdCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.StatusCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.WarehouseCreateCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.WarehousePageQuery;
import com.myow.overseas.application.base.dto.OverseasBaseModels.WarehouseUpdateCommand;
import com.myow.overseas.application.base.service.OverseasBaseDataService;
import com.myow.overseas.application.base.vo.WarehouseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Overseas Physical Warehouse", description = "Physical warehouse base data APIs.")
@RestController
@RequestMapping("/api/v1/overseas/base/physical-warehouse")
public class PhysicalWarehouseController {

    private final OverseasBaseDataService service;

    public PhysicalWarehouseController(OverseasBaseDataService service) {
        this.service = service;
    }

    @Operation(summary = "Create physical warehouse", description = "Creates a physical warehouse and validates that serviceProviderCustomerId owns the WAREHOUSE_PROVIDER role.")
    @PostMapping("/create")
    @SaCheckPermission("overseas:base:physical-warehouse:manage")
    public Result<WarehouseVO> create(@RequestBody WarehouseCreateCommand command) {
        return Result.success(service.createWarehouse(command));
    }

    @Operation(summary = "Update physical warehouse", description = "Updates physical warehouse base information, provider customer, WMS mapping, address and contact data.")
    @PostMapping("/update")
    @SaCheckPermission("overseas:base:physical-warehouse:manage")
    public Result<WarehouseVO> update(@RequestBody WarehouseUpdateCommand command) {
        return Result.success(service.updateWarehouse(command));
    }

    @Operation(summary = "Get physical warehouse detail", description = "Returns physical warehouse detail by warehouse id.")
    @PostMapping("/detail")
    @SaCheckPermission("overseas:base:physical-warehouse:list")
    public Result<WarehouseVO> detail(@RequestBody IdCommand command) {
        return Result.success(service.warehouseDetail(command));
    }

    @Operation(summary = "Page physical warehouses", description = "Returns physical warehouse page data for base data management.")
    @PostMapping("/page")
    @SaCheckPermission("overseas:base:physical-warehouse:list")
    public Result<PageResult<WarehouseVO>> page(@RequestBody WarehousePageQuery query) {
        return Result.success(service.pageWarehouses(query));
    }

    @Operation(summary = "Change physical warehouse status", description = "Changes warehouse status. Enabling requires an active WAREHOUSE_PROVIDER role.")
    @PostMapping("/change-status")
    @SaCheckPermission("overseas:base:physical-warehouse:manage")
    public Result<Boolean> changeStatus(@RequestBody StatusCommand command) {
        return Result.success(service.changeWarehouseStatus(command));
    }

    @Operation(summary = "Delete physical warehouse", description = "Soft deletes a physical warehouse. Referenced data protection will be enforced after fulfillment modules are connected.")
    @PostMapping("/delete")
    @SaCheckPermission("overseas:base:physical-warehouse:manage")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(service.deleteWarehouse(command));
    }
}
