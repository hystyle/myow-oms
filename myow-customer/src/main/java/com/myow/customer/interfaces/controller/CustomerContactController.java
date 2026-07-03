package com.myow.customer.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.customer.application.dto.CustomerModels.ContactCreateCommand;
import com.myow.customer.application.dto.CustomerModels.ContactPageQuery;
import com.myow.customer.application.dto.CustomerModels.ContactUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.IdCommand;
import com.myow.customer.application.service.CustomerService;
import com.myow.customer.application.vo.CustomerContactVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer Contacts", description = "Customer contact matrix management APIs.")
@RestController
@RequestMapping("/api/v1/customer/contacts")
public class CustomerContactController {

    private final CustomerService customerService;

    public CustomerContactController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create customer contact", description = "Creates a contact for a customer and optionally marks it as the primary contact.")
    @PostMapping("/create")
    @SaCheckPermission("customer:contact:create")
    public Result<CustomerContactVO> create(@RequestBody ContactCreateCommand command) {
        return Result.success(customerService.createContact(command));
    }

    @Operation(summary = "Update customer contact", description = "Updates a customer contact and keeps at most one primary contact per customer.")
    @PostMapping("/update")
    @SaCheckPermission("customer:contact:update")
    public Result<CustomerContactVO> update(@RequestBody ContactUpdateCommand command) {
        return Result.success(customerService.updateContact(command));
    }

    @Operation(summary = "Page customer contacts", description = "Returns customer contact page data filtered by keyword.")
    @PostMapping("/page")
    @SaCheckPermission("customer:contact:list")
    public Result<PageResult<CustomerContactVO>> page(@RequestBody ContactPageQuery query) {
        return Result.success(customerService.pageContacts(query));
    }

    @Operation(summary = "Delete customer contact", description = "Soft deletes a customer contact.")
    @PostMapping("/delete")
    @SaCheckPermission("customer:contact:delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(customerService.deleteContact(command));
    }
}
