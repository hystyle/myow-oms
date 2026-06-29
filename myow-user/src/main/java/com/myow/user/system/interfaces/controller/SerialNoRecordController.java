package com.myow.user.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateSerialNoRecordReqDTO;
import com.myow.user.system.application.dto.PageSerialNoRecordReqDTO;
import com.myow.user.system.application.dto.SerialNoRecordIdReqDTO;
import com.myow.user.system.application.dto.SerialNoRecordRespDTO;
import com.myow.user.system.application.dto.UpdateSerialNoRecordReqDTO;
import com.myow.user.system.application.service.SerialNoRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-流水号记录")
@RestController
@RequestMapping("/system/serial-no-record")
@RequiredArgsConstructor
public class SerialNoRecordController {

    private final SerialNoRecordService serialNoRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建流水号记录")
    public Result<Integer> createSerialNoRecord(@RequestBody CreateSerialNoRecordReqDTO createReqDTO) {
        return Result.success(serialNoRecordService.createSerialNoRecord(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新流水号记录")
    public Result<Boolean> updateSerialNoRecord(@RequestBody @Validated UpdateSerialNoRecordReqDTO updateReqDTO) {
        serialNoRecordService.updateSerialNoRecord(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除流水号记录")
    public Result<Boolean> deleteSerialNoRecord(@RequestBody @Validated SerialNoRecordIdReqDTO reqDTO) {
        serialNoRecordService.deleteSerialNoRecord(reqDTO.getSerialNumberId(), reqDTO.getRecordDate());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取流水号记录")
    public Result<SerialNoRecordRespDTO> getSerialNoRecord(@RequestBody @Validated SerialNoRecordIdReqDTO reqDTO) {
        return Result.success(serialNoRecordService.getSerialNoRecord(reqDTO.getSerialNumberId(), reqDTO.getRecordDate()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取流水号记录分页")
    public Result<PageResult<SerialNoRecordRespDTO>> getSerialNoRecordPage(@RequestBody PageSerialNoRecordReqDTO pageSerialNoRecordReqDTO) {
        return Result.success(serialNoRecordService.getSerialNoRecordPage(pageSerialNoRecordReqDTO));
    }
}
