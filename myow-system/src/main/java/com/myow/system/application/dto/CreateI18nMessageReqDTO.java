package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class CreateI18nMessageReqDTO {
    private String keyCode;
    private String lang;
    private String message;
    private Short status;
}
