package com.myow.user.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreatePositionReqDTO;
import com.myow.user.system.application.dto.IdReqDTO;
import com.myow.user.system.application.dto.PagePositionReqDTO;
import com.myow.user.system.application.dto.PositionRespDTO;
import com.myow.user.system.application.dto.UpdatePositionReqDTO;
import com.myow.user.system.application.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-岗位")
@RestController
@RequestMapping("/system/position")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping("/create")
    @Operation(summary = "创建岗位")
    @SaCheckPermission("system:position:add")
    public Result<Long> createPosition(@RequestBody @Validated CreatePositionReqDTO createReqDTO) {
        return Result.success(positionService.createPosition(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新岗位")
    @SaCheckPermission("system:position:update")
    public Result<Boolean> updatePosition(@RequestBody @Validated UpdatePositionReqDTO updateReqDTO) {
        positionService.updatePosition(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除岗位")
    @SaCheckPermission("system:position:delete")
    public Result<Boolean> deletePosition(@RequestBody @Validated IdReqDTO reqDTO) {
        positionService.deletePosition(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取岗位")
    @SaCheckPermission("system:position:query")
    public Result<PositionRespDTO> getPosition(@RequestBody @Validated IdReqDTO reqDTO) {
        return Result.success(positionService.getPosition(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取岗位分页")
    @SaCheckPermission("system:position:query")
    public Result<PageResult<PositionRespDTO>> getPositionPage(@RequestBody PagePositionReqDTO pagePositionReqDTO) {
        return Result.success(positionService.getPositionPage(pagePositionReqDTO));
    }
}
