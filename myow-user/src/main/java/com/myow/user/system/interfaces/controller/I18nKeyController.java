package com.myow.user.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateI18nKeyReqDTO;
import com.myow.user.system.application.dto.I18nKeyRespDTO;
import com.myow.user.system.application.dto.IdReqDTO;
import com.myow.user.system.application.dto.PageI18nKeyReqDTO;
import com.myow.user.system.application.dto.UpdateI18nKeyReqDTO;
import com.myow.user.system.application.service.I18nKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping("/update")
    @Operation(summary = "更新国际化键")
    public Result<Boolean> updateI18nKey(@RequestBody @Validated UpdateI18nKeyReqDTO updateReqDTO) {
        i18nKeyService.updateI18nKey(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除国际化键")
    public Result<Boolean> deleteI18nKey(@RequestBody @Validated IdReqDTO reqDTO) {
        i18nKeyService.deleteI18nKey(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取国际化键")
    public Result<I18nKeyRespDTO> getI18nKey(@RequestBody @Validated IdReqDTO reqDTO) {
        return Result.success(i18nKeyService.getI18nKey(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取国际化键分页")
    public Result<PageResult<I18nKeyRespDTO>> getI18nKeyPage(@RequestBody PageI18nKeyReqDTO pageI18nKeyReqDTO) {
        return Result.success(i18nKeyService.getI18nKeyPage(pageI18nKeyReqDTO));
    }
}
