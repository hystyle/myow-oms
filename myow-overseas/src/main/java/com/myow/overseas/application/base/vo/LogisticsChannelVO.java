package com.myow.overseas.application.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Logistics channel detail")
public record LogisticsChannelVO(
        Long channelId,
        Long tenantId,
        String channelCode,
        String channelName,
        Long carrierCustomerId,
        String channelType,
        String labelSource,
        Long tmsSystemId,
        String labelFormat,
        String status,
        String remark,
        LocalDateTime createTime,
        LocalDateTime updateTime) {
}
