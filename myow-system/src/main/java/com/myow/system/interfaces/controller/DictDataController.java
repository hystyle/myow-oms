package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateDictDataReqDTO;
import com.myow.system.application.dto.DictDataRespDTO;
import com.myow.system.application.dto.PageDictDataReqDTO;
import com.myow.system.application.dto.UpdateDictDataReqDTO;
import com.myow.system.application.service.DictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public Result<Long> createDictData(@RequestBody CreateDictDataReqDTO createReqDTO) {
        return Result.success(dictDataService.createDictData(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新字典数据")
    public Result<Boolean> updateDictData(@RequestBody UpdateDictDataReqDTO updateReqDTO) {
        dictDataService.updateDictData(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除字典数据")
    public Result<Boolean> deleteDictData(@PathVariable("id") Long id) {
        dictDataService.deleteDictData(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取字典数据")
    public Result<DictDataRespDTO> getDictData(@PathVariable("id") Long id) {
        return Result.success(dictDataService.getDictData(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取字典数据分页")
    public Result<PageResult<DictDataRespDTO>> getDictDataPage(PageDictDataReqDTO pageDictDataReqDTO) {
        return Result.success(dictDataService.getDictDataPage(pageDictDataReqDTO));
    }
}
