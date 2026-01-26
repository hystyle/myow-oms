package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageRoleMenuReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.RoleMenuMapper;
import com.myow.system.infrastructure.persistence.po.RoleMenuDO;
import org.springframework.stereotype.Repository;

/**
 * @author gemini
 */
@Repository
public class RoleMenuRepository extends ServiceImpl<RoleMenuMapper, RoleMenuDO> {

    public Page<RoleMenuDO> selectPage(PageRoleMenuReqDTO reqDTO) {
        Page<RoleMenuDO> page = MyPageUtil.convert2PageQuery(reqDTO, RoleMenuDO.class);
        LambdaQueryWrapper<RoleMenuDO> queryWrapper = Wrappers.lambdaQuery();

        if (reqDTO.getRoleId() != null) {
            queryWrapper.eq(RoleMenuDO::getRoleId, reqDTO.getRoleId());
        }
        if (reqDTO.getMenuId() != null) {
            queryWrapper.eq(RoleMenuDO::getMenuId, reqDTO.getMenuId());
        }

        return this.page(page, queryWrapper);
    }

    public RoleMenuDO getByCompositeKey(Long roleId, Long menuId) {
        return this.getOne(Wrappers.<RoleMenuDO>lambdaQuery()
                .eq(RoleMenuDO::getRoleId, roleId)
                .eq(RoleMenuDO::getMenuId, menuId));
    }

    public boolean removeByCompositeKey(Long roleId, Long menuId) {
        return this.remove(Wrappers.<RoleMenuDO>lambdaQuery()
                .eq(RoleMenuDO::getRoleId, roleId)
                .eq(RoleMenuDO::getMenuId, menuId));
    }
}