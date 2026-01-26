package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateOperLogReqDTO;
import com.myow.system.application.dto.OperLogRespDTO;
import com.myow.system.application.dto.PageOperLogReqDTO;
import com.myow.system.application.dto.UpdateOperLogReqDTO;
import com.myow.system.application.service.OperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-操作日志")
@RestController
@RequestMapping("/system/oper-log")
@RequiredArgsConstructor
public class OperLogController {

    private final OperLogService operLogService;

    @PostMapping("/create")
    @Operation(summary = "创建操作日志")
    public Result<Long> createOperLog(@RequestBody CreateOperLogReqDTO createReqDTO) {
        return Result.success(operLogService.createOperLog(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新操作日志")
    public Result<Boolean> updateOperLog(@RequestBody UpdateOperLogReqDTO updateReqDTO) {
        operLogService.updateOperLog(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除操作日志")
    public Result<Boolean> deleteOperLog(@PathVariable("id") Long id) {
        operLogService.deleteOperLog(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取操作日志")
    public Result<OperLogRespDTO> getOperLog(@PathVariable("id") Long id) {
        return Result.success(operLogService.getOperLog(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取操作日志分页")
    public Result<PageResult<OperLogRespDTO>> getOperLogPage(PageOperLogReqDTO pageOperLogReqDTO) {
        return Result.success(operLogService.getOperLogPage(pageOperLogReqDTO));
    }
}
