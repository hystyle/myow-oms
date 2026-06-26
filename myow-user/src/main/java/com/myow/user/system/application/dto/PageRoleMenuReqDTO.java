package com.myow.user.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class PageRoleMenuReqDTO extends PageParam {
    private Long roleId;
    private Long menuId;
}
