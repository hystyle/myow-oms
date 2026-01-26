package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateSerialNoConfigReqDTO;
import com.myow.system.application.dto.PageSerialNoConfigReqDTO;
import com.myow.system.application.dto.SerialNoConfigRespDTO;
import com.myow.system.application.dto.UpdateSerialNoConfigReqDTO;
import com.myow.system.application.service.SerialNoConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author gemini
 */
@Tag(name = "系统模块-流水号配置")
@RestController
@RequestMapping("/system/serial-no-config")
@RequiredArgsConstructor
public class SerialNoConfigController {

    private final SerialNoConfigService serialNoConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建流水号配置")
    public Result<Integer> createSerialNoConfig(@RequestBody CreateSerialNoConfigReqDTO createReqDTO) {
        return Result.success(serialNoConfigService.createSerialNoConfig(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新流水号配置")
    public Result<Boolean> updateSerialNoConfig(@RequestBody UpdateSerialNoConfigReqDTO updateReqDTO) {
        serialNoConfigService.updateSerialNoConfig(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除流水号配置")
    public Result<Boolean> deleteSerialNoConfig(@PathVariable("id") Integer id) {
        serialNoConfigService.deleteSerialNoConfig(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取流水号配置")
    public Result<SerialNoConfigRespDTO> getSerialNoConfig(@PathVariable("id") Integer id) {
        return Result.success(serialNoConfigService.getSerialNoConfig(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取流水号配置分页")
    public Result<PageResult<SerialNoConfigRespDTO>> getSerialNoConfigPage(PageSerialNoConfigReqDTO pageSerialNoConfigReqDTO) {
        return Result.success(serialNoConfigService.getSerialNoConfigPage(pageSerialNoConfigReqDTO));
    }
}
