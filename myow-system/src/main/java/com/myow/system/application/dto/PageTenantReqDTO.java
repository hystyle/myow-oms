package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class PageTenantReqDTO extends PageParam {
    // Add any specific search criteria for tenants here
    private String tenantCode;
    private String name;
    private String status;
}
