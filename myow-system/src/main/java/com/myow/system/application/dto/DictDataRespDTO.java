package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author yss
 */
@Getter
@Setter
public class DictDataRespDTO {
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
