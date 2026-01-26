package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author gemini
 */
@Getter
@Setter
public class PageSerialNoRecordReqDTO extends PageParam {
    private Integer serialNumberId;
    private LocalDate recordDate;
}
