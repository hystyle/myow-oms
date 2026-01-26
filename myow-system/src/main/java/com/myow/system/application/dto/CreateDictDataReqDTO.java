package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class CreateDictDataReqDTO {
    private Long dictId;
    private String dataValue;
    private String dataLabel;
    private String remark;
    private Integer sort;
    private Boolean disabledFlag;
}
