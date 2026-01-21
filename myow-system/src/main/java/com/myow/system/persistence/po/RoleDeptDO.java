package com.myow.system.persistence.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色和部门关联表
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role_dept")
public class RoleDeptDO {

    /**
     * 角色ID
     */
    @TableId("role_id")
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;
}
