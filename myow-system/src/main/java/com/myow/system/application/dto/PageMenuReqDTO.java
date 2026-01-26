package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class PageMenuReqDTO extends PageParam {
    // Add any specific search criteria for menus here
    private String menuName;
    private Long parentId;
    private String menuType;
    private String status;
}
