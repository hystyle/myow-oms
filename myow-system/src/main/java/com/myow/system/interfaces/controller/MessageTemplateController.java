package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.SystemModels.IdCommand;
import com.myow.system.application.dto.SystemModels.MessageTemplateCreateCommand;
import com.myow.system.application.dto.SystemModels.MessageTemplatePreviewCommand;
import com.myow.system.application.dto.SystemModels.MessageTemplateUpdateCommand;
import com.myow.system.application.dto.SystemModels.PageQuery;
import com.myow.system.application.service.SystemSupportService;
import com.myow.system.application.vo.SystemRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "System Message Template", description = "Message template APIs")
@RestController
@RequestMapping("/api/v1/system/message-templates")
public class MessageTemplateController {

    private final SystemSupportService service;

    public MessageTemplateController(SystemSupportService service) {
        this.service = service;
    }

    @Operation(summary = "Create message template", description = "Creates a message template.")
    @PostMapping("/create")
    public Result<SystemRecordVO> create(@RequestBody MessageTemplateCreateCommand command) {
        return Result.success(service.createMessageTemplate(command));
    }

    @Operation(summary = "Update message template", description = "Updates a message template.")
    @PostMapping("/update")
    public Result<SystemRecordVO> update(@RequestBody MessageTemplateUpdateCommand command) {
        return Result.success(service.updateMessageTemplate(command));
    }

    @Operation(summary = "Get message template detail", description = "Returns message template detail by id.")
    @PostMapping("/detail")
    public Result<SystemRecordVO> detail(@RequestBody IdCommand command) {
        return Result.success(service.detail("MESSAGE_TEMPLATE", command));
    }

    @Operation(summary = "Page message templates", description = "Returns message template page data.")
    @PostMapping("/page")
    public Result<PageResult<SystemRecordVO>> page(@RequestBody PageQuery query) {
        return Result.success(service.page("MESSAGE_TEMPLATE", query));
    }

    @Operation(summary = "Delete message template", description = "Deletes a message template.")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody IdCommand command) {
        return Result.success(service.delete("MESSAGE_TEMPLATE", command));
    }

    @Operation(summary = "Preview message template", description = "Renders a message template using sample variables.")
    @PostMapping("/preview")
    public Result<Map<String, Object>> preview(@RequestBody MessageTemplatePreviewCommand command) {
        return Result.success(service.previewMessageTemplate(command));
    }
}
