package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class CreateI18nKeyReqDTO {
    private String keyCode;
    private String bizDomain;
    private String description;
    private Short status;
}
