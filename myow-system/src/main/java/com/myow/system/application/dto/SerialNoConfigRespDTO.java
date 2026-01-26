package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author gemini
 */
@Getter
@Setter
public class SerialNoConfigRespDTO {
    private Integer serialNumberId;
    private String businessName;
    private String format;
    private String ruleType;
    private String remark;
    private Long initNumber;
    private Integer stepRandomRange;
    private Long lastNumber;
    private LocalDateTime lastTime;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
}
