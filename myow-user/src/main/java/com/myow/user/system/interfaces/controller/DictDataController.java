package com.myow.user.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateDictDataReqDTO;
import com.myow.user.system.application.dto.DictDataRespDTO;
import com.myow.user.system.application.dto.IdReqDTO;
import com.myow.user.system.application.dto.PageDictDataReqDTO;
import com.myow.user.system.application.dto.UpdateDictDataReqDTO;
import com.myow.user.system.application.service.DictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-字典数据")
@RestController
@RequestMapping("/system/dict-data")
@RequiredArgsConstructor
public class DictDataController {

    private final DictDataService dictDataService;

    @PostMapping("/create")
    @Operation(summary = "创建字典数据")
    @SaCheckPermission("system:dict:add")
    public Result<Long> createDictData(@RequestBody @Validated CreateDictDataReqDTO createReqDTO) {
        return Result.success(dictDataService.createDictData(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新字典数据")
    @SaCheckPermission("system:dict:update")
    public Result<Boolean> updateDictData(@RequestBody @Validated UpdateDictDataReqDTO updateReqDTO) {
        dictDataService.updateDictData(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除字典数据")
    @SaCheckPermission("system:dict:delete")
    public Result<Boolean> deleteDictData(@RequestBody @Validated IdReqDTO reqDTO) {
        dictDataService.deleteDictData(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取字典数据")
    @SaCheckPermission("system:dict:query")
    public Result<DictDataRespDTO> getDictData(@RequestBody @Validated IdReqDTO reqDTO) {
        return Result.success(dictDataService.getDictData(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取字典数据分页")
    @SaCheckPermission("system:dict:query")
    public Result<PageResult<DictDataRespDTO>> getDictDataPage(@RequestBody PageDictDataReqDTO pageDictDataReqDTO) {
        return Result.success(dictDataService.getDictDataPage(pageDictDataReqDTO));
    }
}
