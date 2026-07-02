package com.myow.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.SystemModels.ExportTaskCreateCommand;
import com.myow.system.application.dto.SystemModels.IdCommand;
import com.myow.system.application.dto.SystemModels.PageQuery;
import com.myow.system.application.service.SystemSupportService;
import com.myow.system.application.vo.SystemDownloadFile;
import com.myow.system.application.vo.SystemRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
    @SaCheckPermission("system:export-task:create")
    public Result<SystemRecordVO> create(@RequestBody ExportTaskCreateCommand command) {
        return Result.success(service.createExportTask(command));
    }

    @Operation(summary = "Get export task detail", description = "Returns export task status and metadata.")
    @PostMapping("/detail")
    @SaCheckPermission("system:export-task:list")
    public Result<SystemRecordVO> detail(@RequestBody IdCommand command) {
        return Result.success(service.detail("EXPORT_TASK", command));
    }

    @Operation(summary = "Page my export tasks", description = "Returns current user's export task page data.")
    @PostMapping("/my-page")
    @SaCheckPermission("system:export-task:list")
    public Result<PageResult<SystemRecordVO>> myPage(@RequestBody PageQuery query) {
        return Result.success(service.page("EXPORT_TASK", query));
    }

    @Operation(summary = "Download export file", description = "Downloads generated export file when the task has succeeded.")
    @PostMapping("/download")
    @SaCheckPermission("system:export-task:download")
    public ResponseEntity<org.springframework.core.io.Resource> download(@RequestBody IdCommand command) {
        SystemDownloadFile file = service.downloadExportTask(command);
        String fileName = URLEncoder.encode(file.fileName(), StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + fileName)
                .body(file.resource());
    }

    @Operation(summary = "Delete export task", description = "Deletes an export task and schedules generated file cleanup.")
    @PostMapping("/delete")
    @SaCheckPermission("system:export-task:delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(service.delete("EXPORT_TASK", command));
    }
}
