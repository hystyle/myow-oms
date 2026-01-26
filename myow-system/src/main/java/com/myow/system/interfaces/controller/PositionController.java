package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreatePositionReqDTO;
import com.myow.system.application.dto.PagePositionReqDTO;
import com.myow.system.application.dto.PositionRespDTO;
import com.myow.system.application.dto.UpdatePositionReqDTO;
import com.myow.system.application.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author gemini
 */
@Tag(name = "系统模块-岗位")
@RestController
@RequestMapping("/system/position")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping("/create")
    @Operation(summary = "创建岗位")
    public Result<Long> createPosition(@RequestBody CreatePositionReqDTO createReqDTO) {
        return Result.success(positionService.createPosition(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新岗位")
    public Result<Boolean> updatePosition(@RequestBody UpdatePositionReqDTO updateReqDTO) {
        positionService.updatePosition(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除岗位")
    public Result<Boolean> deletePosition(@PathVariable("id") Long id) {
        positionService.deletePosition(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取岗位")
    public Result<PositionRespDTO> getPosition(@PathVariable("id") Long id) {
        return Result.success(positionService.getPosition(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取岗位分页")
    public Result<PageResult<PositionRespDTO>> getPositionPage(PagePositionReqDTO pagePositionReqDTO) {
        return Result.success(positionService.getPositionPage(pagePositionReqDTO));
    }
}
