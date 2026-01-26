package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class CreateRoleMenuReqDTO {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
