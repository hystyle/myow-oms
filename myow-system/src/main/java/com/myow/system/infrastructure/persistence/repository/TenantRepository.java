package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageTenantReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.TenantMapper;
import com.myow.system.infrastructure.persistence.po.TenantDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author yss
 */
@Repository
public class TenantRepository extends ServiceImpl<TenantMapper, TenantDO> {

    public Page<TenantDO> selectPage(PageTenantReqDTO reqDTO) {
        Page<TenantDO> page = MyPageUtil.convert2PageQuery(reqDTO, TenantDO.class);
        LambdaQueryWrapper<TenantDO> queryWrapper = Wrappers.lambdaQuery();

        if (StringUtils.hasText(reqDTO.getName())) {
            queryWrapper.like(TenantDO::getName, reqDTO.getName());
        }
        if (StringUtils.hasText(reqDTO.getTenantCode())) {
            queryWrapper.eq(TenantDO::getTenantCode, reqDTO.getTenantCode());
        }
        if (StringUtils.hasText(reqDTO.getStatus())) {
            queryWrapper.eq(TenantDO::getStatus, reqDTO.getStatus());
        }

        return this.page(page, queryWrapper);
    }
}