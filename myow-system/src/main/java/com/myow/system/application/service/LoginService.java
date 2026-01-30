package com.myow.system.application.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.lang.UUID;
import com.myow.common.constant.StringConst;
import com.myow.system.application.vo.UserPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author: yss
 * @date: 2026-01-28 21:21
 * @description: 登录服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements StpInterface {

    private final LoginCacheService loginCacheService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = this.getUserIdByLoginId((String) loginId);
        if (userId == null) {
            return Collections.emptyList();
        }

        UserPermission userPermission = loginCacheService.getUserPermission(userId);
        return userPermission.getPermissionList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = this.getUserIdByLoginId((String) loginId);
        if (userId == null) {
            return Collections.emptyList();
        }

        UserPermission userPermission = loginCacheService.getUserPermission(userId);
        return userPermission.getRoleList();
    }

    private Long getUserIdByLoginId(String loginId) {
        if (loginId == null) {
            return null;
        }

        try {
            String userId = loginId.split("_")[1];
            return Long.valueOf(userId);
        } catch (Exception e) {
            log.error("loginId parse error , loginId : {}", loginId, e);
            return null;
        }
    }

    /**
     * token 生成
     */
    private static String generateToken(Long userId) {
        return UUID.randomUUID().toString().replace("-", "") + StringConst.COLON + userId;
    }

    public void clearLoginEmployeeCache(Long userId) {
        loginCacheService.clearUserPermission(userId);
        loginCacheService.clearUserLoginInfo(userId);
    }

}
