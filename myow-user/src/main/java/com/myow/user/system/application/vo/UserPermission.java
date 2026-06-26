package com.myow.user.system.application.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author: yss
 * @date: 2026-01-28 21:26
 * @description: sa-token 所需的权限信息
 */
@Data
public class UserPermission implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 权限列表
     */
    private List<String> permissionList;

    /**
     * 角色列表
     */
    private List<String> roleList;
}
