package com.myow.user.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import com.myow.user.infrastructure.persistence.mapper.UserRolePermissionMapper;
import com.myow.user.infrastructure.persistence.po.MenuDO;
import com.myow.user.infrastructure.persistence.po.RoleDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserPermissionRepository {

    private final UserRolePermissionMapper userRolePermissionMapper;

    public List<RoleDO> listRolesByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return userRolePermissionMapper.selectRolesByUserId(userId);
    }

    public List<MenuDO> listMenusByRoleIds(List<Long> roleIds, Boolean adminFlag) {
        if (!Boolean.TRUE.equals(adminFlag) && CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        return userRolePermissionMapper.selectMenusByRoleIdList(roleIds, adminFlag);
    }
}
