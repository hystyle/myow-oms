package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class UpdateRoleMenuReqDTO {
    /**
     * Original Role ID (for identifying the record to update)
     */
    private Long originalRoleId;

    /**
     * Original Menu ID (for identifying the record to update)
     */
    private Long originalMenuId;

    /**
     * New Role ID
     */
    private Long roleId;

    /**
     * New Menu ID
     */
    private Long menuId;
}
