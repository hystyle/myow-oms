package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageMenuReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.MenuMapper;
import com.myow.system.infrastructure.persistence.po.MenuDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author yss
 */
@Repository
public class MenuRepository extends ServiceImpl<MenuMapper, MenuDO> {

    public Page<MenuDO> selectPage(PageMenuReqDTO reqDTO) {
        Page<MenuDO> page = MyPageUtil.convert2PageQuery(reqDTO, MenuDO.class);
        LambdaQueryWrapper<MenuDO> queryWrapper = Wrappers.lambdaQuery();

        if (StringUtils.hasText(reqDTO.getMenuName())) {
            queryWrapper.like(MenuDO::getMenuName, reqDTO.getMenuName());
        }
        if (reqDTO.getParentId() != null) {
            queryWrapper.eq(MenuDO::getParentId, reqDTO.getParentId());
        }
        if (StringUtils.hasText(reqDTO.getMenuType())) {
            queryWrapper.eq(MenuDO::getMenuType, reqDTO.getMenuType());
        }
        if (StringUtils.hasText(reqDTO.getStatus())) {
            queryWrapper.eq(MenuDO::getStatus, reqDTO.getStatus());
        }

        return this.page(page, queryWrapper);
    }
}