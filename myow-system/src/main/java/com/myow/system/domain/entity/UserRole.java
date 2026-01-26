package com.myow.system.domain.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class UserRole {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
