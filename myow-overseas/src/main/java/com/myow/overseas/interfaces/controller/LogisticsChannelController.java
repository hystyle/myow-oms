package com.myow.overseas.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.overseas.application.base.dto.OverseasBaseModels.IdCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsChannelCreateCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsChannelPageQuery;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsChannelUpdateCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.StatusCommand;
import com.myow.overseas.application.base.service.OverseasBaseDataService;
import com.myow.overseas.application.base.vo.LogisticsChannelVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Overseas Logistics Channel", description = "Logistics channel base data APIs.")
@RestController
@RequestMapping("/api/v1/overseas/base/logistics-channel")
public class LogisticsChannelController {

    private final OverseasBaseDataService service;

    public LogisticsChannelController(OverseasBaseDataService service) {
        this.service = service;
    }

    @Operation(summary = "Create logistics channel", description = "Creates a logistics channel and validates that carrierCustomerId owns the CARRIER role.")
    @PostMapping("/create")
    @SaCheckPermission("overseas:base:logistics-channel:manage")
    public Result<LogisticsChannelVO> create(@RequestBody LogisticsChannelCreateCommand command) {
        return Result.success(service.createChannel(command));
    }

    @Operation(summary = "Update logistics channel", description = "Updates logistics channel base information, carrier, label source and TMS mapping.")
    @PostMapping("/update")
    @SaCheckPermission("overseas:base:logistics-channel:manage")
    public Result<LogisticsChannelVO> update(@RequestBody LogisticsChannelUpdateCommand command) {
        return Result.success(service.updateChannel(command));
    }

    @Operation(summary = "Get logistics channel detail", description = "Returns logistics channel detail by channel id.")
    @PostMapping("/detail")
    @SaCheckPermission("overseas:base:logistics-channel:list")
    public Result<LogisticsChannelVO> detail(@RequestBody IdCommand command) {
        return Result.success(service.channelDetail(command));
    }

    @Operation(summary = "Page logistics channels", description = "Returns logistics channel page data.")
    @PostMapping("/page")
    @SaCheckPermission("overseas:base:logistics-channel:list")
    public Result<PageResult<LogisticsChannelVO>> page(@RequestBody LogisticsChannelPageQuery query) {
        return Result.success(service.pageChannels(query));
    }

    @Operation(summary = "Change logistics channel status", description = "Changes logistics channel status. Enabling requires an active CARRIER role.")
    @PostMapping("/change-status")
    @SaCheckPermission("overseas:base:logistics-channel:manage")
    public Result<Boolean> changeStatus(@RequestBody StatusCommand command) {
        return Result.success(service.changeChannelStatus(command));
    }

    @Operation(summary = "Delete logistics channel", description = "Soft deletes a logistics channel.")
    @PostMapping("/delete")
    @SaCheckPermission("overseas:base:logistics-channel:manage")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(service.deleteChannel(command));
    }
}
