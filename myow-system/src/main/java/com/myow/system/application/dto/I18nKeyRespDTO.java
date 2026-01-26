package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author gemini
 */
@Getter
@Setter
public class I18nKeyRespDTO {
    private Long id;
    private String keyCode;
    private String bizDomain;
    private String description;
    private Short status;
    private String createdBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
