package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class UpdateI18nKeyReqDTO {
    private Long id;
    private String keyCode;
    private String bizDomain;
    private String description;
    private Short status;
}
