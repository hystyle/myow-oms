package com.myow.customer.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.customer.application.dto.CustomerModels.IdCommand;
import com.myow.customer.application.dto.CustomerModels.KycAuditCommand;
import com.myow.customer.application.dto.CustomerModels.KycCreateCommand;
import com.myow.customer.application.dto.CustomerModels.KycPageQuery;
import com.myow.customer.application.dto.CustomerModels.KycUpdateCommand;
import com.myow.customer.application.service.CustomerService;
import com.myow.customer.application.vo.CustomerKycVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer KYC", description = "Customer KYC record and audit APIs.")
@RestController
@RequestMapping("/api/v1/customer/kycs")
public class CustomerKycController {

    private final CustomerService customerService;

    public CustomerKycController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create customer KYC", description = "Creates a pending customer KYC audit record.")
    @PostMapping("/create")
    @SaCheckPermission("customer:kyc:create")
    public Result<CustomerKycVO> create(@RequestBody KycCreateCommand command) {
        return Result.success(customerService.createKyc(command));
    }

    @Operation(summary = "Update customer KYC", description = "Updates KYC type and remark before or after audit.")
    @PostMapping("/update")
    @SaCheckPermission("customer:kyc:update")
    public Result<CustomerKycVO> update(@RequestBody KycUpdateCommand command) {
        return Result.success(customerService.updateKyc(command));
    }

    @Operation(summary = "Audit customer KYC", description = "Approves or rejects a customer KYC record.")
    @PostMapping("/audit")
    @SaCheckPermission("customer:kyc:audit")
    public Result<CustomerKycVO> audit(@RequestBody KycAuditCommand command) {
        return Result.success(customerService.auditKyc(command));
    }

    @Operation(summary = "Page customer KYC records", description = "Returns customer KYC page data.")
    @PostMapping("/page")
    @SaCheckPermission("customer:kyc:list")
    public Result<PageResult<CustomerKycVO>> page(@RequestBody KycPageQuery query) {
        return Result.success(customerService.pageKycs(query));
    }

    @Operation(summary = "Delete customer KYC", description = "Soft deletes a customer KYC record.")
    @PostMapping("/delete")
    @SaCheckPermission("customer:kyc:delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(customerService.deleteKyc(command));
    }
}
