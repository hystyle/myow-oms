package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.SystemModels.IdCommand;
import com.myow.system.application.dto.SystemModels.PageQuery;
import com.myow.system.application.service.SystemSupportService;
import com.myow.system.application.vo.SystemRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "System File", description = "System file upload and metadata APIs")
@RestController
@RequestMapping("/api/v1/system/files")
public class FileController {

    private final SystemSupportService service;

    public FileController(SystemSupportService service) {
        this.service = service;
    }

    @Operation(summary = "Upload file", description = "Uploads a file and stores file metadata.")
    @PostMapping("/upload")
    public Result<SystemRecordVO> upload(@RequestParam("file") MultipartFile file, @RequestParam("moduleName") String moduleName) {
        return Result.success(service.uploadFile(file, moduleName));
    }

    @Operation(summary = "Batch upload files", description = "Uploads files and stores file metadata.")
    @PostMapping("/batch-upload")
    public Result<List<SystemRecordVO>> batchUpload(@RequestParam("files") MultipartFile[] files, @RequestParam("moduleName") String moduleName) {
        return Result.success(service.batchUploadFile(files, moduleName));
    }

    @Operation(summary = "Get file detail", description = "Returns file metadata detail by id.")
    @PostMapping("/detail")
    public Result<SystemRecordVO> detail(@RequestBody IdCommand command) {
        return Result.success(service.detail("FILE", command));
    }

    @Operation(summary = "Page files", description = "Returns file metadata page data.")
    @PostMapping("/page")
    public Result<PageResult<SystemRecordVO>> page(@RequestBody PageQuery query) {
        return Result.success(service.page("FILE", query));
    }

    @Operation(summary = "Download file", description = "Returns file metadata for download routing.")
    @PostMapping("/download")
    public Result<SystemRecordVO> download(@RequestBody IdCommand command) {
        return Result.success(service.detail("FILE", command));
    }

    @Operation(summary = "Delete file", description = "Deletes file metadata and schedules physical file cleanup.")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(service.delete("FILE", command));
    }
}
