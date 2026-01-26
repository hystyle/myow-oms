package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateDictReqDTO;
import com.myow.system.application.dto.DictRespDTO;
import com.myow.system.application.dto.PageDictReqDTO;
import com.myow.system.application.dto.UpdateDictReqDTO;
import com.myow.system.application.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-字典")
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @PostMapping("/create")
    @Operation(summary = "创建字典")
    public Result<Long> createDict(@RequestBody CreateDictReqDTO createReqDTO) {
        return Result.success(dictService.createDict(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新字典")
    public Result<Boolean> updateDict(@RequestBody UpdateDictReqDTO updateReqDTO) {
        dictService.updateDict(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除字典")
    public Result<Boolean> deleteDict(@PathVariable("id") Long id) {
        dictService.deleteDict(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取字典")
    public Result<DictRespDTO> getDict(@PathVariable("id") Long id) {
        return Result.success(dictService.getDict(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取字典分页")
    public Result<PageResult<DictRespDTO>> getDictPage(PageDictReqDTO pageDictReqDTO) {
        return Result.success(dictService.getDictPage(pageDictReqDTO));
    }
}
