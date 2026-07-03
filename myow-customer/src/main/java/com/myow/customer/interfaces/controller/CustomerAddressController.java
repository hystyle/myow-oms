package com.myow.customer.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.customer.application.dto.CustomerModels.AddressCreateCommand;
import com.myow.customer.application.dto.CustomerModels.AddressPageQuery;
import com.myow.customer.application.dto.CustomerModels.AddressUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.IdCommand;
import com.myow.customer.application.service.CustomerService;
import com.myow.customer.application.vo.CustomerAddressVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer Addresses", description = "Customer address book management APIs.")
@RestController
@RequestMapping("/api/v1/customer/addresses")
public class CustomerAddressController {

    private final CustomerService customerService;

    public CustomerAddressController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create customer address", description = "Creates a registered, billing, shipping, return, or other customer address.")
    @PostMapping("/create")
    @SaCheckPermission("customer:address:create")
    public Result<CustomerAddressVO> create(@RequestBody AddressCreateCommand command) {
        return Result.success(customerService.createAddress(command));
    }

    @Operation(summary = "Update customer address", description = "Updates a customer address and keeps at most one default address per customer and address type.")
    @PostMapping("/update")
    @SaCheckPermission("customer:address:update")
    public Result<CustomerAddressVO> update(@RequestBody AddressUpdateCommand command) {
        return Result.success(customerService.updateAddress(command));
    }

    @Operation(summary = "Page customer addresses", description = "Returns customer address page data filtered by customer and address type.")
    @PostMapping("/page")
    @SaCheckPermission("customer:address:list")
    public Result<PageResult<CustomerAddressVO>> page(@RequestBody AddressPageQuery query) {
        return Result.success(customerService.pageAddresses(query));
    }

    @Operation(summary = "Delete customer address", description = "Soft deletes a customer address.")
    @PostMapping("/delete")
    @SaCheckPermission("customer:address:delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(customerService.deleteAddress(command));
    }
}
