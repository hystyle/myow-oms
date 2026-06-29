package com.myow.user.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateSerialNoConfigReqDTO;
import com.myow.user.system.application.dto.IntIdReqDTO;
import com.myow.user.system.application.dto.PageSerialNoConfigReqDTO;
import com.myow.user.system.application.dto.SerialNoConfigRespDTO;
import com.myow.user.system.application.dto.UpdateSerialNoConfigReqDTO;
import com.myow.user.system.application.service.SerialNoConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
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

    @PostMapping("/update")
    @Operation(summary = "更新流水号配置")
    public Result<Boolean> updateSerialNoConfig(@RequestBody @Validated UpdateSerialNoConfigReqDTO updateReqDTO) {
        serialNoConfigService.updateSerialNoConfig(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除流水号配置")
    public Result<Boolean> deleteSerialNoConfig(@RequestBody @Validated IntIdReqDTO reqDTO) {
        serialNoConfigService.deleteSerialNoConfig(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取流水号配置")
    public Result<SerialNoConfigRespDTO> getSerialNoConfig(@RequestBody @Validated IntIdReqDTO reqDTO) {
        return Result.success(serialNoConfigService.getSerialNoConfig(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取流水号配置分页")
    public Result<PageResult<SerialNoConfigRespDTO>> getSerialNoConfigPage(@RequestBody PageSerialNoConfigReqDTO pageSerialNoConfigReqDTO) {
        return Result.success(serialNoConfigService.getSerialNoConfigPage(pageSerialNoConfigReqDTO));
    }
}
