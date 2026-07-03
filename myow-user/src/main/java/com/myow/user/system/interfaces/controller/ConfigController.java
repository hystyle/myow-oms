package com.myow.user.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
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

@Tag(name = "系统模块-参数配置", description = "维护平台系统级和租户级参数配置，支持按租户覆盖全局参数。")
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    @PostMapping("/create")
    @Operation(summary = "创建参数配置", description = "创建系统参数或租户参数。tenantId 为空时写入全局参数，configKey 在同一租户下必须唯一。")
    @SaCheckPermission("system:config:add")
    public Result<Long> createConfig(@RequestBody CreateConfigReqDTO createReqDTO) {
        return Result.success(configService.createConfig(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新参数配置", description = "更新指定参数配置的值、类型、分组、系统参数标识和备注。系统参数可更新但删除会被业务规则拦截。")
    @SaCheckPermission("system:config:update")
    public Result<Boolean> updateConfig(@RequestBody UpdateConfigReqDTO updateReqDTO) {
        configService.updateConfig(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "删除参数配置", description = "按配置 ID 删除参数配置。系统内置参数不允许删除，删除前应确认没有业务逻辑依赖该参数。")
    @SaCheckPermission("system:config:delete")
    public Result<Boolean> deleteConfig(@PathVariable("id") Long id) {
        configService.deleteConfig(id);
        return Result.success(true);
    }

    @PostMapping("/get/{id}")
    @Operation(summary = "获取参数配置详情", description = "按配置 ID 获取参数配置详情，用于编辑抽屉、详情页和配置审计展示。")
    @SaCheckPermission("system:config:query")
    public Result<ConfigRespDTO> getConfig(@PathVariable("id") Long id) {
        return Result.success(configService.getConfig(id));
    }

    @PostMapping("/effective")
    @Operation(summary = "获取生效参数", description = "按 tenantId 和 configKey 获取最终生效配置。租户配置不存在时回退到全局配置。")
    @SaCheckPermission("system:config:query")
    public Result<ConfigRespDTO> getEffectiveConfig(@RequestParam(value = "tenantId", required = false) Long tenantId,
                                                   @RequestParam("configKey") String configKey) {
        return Result.success(configService.getEffectiveConfig(tenantId, configKey));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询参数配置", description = "按租户、参数键和分组分页查询配置项，供管理端参数配置列表和筛选条件使用。")
    @SaCheckPermission("system:config:query")
    public Result<PageResult<ConfigRespDTO>> getConfigPage(@RequestBody PageConfigReqDTO pageConfigReqDTO) {
        return Result.success(configService.getConfigPage(pageConfigReqDTO));
    }
}
