package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class PagePositionReqDTO extends PageParam {
    // Add any specific search criteria for positions here
    private String positionName;
    private String positionCode;
    private Long deptId;
    private String status;
}
