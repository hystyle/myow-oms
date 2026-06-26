package com.myow.user.application.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.util.StrUtil;
import com.myow.common.constant.StringConst;
import com.myow.user.infrastructure.persistence.po.MenuDO;
import com.myow.user.infrastructure.persistence.po.RoleDO;
import com.myow.user.infrastructure.persistence.po.TenantUserDO;
import com.myow.user.infrastructure.persistence.repository.TenantUserRepository;
import com.myow.user.infrastructure.persistence.repository.UserPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPermissionService implements StpInterface {

    private final TenantUserRepository tenantUserRepository;
    private final UserPermissionRepository userPermissionRepository;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = parseUserId(loginId);
        TenantUserDO user = getUser(userId);
        if (user == null) {
            return List.of();
        }

        List<RoleDO> roleList = userPermissionRepository.listRolesByUserId(userId);
        List<Long> roleIdList = roleList.stream()
                .map(RoleDO::getRoleId)
                .filter(Objects::nonNull)
                .toList();

        return userPermissionRepository.listMenusByRoleIds(roleIdList, user.getAdminFlag()).stream()
                .map(MenuDO::getApiPerms)
                .filter(StrUtil::isNotBlank)
                .flatMap(apiPerms -> Arrays.stream(apiPerms.split(",")))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = parseUserId(loginId);
        if (userId == null) {
            return List.of();
        }

        return userPermissionRepository.listRolesByUserId(userId).stream()
                .map(RoleDO::getRoleCode)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
    }

    private TenantUserDO getUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return tenantUserRepository.getById(userId);
    }

    private Long parseUserId(Object loginId) {
        if (loginId == null) {
            return null;
        }

        try {
            String loginIdText = String.valueOf(loginId);
            int separatorIndex = loginIdText.indexOf(StringConst.COLON);
            String userId = separatorIndex >= 0 ? loginIdText.substring(separatorIndex + 1) : loginIdText;
            return Long.valueOf(userId);
        } catch (Exception ignored) {
            return null;
        }
    }
}
