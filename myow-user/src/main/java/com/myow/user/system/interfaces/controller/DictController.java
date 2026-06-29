package com.myow.user.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateDictReqDTO;
import com.myow.user.system.application.dto.DictRespDTO;
import com.myow.user.system.application.dto.IdReqDTO;
import com.myow.user.system.application.dto.PageDictReqDTO;
import com.myow.user.system.application.dto.UpdateDictReqDTO;
import com.myow.user.system.application.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
    @SaCheckPermission("system:dict:add")
    public Result<Long> createDict(@RequestBody @Validated CreateDictReqDTO createReqDTO) {
        return Result.success(dictService.createDict(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新字典")
    @SaCheckPermission("system:dict:update")
    public Result<Boolean> updateDict(@RequestBody @Validated UpdateDictReqDTO updateReqDTO) {
        dictService.updateDict(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除字典")
    @SaCheckPermission("system:dict:delete")
    public Result<Boolean> deleteDict(@RequestBody @Validated IdReqDTO reqDTO) {
        dictService.deleteDict(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取字典")
    @SaCheckPermission("system:dict:query")
    public Result<DictRespDTO> getDict(@RequestBody @Validated IdReqDTO reqDTO) {
        return Result.success(dictService.getDict(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取字典分页")
    @SaCheckPermission("system:dict:query")
    public Result<PageResult<DictRespDTO>> getDictPage(@RequestBody PageDictReqDTO pageDictReqDTO) {
        return Result.success(dictService.getDictPage(pageDictReqDTO));
    }
}
