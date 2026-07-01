package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.SystemModels.ExportTaskCreateCommand;
import com.myow.system.application.dto.SystemModels.IdCommand;
import com.myow.system.application.dto.SystemModels.PageQuery;
import com.myow.system.application.service.SystemSupportService;
import com.myow.system.application.vo.SystemRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "System Export Task", description = "Asynchronous export task APIs")
@RestController
@RequestMapping("/api/v1/system/export-tasks")
public class ExportTaskController {

    private final SystemSupportService service;

    public ExportTaskController(SystemSupportService service) {
        this.service = service;
    }

    @Operation(summary = "Create export task", description = "Creates an asynchronous export task.")
    @PostMapping("/create")
    public Result<SystemRecordVO> create(@RequestBody ExportTaskCreateCommand command) {
        return Result.success(service.createExportTask(command));
    }

    @Operation(summary = "Get export task detail", description = "Returns export task status and metadata.")
    @PostMapping("/detail")
    public Result<SystemRecordVO> detail(@RequestBody IdCommand command) {
        return Result.success(service.detail("EXPORT_TASK", command));
    }

    @Operation(summary = "Page my export tasks", description = "Returns current user's export task page data.")
    @PostMapping("/my-page")
    public Result<PageResult<SystemRecordVO>> myPage(@RequestBody PageQuery query) {
        return Result.success(service.page("EXPORT_TASK", query));
    }

    @Operation(summary = "Download export file", description = "Returns export task metadata for file download.")
    @PostMapping("/download")
    public Result<SystemRecordVO> download(@RequestBody IdCommand command) {
        return Result.success(service.detail("EXPORT_TASK", command));
    }

    @Operation(summary = "Delete export task", description = "Deletes an export task and schedules generated file cleanup.")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(service.delete("EXPORT_TASK", command));
    }
}
