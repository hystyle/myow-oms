package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class PageDictDataReqDTO extends PageParam {
    private Long dictId;
    private String dataValue;
    private String dataLabel;
    private Boolean disabledFlag;
}
