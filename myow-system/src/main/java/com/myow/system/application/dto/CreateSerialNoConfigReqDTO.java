package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class CreateSerialNoConfigReqDTO {
    private String businessName;
    private String format;
    private String ruleType;
    private String remark;
    private Long initNumber;
    private Integer stepRandomRange;
}
