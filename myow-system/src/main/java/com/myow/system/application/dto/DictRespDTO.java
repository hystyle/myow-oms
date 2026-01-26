package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author yss
 */
@Getter
@Setter
public class DictRespDTO {
    private Long dictId;
    private String dictName;
    private String dictCode;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
}
