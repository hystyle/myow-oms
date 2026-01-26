package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class UpdateI18nMessageReqDTO {
    private Long id;
    private String keyCode;
    private String lang;
    private String message;
    private Short status;
}
