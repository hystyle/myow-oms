package com.myow.system.application.vo;

import lombok.Data;

/**
 * @author: yss
 * @date: 2026-01-28 20:55
 * @description: 角色的员工
 */
@Data
public class RoleUserVO {
    private Long roleId;

    private Long userId;

    private String roleName;
}
