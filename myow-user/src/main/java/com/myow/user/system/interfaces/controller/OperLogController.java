package com.myow.user.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
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
@Tag(name = "系统模块-操作日志", description = "记录和查询后台关键操作行为，包括业务类型、操作人、请求信息、执行状态和耗时。")
@RestController
@RequestMapping("/system/oper-log")
@RequiredArgsConstructor
public class OperLogController {

    private final OperLogService operLogService;

    @PostMapping("/create")
    @Operation(summary = "创建操作日志", description = "写入一条后台操作日志。通常由操作审计组件或业务服务调用，管理端页面不直接创建。")
    @SaCheckPermission("system:oper-log:add")
    public Result<Long> createOperLog(@RequestBody CreateOperLogReqDTO createReqDTO) {
        return Result.success(operLogService.createOperLog(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新操作日志", description = "更新操作日志内容。仅用于审计数据修正场景，普通管理端列表默认不开放编辑。")
    @SaCheckPermission("system:oper-log:update")
    public Result<Boolean> updateOperLog(@RequestBody @Validated UpdateOperLogReqDTO updateReqDTO) {
        operLogService.updateOperLog(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除操作日志", description = "按日志 ID 删除操作日志。删除会影响审计追溯，应仅在日志清理或合规处理场景使用。")
    @SaCheckPermission("system:oper-log:delete")
    public Result<Boolean> deleteOperLog(@RequestBody @Validated IdReqDTO reqDTO) {
        operLogService.deleteOperLog(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取操作日志详情", description = "按日志 ID 获取操作日志详情，包括请求参数、返回参数、错误信息和耗时。")
    @SaCheckPermission("system:oper-log:query")
    public Result<OperLogRespDTO> getOperLog(@RequestBody @Validated IdReqDTO reqDTO) {
        return Result.success(operLogService.getOperLog(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询操作日志", description = "按模块标题、业务类型、操作类别、操作人、状态和时间范围分页查询操作日志。")
    @SaCheckPermission("system:oper-log:query")
    public Result<PageResult<OperLogRespDTO>> getOperLogPage(@RequestBody PageOperLogReqDTO pageOperLogReqDTO) {
        return Result.success(operLogService.getOperLogPage(pageOperLogReqDTO));
    }
}
