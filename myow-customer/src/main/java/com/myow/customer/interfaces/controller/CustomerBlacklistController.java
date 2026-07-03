package com.myow.customer.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.customer.application.dto.CustomerModels.BlacklistCheckCommand;
import com.myow.customer.application.dto.CustomerModels.BlacklistCreateCommand;
import com.myow.customer.application.dto.CustomerModels.BlacklistPageQuery;
import com.myow.customer.application.dto.CustomerModels.BlacklistUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.IdCommand;
import com.myow.customer.application.service.CustomerService;
import com.myow.customer.application.vo.CustomerBlacklistVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Customer Blacklist", description = "Customer blacklist management and interception check APIs.")
@RestController
@RequestMapping("/api/v1/customer/blacklists")
public class CustomerBlacklistController {

    private final CustomerService customerService;

    public CustomerBlacklistController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create blacklist item", description = "Creates a blacklist item for customer id, tax number, license number, phone, or email.")
    @PostMapping("/create")
    @SaCheckPermission("customer:blacklist:create")
    public Result<CustomerBlacklistVO> create(@RequestBody BlacklistCreateCommand command) {
        return Result.success(customerService.createBlacklist(command));
    }

    @Operation(summary = "Update blacklist item", description = "Updates blacklist target, risk level, reason, status, and effective time.")
    @PostMapping("/update")
    @SaCheckPermission("customer:blacklist:update")
    public Result<CustomerBlacklistVO> update(@RequestBody BlacklistUpdateCommand command) {
        return Result.success(customerService.updateBlacklist(command));
    }

    @Operation(summary = "Page blacklist items", description = "Returns blacklist page data filtered by keyword, target type, and status.")
    @PostMapping("/page")
    @SaCheckPermission("customer:blacklist:list")
    public Result<PageResult<CustomerBlacklistVO>> page(@RequestBody BlacklistPageQuery query) {
        return Result.success(customerService.pageBlacklists(query));
    }

    @Operation(summary = "Delete blacklist item", description = "Soft deletes a blacklist item.")
    @PostMapping("/delete")
    @SaCheckPermission("customer:blacklist:delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(customerService.deleteBlacklist(command));
    }

    @Operation(summary = "Check blacklist", description = "Checks whether customer id, tax number, license number, phone, or email hits active blacklist items.")
    @PostMapping("/check")
    @SaCheckPermission("customer:blacklist:check")
    public Result<List<CustomerBlacklistVO>> check(@RequestBody BlacklistCheckCommand command) {
        return Result.success(customerService.checkBlacklist(command));
    }
}
