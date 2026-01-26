package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateI18nKeyReqDTO;
import com.myow.system.application.dto.I18nKeyRespDTO;
import com.myow.system.application.dto.PageI18nKeyReqDTO;
import com.myow.system.application.dto.UpdateI18nKeyReqDTO;
import com.myow.system.application.service.I18nKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-国际化键")
@RestController
@RequestMapping("/system/i18n-key")
@RequiredArgsConstructor
public class I18nKeyController {

    private final I18nKeyService i18nKeyService;

    @PostMapping("/create")
    @Operation(summary = "创建国际化键")
    public Result<Long> createI18nKey(@RequestBody CreateI18nKeyReqDTO createReqDTO) {
        return Result.success(i18nKeyService.createI18nKey(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新国际化键")
    public Result<Boolean> updateI18nKey(@RequestBody UpdateI18nKeyReqDTO updateReqDTO) {
        i18nKeyService.updateI18nKey(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除国际化键")
    public Result<Boolean> deleteI18nKey(@PathVariable("id") Long id) {
        i18nKeyService.deleteI18nKey(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取国际化键")
    public Result<I18nKeyRespDTO> getI18nKey(@PathVariable("id") Long id) {
        return Result.success(i18nKeyService.getI18nKey(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取国际化键分页")
    public Result<PageResult<I18nKeyRespDTO>> getI18nKeyPage(PageI18nKeyReqDTO pageI18nKeyReqDTO) {
        return Result.success(i18nKeyService.getI18nKeyPage(pageI18nKeyReqDTO));
    }
}
