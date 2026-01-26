package com.myow.system.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author yss
 */
@Getter
@Setter
public class I18nKey {
    private Long id;

    private String keyCode;

    private String bizDomain;

    private String description;

    private Short status;

    private String createdBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
