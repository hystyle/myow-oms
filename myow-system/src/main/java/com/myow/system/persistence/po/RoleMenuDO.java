package com.myow.system.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色和菜单关联表
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role_menu")
public class RoleMenuDO {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
