package com.myow.user.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class PageUserRoleReqDTO extends PageParam {
    private Long userId;
    private Long roleId;
}
