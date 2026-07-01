package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.SystemModels.IdCommand;
import com.myow.system.application.dto.SystemModels.PageQuery;
import com.myow.system.application.dto.SystemModels.SiteCodeQuery;
import com.myow.system.application.dto.SystemModels.SiteConfigCreateCommand;
import com.myow.system.application.dto.SystemModels.SiteConfigUpdateCommand;
import com.myow.system.application.service.SystemSupportService;
import com.myow.system.application.vo.SystemRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "System Site Config", description = "Site configuration APIs")
@RestController
@RequestMapping("/api/v1/system/site-configs")
public class SiteConfigController {

    private final SystemSupportService service;

    public SiteConfigController(SystemSupportService service) {
        this.service = service;
    }

    @Operation(summary = "Create site config", description = "Creates a site configuration item.")
    @PostMapping("/create")
    public Result<SystemRecordVO> create(@RequestBody SiteConfigCreateCommand command) {
        return Result.success(service.createSiteConfig(command));
    }

    @Operation(summary = "Update site config", description = "Updates a site configuration item.")
    @PostMapping("/update")
    public Result<SystemRecordVO> update(@RequestBody SiteConfigUpdateCommand command) {
        return Result.success(service.updateSiteConfig(command));
    }

    @Operation(summary = "Get site config detail", description = "Returns site configuration detail by id.")
    @PostMapping("/detail")
    public Result<SystemRecordVO> detail(@RequestBody IdCommand command) {
        return Result.success(service.detail("SITE_CONFIG", command));
    }

    @Operation(summary = "Page site configs", description = "Returns site configuration page data.")
    @PostMapping("/page")
    public Result<PageResult<SystemRecordVO>> page(@RequestBody PageQuery query) {
        return Result.success(service.page("SITE_CONFIG", query));
    }

    @Operation(summary = "Delete site config", description = "Deletes a site configuration item.")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(service.delete("SITE_CONFIG", command));
    }

    @Operation(summary = "Get configs by site", description = "Returns site configuration map by site code.")
    @PostMapping("/by-site")
    public Result<Map<String, Object>> bySite(@RequestBody SiteCodeQuery query) {
        return Result.success(service.getSiteConfigBySite(query));
    }

    @Operation(summary = "Refresh site config cache", description = "Refreshes site configuration cache.")
    @PostMapping("/refresh")
    public Result<Boolean> refresh(@RequestBody SiteCodeQuery query) {
        return Result.success(query.siteCode() != null);
    }
}
