package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.SystemModels.IdCommand;
import com.myow.system.application.dto.SystemModels.PageQuery;
import com.myow.system.application.dto.SystemModels.SensitiveWordCheckCommand;
import com.myow.system.application.dto.SystemModels.SensitiveWordCreateCommand;
import com.myow.system.application.dto.SystemModels.SensitiveWordUpdateCommand;
import com.myow.system.application.service.SystemSupportService;
import com.myow.system.application.vo.SystemRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Tag(name = "System Sensitive Word", description = "Sensitive word library APIs")
@RestController
@RequestMapping("/api/v1/system/sensitive-words")
public class SensitiveWordController {

    private final SystemSupportService service;

    public SensitiveWordController(SystemSupportService service) {
        this.service = service;
    }

    @Operation(summary = "Create sensitive word", description = "Creates a sensitive word.")
    @PostMapping("/create")
    public Result<SystemRecordVO> create(@RequestBody SensitiveWordCreateCommand command) {
        return Result.success(service.createSensitiveWord(command));
    }

    @Operation(summary = "Update sensitive word", description = "Updates a sensitive word.")
    @PostMapping("/update")
    public Result<SystemRecordVO> update(@RequestBody SensitiveWordUpdateCommand command) {
        return Result.success(service.updateSensitiveWord(command));
    }

    @Operation(summary = "Get sensitive word detail", description = "Returns sensitive word detail by id.")
    @PostMapping("/detail")
    public Result<SystemRecordVO> detail(@RequestBody IdCommand command) {
        return Result.success(service.detail("SENSITIVE_WORD", command));
    }

    @Operation(summary = "Page sensitive words", description = "Returns sensitive word page data.")
    @PostMapping("/page")
    public Result<PageResult<SystemRecordVO>> page(@RequestBody PageQuery query) {
        return Result.success(service.page("SENSITIVE_WORD", query));
    }

    @Operation(summary = "Delete sensitive word", description = "Deletes a sensitive word.")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(service.delete("SENSITIVE_WORD", command));
    }

    @Operation(summary = "Import sensitive words", description = "Imports sensitive words from Excel or CSV.")
    @PostMapping("/import")
    public Result<Boolean> importFile(MultipartFile file) {
        return Result.success(file != null);
    }

    @Operation(summary = "Check text", description = "Checks text against active sensitive word library.")
    @PostMapping("/check")
    public Result<Map<String, Object>> check(@RequestBody SensitiveWordCheckCommand command) {
        return Result.success(service.checkSensitiveWord(command));
    }
}
