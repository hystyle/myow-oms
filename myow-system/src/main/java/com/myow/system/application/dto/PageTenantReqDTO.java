package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class PageTenantReqDTO extends PageParam {
    private String tenantCode;
    private String name;
    private String status;
}
