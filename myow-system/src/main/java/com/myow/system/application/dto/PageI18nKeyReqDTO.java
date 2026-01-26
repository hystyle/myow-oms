package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class PageI18nKeyReqDTO extends PageParam {
    private String keyCode;
    private String bizDomain;
    private Short status;
}
