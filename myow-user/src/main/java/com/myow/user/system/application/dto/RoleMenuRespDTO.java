package com.myow.user.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class RoleMenuRespDTO {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
