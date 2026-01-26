package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateSerialNoRecordReqDTO;
import com.myow.system.application.dto.PageSerialNoRecordReqDTO;
import com.myow.system.application.dto.SerialNoRecordRespDTO;
import com.myow.system.application.dto.UpdateSerialNoRecordReqDTO;
import com.myow.system.application.service.SerialNoRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PutMapping("/update")
    @Operation(summary = "更新流水号记录")
    public Result<Boolean> updateSerialNoRecord(@RequestBody UpdateSerialNoRecordReqDTO updateReqDTO) {
        serialNoRecordService.updateSerialNoRecord(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{serialNumberId}/{recordDate}")
    @Operation(summary = "删除流水号记录")
    public Result<Boolean> deleteSerialNoRecord(
            @PathVariable("serialNumberId") Integer serialNumberId,
            @PathVariable("recordDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate) {
        serialNoRecordService.deleteSerialNoRecord(serialNumberId, recordDate);
        return Result.success(true);
    }

    @GetMapping("/get/{serialNumberId}/{recordDate}")
    @Operation(summary = "获取流水号记录")
    public Result<SerialNoRecordRespDTO> getSerialNoRecord(
            @PathVariable("serialNumberId") Integer serialNumberId,
            @PathVariable("recordDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate) {
        return Result.success(serialNoRecordService.getSerialNoRecord(serialNumberId, recordDate));
    }

    @GetMapping("/page")
    @Operation(summary = "获取流水号记录分页")
    public Result<PageResult<SerialNoRecordRespDTO>> getSerialNoRecordPage(PageSerialNoRecordReqDTO pageSerialNoRecordReqDTO) {
        return Result.success(serialNoRecordService.getSerialNoRecordPage(pageSerialNoRecordReqDTO));
    }
}
