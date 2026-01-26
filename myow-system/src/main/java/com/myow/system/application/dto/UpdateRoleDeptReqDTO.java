package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class UpdateRoleDeptReqDTO {
    /**
     * Original Role ID (for identifying the record to update)
     */
    private Long originalRoleId;

    /**
     * Original Dept ID (for identifying the record to update)
     */
    private Long originalDeptId;

    /**
     * New Role ID
     */
    private Long roleId;

    /**
     * New Dept ID
     */
    private Long deptId;
}
