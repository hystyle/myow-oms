package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateI18nMessageReqDTO;
import com.myow.system.application.dto.I18nMessageRespDTO;
import com.myow.system.application.dto.PageI18nMessageReqDTO;
import com.myow.system.application.dto.UpdateI18nMessageReqDTO;
import com.myow.system.application.service.I18nMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-国际化消息")
@RestController
@RequestMapping("/system/i18n-message")
@RequiredArgsConstructor
public class I18nMessageController {

    private final I18nMessageService i18nMessageService;

    @PostMapping("/create")
    @Operation(summary = "创建国际化消息")
    public Result<Long> createI18nMessage(@RequestBody CreateI18nMessageReqDTO createReqDTO) {
        return Result.success(i18nMessageService.createI18nMessage(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新国际化消息")
    public Result<Boolean> updateI18nMessage(@RequestBody UpdateI18nMessageReqDTO updateReqDTO) {
        i18nMessageService.updateI18nMessage(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除国际化消息")
    public Result<Boolean> deleteI18nMessage(@PathVariable("id") Long id) {
        i18nMessageService.deleteI18nMessage(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取国际化消息")
    public Result<I18nMessageRespDTO> getI18nMessage(@PathVariable("id") Long id) {
        return Result.success(i18nMessageService.getI18nMessage(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取国际化消息分页")
    public Result<PageResult<I18nMessageRespDTO>> getI18nMessagePage(PageI18nMessageReqDTO pageI18nMessageReqDTO) {
        return Result.success(i18nMessageService.getI18nMessagePage(pageI18nMessageReqDTO));
    }
}
