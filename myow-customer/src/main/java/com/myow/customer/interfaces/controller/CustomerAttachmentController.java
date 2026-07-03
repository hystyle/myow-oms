package com.myow.customer.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.customer.application.dto.CustomerModels.AttachmentCreateCommand;
import com.myow.customer.application.dto.CustomerModels.AttachmentPageQuery;
import com.myow.customer.application.dto.CustomerModels.AttachmentUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.IdCommand;
import com.myow.customer.application.service.CustomerService;
import com.myow.customer.application.vo.CustomerAttachmentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer Attachments", description = "Customer attachment index APIs. File binary is managed by system file center.")
@RestController
@RequestMapping("/api/v1/customer/attachments")
public class CustomerAttachmentController {

    private final CustomerService customerService;

    public CustomerAttachmentController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create customer attachment", description = "Creates a customer attachment index with file id from system file center.")
    @PostMapping("/create")
    @SaCheckPermission("customer:attachment:create")
    public Result<CustomerAttachmentVO> create(@RequestBody AttachmentCreateCommand command) {
        return Result.success(customerService.createAttachment(command));
    }

    @Operation(summary = "Update customer attachment", description = "Updates customer attachment type, file id, file name, expire date, audit status, and remark.")
    @PostMapping("/update")
    @SaCheckPermission("customer:attachment:update")
    public Result<CustomerAttachmentVO> update(@RequestBody AttachmentUpdateCommand command) {
        return Result.success(customerService.updateAttachment(command));
    }

    @Operation(summary = "Page customer attachments", description = "Returns customer attachment index page data.")
    @PostMapping("/page")
    @SaCheckPermission("customer:attachment:list")
    public Result<PageResult<CustomerAttachmentVO>> page(@RequestBody AttachmentPageQuery query) {
        return Result.success(customerService.pageAttachments(query));
    }

    @Operation(summary = "Delete customer attachment", description = "Soft deletes a customer attachment index.")
    @PostMapping("/delete")
    @SaCheckPermission("customer:attachment:delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(customerService.deleteAttachment(command));
    }
}
