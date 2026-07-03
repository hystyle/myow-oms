package com.myow.customer.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.customer.application.dto.CustomerModels.IdCommand;
import com.myow.customer.application.dto.CustomerModels.RoleCreateCommand;
import com.myow.customer.application.dto.CustomerModels.RoleOptionQuery;
import com.myow.customer.application.dto.CustomerModels.RolePageQuery;
import com.myow.customer.application.dto.CustomerModels.RoleUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.RoleValidateCommand;
import com.myow.customer.application.service.CustomerService;
import com.myow.customer.application.vo.CustomerOptionVO;
import com.myow.customer.application.vo.CustomerRoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Customer Roles", description = "Customer business role tag management APIs.")
@RestController
@RequestMapping("/api/v1/customer/roles")
public class CustomerRoleController {

    private final CustomerService customerService;

    public CustomerRoleController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create customer role", description = "Adds a business role tag to a customer, such as supplier, overseas agent, carrier, or warehouse provider.")
    @PostMapping("/create")
    @SaCheckPermission("customer:role:create")
    public Result<CustomerRoleVO> create(@RequestBody RoleCreateCommand command) {
        return Result.success(customerService.createRole(command));
    }

    @Operation(summary = "Update customer role", description = "Updates status, role code, finance offset flag, and remark for a customer role tag.")
    @PostMapping("/update")
    @SaCheckPermission("customer:role:update")
    public Result<CustomerRoleVO> update(@RequestBody RoleUpdateCommand command) {
        return Result.success(customerService.updateRole(command));
    }

    @Operation(summary = "Page customer roles", description = "Returns customer business role tags by customer id.")
    @PostMapping("/page")
    @SaCheckPermission("customer:role:list")
    public Result<PageResult<CustomerRoleVO>> page(@RequestBody RolePageQuery query) {
        return Result.success(customerService.pageRoles(query));
    }

    @Operation(summary = "List customers by business role", description = "Returns active customer options that own the requested active business role, used by warehouse provider, carrier, and overseas agent selectors.")
    @PostMapping("/options-by-role")
    @SaCheckPermission("customer:role:list")
    public Result<List<CustomerOptionVO>> optionsByRole(@RequestBody RoleOptionQuery query) {
        return Result.success(customerService.roleOptions(query));
    }

    @Operation(summary = "Validate customer business role", description = "Checks whether the customer is active and owns the requested active business role before another module references it.")
    @PostMapping("/validate-role")
    @SaCheckPermission("customer:role:list")
    public Result<Boolean> validateRole(@RequestBody RoleValidateCommand command) {
        return Result.success(customerService.validateRole(command));
    }

    @Operation(summary = "Delete customer role", description = "Soft deletes a customer business role tag.")
    @PostMapping("/delete")
    @SaCheckPermission("customer:role:delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(customerService.deleteRole(command));
    }
}
