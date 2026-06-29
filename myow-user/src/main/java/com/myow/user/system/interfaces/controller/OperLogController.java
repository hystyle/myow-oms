package com.myow.user.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateOperLogReqDTO;
import com.myow.user.system.application.dto.IdReqDTO;
import com.myow.user.system.application.dto.OperLogRespDTO;
import com.myow.user.system.application.dto.PageOperLogReqDTO;
import com.myow.user.system.application.dto.UpdateOperLogReqDTO;
import com.myow.user.system.application.service.OperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping("/update")
    @Operation(summary = "更新操作日志")
    public Result<Boolean> updateOperLog(@RequestBody @Validated UpdateOperLogReqDTO updateReqDTO) {
        operLogService.updateOperLog(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除操作日志")
    public Result<Boolean> deleteOperLog(@RequestBody @Validated IdReqDTO reqDTO) {
        operLogService.deleteOperLog(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取操作日志")
    public Result<OperLogRespDTO> getOperLog(@RequestBody @Validated IdReqDTO reqDTO) {
        return Result.success(operLogService.getOperLog(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取操作日志分页")
    public Result<PageResult<OperLogRespDTO>> getOperLogPage(@RequestBody PageOperLogReqDTO pageOperLogReqDTO) {
        return Result.success(operLogService.getOperLogPage(pageOperLogReqDTO));
    }
}
