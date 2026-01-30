package com.myow.system.application.service;

import com.myow.system.application.dto.MenuRespDTO;
import com.myow.system.application.dto.RoleRespDTO;
import com.myow.system.application.dto.UserRespDTO;
import com.myow.system.application.vo.UserPermission;
import com.myow.system.domain.consts.SystemCacheConst;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: yss
 * @date: 2026-01-28 22:11
 * @description: 登录缓存服务
 */
@Service
@RequiredArgsConstructor
public class LoginCacheService {

    private final UserService userService;
    private final RoleService roleService;
    private final MenuService menuService;

    /**
     * 获取用户的权限（包含 角色列表、权限列表）
     */
    @Cacheable(SystemCacheConst.Login.USER_PERMISSION)
    public UserPermission getUserPermission(Long userId) {
        if (null == userId) {
            return null;
        }

        return this.loadUserPermission(userId);
    }

    /**
     * 获取用户的权限（包含 角色列表、权限列表）
     */
    @CachePut(SystemCacheConst.Login.USER_PERMISSION)
    public UserPermission loadUserPermission(Long userId) {
        UserPermission userPermission = new UserPermission();
        userPermission.setPermissionList(new ArrayList<>());
        userPermission.setRoleList(new ArrayList<>());

        // 角色列表
        List<RoleRespDTO> roleVOList = roleService.getRoleByUserId(userId);
        userPermission.getRoleList().addAll(roleVOList.stream().map(RoleRespDTO::getRoleCode).collect(Collectors.toSet()));

        // 前端菜单和功能点清单
        UserRespDTO user = userService.getUser(userId);
        List<MenuRespDTO> menuAndPointsList = menuService.getMenuList(roleVOList.stream().map(RoleRespDTO::getRoleId).collect(Collectors.toList()), user.getAdminFlag());

        // 权限列表
        HashSet<String> permissionSet = new HashSet<>();
        for (MenuRespDTO menu : menuAndPointsList) {
            String perms = menu.getApiPerms();
            if (StringUtils.isEmpty(perms)) {
                continue;
            }

            String[] split = perms.split(",");
            permissionSet.addAll(Arrays.asList(split));
        }
        userPermission.getPermissionList().addAll(permissionSet);

        return userPermission;
    }

    /**
     * 清除用户权限
     */
    @CacheEvict(value = SystemCacheConst.Login.USER_PERMISSION)
    public void clearUserPermission(Long userId) {

    }

    /**
     * 清除用户登录信息
     */
    @CacheEvict(value = SystemCacheConst.Login.LOGIN_USER)
    public void clearUserLoginInfo(Long userId) {

    }
}
