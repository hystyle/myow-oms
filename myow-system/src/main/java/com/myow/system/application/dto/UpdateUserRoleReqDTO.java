package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class UpdateUserRoleReqDTO {
    /**
     * Original User ID (for identifying the record to update)
     */
    private Long originalUserId;

    /**
     * Original Role ID (for identifying the record to update)
     */
    private Long originalRoleId;

    /**
     * New User ID
     */
    private Long userId;

    /**
     * New Role ID
     */
    private Long roleId;
}
