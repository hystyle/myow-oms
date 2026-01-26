package com.myow.system.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author yss
 */
@Getter
@Setter
public class DictData {
    private Long dictDataId;

    private Long dictId;

    private String dataValue;

    private String dataLabel;

    private String remark;

    private Integer sort;

    private Boolean disabledFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
