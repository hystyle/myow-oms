package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class CreateUserRoleReqDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
