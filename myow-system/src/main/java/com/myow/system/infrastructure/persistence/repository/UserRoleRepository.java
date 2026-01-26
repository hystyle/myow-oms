package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageUserRoleReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.UserRoleMapper;
import com.myow.system.infrastructure.persistence.po.UserRoleDO;
import org.springframework.stereotype.Repository;

/**
 * @author gemini
 */
@Repository
public class UserRoleRepository extends ServiceImpl<UserRoleMapper, UserRoleDO> {

    public Page<UserRoleDO> selectPage(PageUserRoleReqDTO reqDTO) {
        Page<UserRoleDO> page = MyPageUtil.convert2PageQuery(reqDTO, UserRoleDO.class);
        LambdaQueryWrapper<UserRoleDO> queryWrapper = Wrappers.lambdaQuery();

        if (reqDTO.getUserId() != null) {
            queryWrapper.eq(UserRoleDO::getUserId, reqDTO.getUserId());
        }
        if (reqDTO.getRoleId() != null) {
            queryWrapper.eq(UserRoleDO::getRoleId, reqDTO.getRoleId());
        }

        return this.page(page, queryWrapper);
    }

    public UserRoleDO getByCompositeKey(Long userId, Long roleId) {
        return this.getOne(Wrappers.<UserRoleDO>lambdaQuery()
                .eq(UserRoleDO::getUserId, userId)
                .eq(UserRoleDO::getRoleId, roleId));
    }

    public boolean removeByCompositeKey(Long userId, Long roleId) {
        return this.remove(Wrappers.<UserRoleDO>lambdaQuery()
                .eq(UserRoleDO::getUserId, userId)
                .eq(UserRoleDO::getRoleId, roleId));
    }
}