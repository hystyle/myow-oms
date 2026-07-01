package com.myow.system.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "System support record")
public record SystemRecordVO(
        @Schema(description = "Record id") Long id,
        @Schema(description = "Record type") String type,
        @Schema(description = "Record code") String code,
        @Schema(description = "Record name") String name,
        @Schema(description = "Record status") Integer status,
        @Schema(description = "Record attributes") Map<String, Object> attributes,
        @Schema(description = "Created time") LocalDateTime createTime,
        @Schema(description = "Updated time") LocalDateTime updateTime) {
}
