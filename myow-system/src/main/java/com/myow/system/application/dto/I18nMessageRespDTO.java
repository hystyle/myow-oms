package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author gemini
 */
@Getter
@Setter
public class I18nMessageRespDTO {
    private Long id;
    private String keyCode;
    private String lang;
    private String message;
    private Integer version;
    private Short status;
    private String createdBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
