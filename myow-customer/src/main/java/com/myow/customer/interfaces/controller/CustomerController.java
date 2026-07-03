package com.myow.customer.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.customer.application.dto.CustomerModels.CustomerCreateCommand;
import com.myow.customer.application.dto.CustomerModels.CustomerPageQuery;
import com.myow.customer.application.dto.CustomerModels.CustomerStatusCommand;
import com.myow.customer.application.dto.CustomerModels.CustomerUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.IdCommand;
import com.myow.customer.application.service.CustomerService;
import com.myow.customer.application.vo.CustomerVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer Master Data", description = "Customer profile CRUD and status management APIs.")
@RestController
@RequestMapping("/api/v1/customer/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create customer", description = "Creates a customer master record with basic settlement and owner fields.")
    @PostMapping("/create")
    @SaCheckPermission("customer:customer:create")
    public Result<CustomerVO> create(@RequestBody CustomerCreateCommand command) {
        return Result.success(customerService.create(command));
    }

    @Operation(summary = "Update customer", description = "Updates basic customer profile, settlement, owner, pool, and remark fields.")
    @PostMapping("/update")
    @SaCheckPermission("customer:customer:update")
    public Result<CustomerVO> update(@RequestBody CustomerUpdateCommand command) {
        return Result.success(customerService.update(command));
    }

    @Operation(summary = "Change customer status", description = "Changes customer lifecycle status, such as activating or suspending a customer.")
    @PostMapping("/change-status")
    @SaCheckPermission("customer:customer:update")
    public Result<Boolean> changeStatus(@RequestBody CustomerStatusCommand command) {
        return Result.success(customerService.changeStatus(command));
    }

    @Operation(summary = "Get customer detail", description = "Returns customer master data detail by customer id.")
    @PostMapping("/detail")
    @SaCheckPermission("customer:customer:list")
    public Result<CustomerVO> detail(@RequestBody IdCommand command) {
        return Result.success(customerService.detail(command));
    }

    @Operation(summary = "Page customers", description = "Returns customer page data filtered by keyword, status, owner, and pool status.")
    @PostMapping("/page")
    @SaCheckPermission("customer:customer:list")
    public Result<PageResult<CustomerVO>> page(@RequestBody CustomerPageQuery query) {
        return Result.success(customerService.page(query));
    }

    @Operation(summary = "Delete customer", description = "Soft deletes a customer master record.")
    @PostMapping("/delete")
    @SaCheckPermission("customer:customer:delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(customerService.delete(command));
    }
}
