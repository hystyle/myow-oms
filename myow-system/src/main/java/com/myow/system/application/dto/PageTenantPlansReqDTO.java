package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class PageTenantPlansReqDTO extends PageParam {
    private String plansName;
    private String plansCode;
    private String status;
}
