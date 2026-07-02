package com.myow.system.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

public final class SystemModels {

    private SystemModels() {
    }

    @Schema(description = "Primary id command")
    public record IdCommand(@Schema(description = "Primary id") Long id) {
    }

    @Schema(description = "Empty command")
    public record EmptyCommand() {
    }

    @Schema(description = "Generic page query")
    public record PageQuery(
            @Schema(description = "Keyword") String keyword,
            @Schema(description = "Status") Integer status,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Job create command")
    public record JobCreateCommand(String jobName, String jobGroup, String cronExpression, String handlerName) {
    }

    @Schema(description = "Job update command")
    public record JobUpdateCommand(Long id, String jobName, String jobGroup, String cronExpression, String handlerName, Integer status) {
    }

    @Schema(description = "Notice create command")
    public record NoticeCreateCommand(String title, String content, String noticeType, LocalDateTime expireTime) {
    }

    @Schema(description = "Notice update command")
    public record NoticeUpdateCommand(Long id, String title, String content, String noticeType, LocalDateTime expireTime) {
    }

    @Schema(description = "Site config create command")
    public record SiteConfigCreateCommand(String siteCode, String configKey, String configValue, String configType, String remark) {
    }

    @Schema(description = "Site config update command")
    public record SiteConfigUpdateCommand(Long id, String siteCode, String configKey, String configValue, String configType, String remark) {
    }

    @Schema(description = "Site code query")
    public record SiteCodeQuery(String siteCode) {
    }

    @Schema(description = "Sensitive word create command")
    public record SensitiveWordCreateCommand(String word, String category, Integer level, String replacement) {
    }

    @Schema(description = "Sensitive word update command")
    public record SensitiveWordUpdateCommand(Long id, String word, String category, Integer level, String replacement, Integer status) {
    }

    @Schema(description = "Sensitive word check command")
    public record SensitiveWordCheckCommand(String text) {
    }

    @Schema(description = "Message template create command")
    public record MessageTemplateCreateCommand(String templateCode, String channel, String title, String content, String variables) {
    }

    @Schema(description = "Message template update command")
    public record MessageTemplateUpdateCommand(Long id, String templateCode, String channel, String title, String content, String variables, Integer status) {
    }

    @Schema(description = "Message template preview command")
    public record MessageTemplatePreviewCommand(Long id, String templateCode, Map<String, String> variables) {
    }

    @Schema(description = "Download file payload")
    public record DownloadFile(String fileName, String contentType, org.springframework.core.io.Resource resource) {
    }

    @Schema(description = "Export task create command")
    public record ExportTaskCreateCommand(String moduleName, String exportType, Map<String, Object> queryParams) {
    }

    @Schema(description = "Online user page query")
    public record OnlineUserPageQuery(String loginName, Long pageNum, Long pageSize) {
    }

    @Schema(description = "Kick online user command")
    public record KickOnlineUserCommand(String token) {
    }
}
