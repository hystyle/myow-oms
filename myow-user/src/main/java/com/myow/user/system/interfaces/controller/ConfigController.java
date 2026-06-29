package com.myow.user.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.ConfigRespDTO;
import com.myow.user.system.application.dto.CreateConfigReqDTO;
import com.myow.user.system.application.dto.PageConfigReqDTO;
import com.myow.user.system.application.dto.UpdateConfigReqDTO;
import com.myow.user.system.application.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "System Config")
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    @PostMapping("/create")
    @Operation(summary = "Create config")
    public Result<Long> createConfig(@RequestBody CreateConfigReqDTO createReqDTO) {
        return Result.success(configService.createConfig(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "Update config")
    public Result<Boolean> updateConfig(@RequestBody UpdateConfigReqDTO updateReqDTO) {
        configService.updateConfig(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "Delete config")
    public Result<Boolean> deleteConfig(@PathVariable("id") Long id) {
        configService.deleteConfig(id);
        return Result.success(true);
    }

    @PostMapping("/get/{id}")
    @Operation(summary = "Get config")
    public Result<ConfigRespDTO> getConfig(@PathVariable("id") Long id) {
        return Result.success(configService.getConfig(id));
    }

    @PostMapping("/effective")
    @Operation(summary = "Get effective config by tenant and key")
    public Result<ConfigRespDTO> getEffectiveConfig(@RequestParam(value = "tenantId", required = false) Long tenantId,
                                                   @RequestParam("configKey") String configKey) {
        return Result.success(configService.getEffectiveConfig(tenantId, configKey));
    }

    @PostMapping("/page")
    @Operation(summary = "Page configs")
    public Result<PageResult<ConfigRespDTO>> getConfigPage(@RequestBody PageConfigReqDTO pageConfigReqDTO) {
        return Result.success(configService.getConfigPage(pageConfigReqDTO));
    }
}
