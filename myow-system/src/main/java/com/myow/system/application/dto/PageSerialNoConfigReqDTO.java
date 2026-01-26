package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class PageSerialNoConfigReqDTO extends PageParam {
    private String businessName;
    private String ruleType;
}
