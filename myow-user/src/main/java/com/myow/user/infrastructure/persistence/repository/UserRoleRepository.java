package com.myow.user.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.user.application.vo.UserRoleInfoVO;
import com.myow.user.infrastructure.persistence.mapper.UserRoleMapper;
import com.myow.user.infrastructure.persistence.mapper.UserRolePermissionMapper;
import com.myow.user.infrastructure.persistence.po.UserRoleDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRoleRepository extends ServiceImpl<UserRoleMapper, UserRoleDO> {

    private final UserRolePermissionMapper userRolePermissionMapper;

    public void deleteByUserId(Long userId) {
        remove(Wrappers.<UserRoleDO>lambdaQuery().eq(UserRoleDO::getUserId, userId));
    }

    public List<UserRoleInfoVO> listRoleInfoByUserIds(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userRolePermissionMapper.selectRoleInfoByUserIds(userIds);
    }
}
