package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class PageDeptReqDTO extends PageParam {
    // Add any specific search criteria for departments here
    private String deptName;
    private Long parentId;
}
