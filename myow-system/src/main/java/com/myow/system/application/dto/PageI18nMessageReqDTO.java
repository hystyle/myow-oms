package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class PageI18nMessageReqDTO extends PageParam {
    private String keyCode;
    private String lang;
    private String message;
    private Short status;
}
