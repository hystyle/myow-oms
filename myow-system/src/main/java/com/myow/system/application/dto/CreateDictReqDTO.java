package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class CreateDictReqDTO {
    private String dictName;
    private String dictCode;
    private String remark;
}
