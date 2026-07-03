package com.myow.customer.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.customer.application.dto.CustomerModels.IdCommand;
import com.myow.customer.application.dto.CustomerModels.RelationCreateCommand;
import com.myow.customer.application.dto.CustomerModels.RelationPageQuery;
import com.myow.customer.application.dto.CustomerModels.RelationUpdateCommand;
import com.myow.customer.application.service.CustomerService;
import com.myow.customer.application.vo.CustomerRelationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer Relations", description = "Customer parent-child, billing-title, and settlement-subject relation APIs.")
@RestController
@RequestMapping("/api/v1/customer/relations")
public class CustomerRelationController {

    private final CustomerService customerService;

    public CustomerRelationController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create customer relation", description = "Creates a relation between two customers, such as parent-child, billing title, or settlement subject.")
    @PostMapping("/create")
    @SaCheckPermission("customer:relation:create")
    public Result<CustomerRelationVO> create(@RequestBody RelationCreateCommand command) {
        return Result.success(customerService.createRelation(command));
    }

    @Operation(summary = "Update customer relation", description = "Updates relation status, independent settlement flag, and remark.")
    @PostMapping("/update")
    @SaCheckPermission("customer:relation:update")
    public Result<CustomerRelationVO> update(@RequestBody RelationUpdateCommand command) {
        return Result.success(customerService.updateRelation(command));
    }

    @Operation(summary = "Page customer relations", description = "Returns relations where the specified customer is parent or child.")
    @PostMapping("/page")
    @SaCheckPermission("customer:relation:list")
    public Result<PageResult<CustomerRelationVO>> page(@RequestBody RelationPageQuery query) {
        return Result.success(customerService.pageRelations(query));
    }

    @Operation(summary = "Delete customer relation", description = "Soft deletes a customer relation.")
    @PostMapping("/delete")
    @SaCheckPermission("customer:relation:delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(customerService.deleteRelation(command));
    }
}
