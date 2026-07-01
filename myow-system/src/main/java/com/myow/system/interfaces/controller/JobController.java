package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.SystemModels.IdCommand;
import com.myow.system.application.dto.SystemModels.JobCreateCommand;
import com.myow.system.application.dto.SystemModels.JobUpdateCommand;
import com.myow.system.application.dto.SystemModels.PageQuery;
import com.myow.system.application.service.SystemSupportService;
import com.myow.system.application.vo.SystemRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "System Job", description = "System scheduled job management APIs")
@RestController
@RequestMapping("/api/v1/system/jobs")
public class JobController {

    private final SystemSupportService service;

    public JobController(SystemSupportService service) {
        this.service = service;
    }

    @Operation(summary = "Create scheduled job", description = "Creates a scheduled job configuration.")
    @PostMapping("/create")
    public Result<SystemRecordVO> create(@RequestBody JobCreateCommand command) {
        return Result.success(service.createJob(command));
    }

    @Operation(summary = "Update scheduled job", description = "Updates job basic information and scheduling expression.")
    @PostMapping("/update")
    public Result<SystemRecordVO> update(@RequestBody JobUpdateCommand command) {
        return Result.success(service.updateJob(command));
    }

    @Operation(summary = "Get scheduled job detail", description = "Returns scheduled job detail by id.")
    @PostMapping("/detail")
    public Result<SystemRecordVO> detail(@RequestBody IdCommand command) {
        return Result.success(service.detail("JOB", command));
    }

    @Operation(summary = "Page scheduled jobs", description = "Returns scheduled job page data.")
    @PostMapping("/page")
    public Result<PageResult<SystemRecordVO>> page(@RequestBody PageQuery query) {
        return Result.success(service.page("JOB", query));
    }

    @Operation(summary = "Delete scheduled job", description = "Deletes a scheduled job configuration.")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(service.delete("JOB", command));
    }

    @Operation(summary = "Run scheduled job once", description = "Runs the selected scheduled job immediately once.")
    @PostMapping("/run")
    public Result<SystemRecordVO> run(@RequestBody IdCommand command) {
        return Result.success(service.changeStatus("JOB", command, 1));
    }

    @Operation(summary = "Pause scheduled job", description = "Pauses the selected scheduled job.")
    @PostMapping("/pause")
    public Result<SystemRecordVO> pause(@RequestBody IdCommand command) {
        return Result.success(service.changeStatus("JOB", command, 0));
    }

    @Operation(summary = "Resume scheduled job", description = "Resumes the selected scheduled job.")
    @PostMapping("/resume")
    public Result<SystemRecordVO> resume(@RequestBody IdCommand command) {
        return Result.success(service.changeStatus("JOB", command, 1));
    }

    @Operation(summary = "Page scheduled job logs", description = "Returns scheduled job execution log page data.")
    @PostMapping("/log-page")
    public Result<PageResult<SystemRecordVO>> logPage(@RequestBody PageQuery query) {
        return Result.success(service.page("JOB_LOG", query));
    }
}
