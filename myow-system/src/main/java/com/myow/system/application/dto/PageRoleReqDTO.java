package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class PageRoleReqDTO extends PageParam {
    // Add any specific search criteria for roles here
    private String roleName;
    private String roleCode;
    private String status;
}
