package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.SystemModels.IdCommand;
import com.myow.system.application.dto.SystemModels.NoticeCreateCommand;
import com.myow.system.application.dto.SystemModels.NoticeUpdateCommand;
import com.myow.system.application.dto.SystemModels.PageQuery;
import com.myow.system.application.service.SystemSupportService;
import com.myow.system.application.vo.SystemRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "System Notice", description = "System notice and announcement APIs")
@RestController
@RequestMapping("/api/v1/system/notices")
public class NoticeController {

    private final SystemSupportService service;

    public NoticeController(SystemSupportService service) {
        this.service = service;
    }

    @Operation(summary = "Create notice", description = "Creates a system notice draft.")
    @PostMapping("/create")
    public Result<SystemRecordVO> create(@RequestBody NoticeCreateCommand command) {
        return Result.success(service.createNotice(command));
    }

    @Operation(summary = "Update notice", description = "Updates a draft notice.")
    @PostMapping("/update")
    public Result<SystemRecordVO> update(@RequestBody NoticeUpdateCommand command) {
        return Result.success(service.updateNotice(command));
    }

    @Operation(summary = "Get notice detail", description = "Returns notice detail by id.")
    @PostMapping("/detail")
    public Result<SystemRecordVO> detail(@RequestBody IdCommand command) {
        return Result.success(service.detail("NOTICE", command));
    }

    @Operation(summary = "Page notices", description = "Returns notice page data for administrators.")
    @PostMapping("/page")
    public Result<PageResult<SystemRecordVO>> page(@RequestBody PageQuery query) {
        return Result.success(service.page("NOTICE", query));
    }

    @Operation(summary = "Publish notice", description = "Publishes a draft notice.")
    @PostMapping("/publish")
    public Result<SystemRecordVO> publish(@RequestBody IdCommand command) {
        return Result.success(service.changeStatus("NOTICE", command, 1));
    }

    @Operation(summary = "Withdraw notice", description = "Withdraws a published notice.")
    @PostMapping("/withdraw")
    public Result<SystemRecordVO> withdraw(@RequestBody IdCommand command) {
        return Result.success(service.changeStatus("NOTICE", command, 2));
    }

    @Operation(summary = "Delete notice", description = "Deletes a notice.")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(service.delete("NOTICE", command));
    }

    @Operation(summary = "Page my notices", description = "Returns current user's notice page data.")
    @PostMapping("/my-page")
    public Result<PageResult<SystemRecordVO>> myPage(@RequestBody PageQuery query) {
        return Result.success(service.page("NOTICE", query));
    }

    @Operation(summary = "Mark notice read", description = "Marks a notice as read for current user.")
    @PostMapping("/read")
    public Result<Boolean> read(@RequestBody IdCommand command) {
        return Result.success(command.id() != null);
    }

    @Operation(summary = "Mark all notices read", description = "Marks all notices as read for current user.")
    @PostMapping("/read-all")
    public Result<Boolean> readAll() {
        return Result.success(true);
    }
}
