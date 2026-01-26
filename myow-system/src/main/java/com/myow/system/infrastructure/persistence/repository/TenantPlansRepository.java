package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageTenantPlansReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.TenantPlansMapper;
import com.myow.system.infrastructure.persistence.po.TenantPlansDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author yss
 */
@Repository
public class TenantPlansRepository extends ServiceImpl<TenantPlansMapper, TenantPlansDO> {

    public Page<TenantPlansDO> selectPage(PageTenantPlansReqDTO reqDTO) {
        Page<TenantPlansDO> page = MyPageUtil.convert2PageQuery(reqDTO, TenantPlansDO.class);
        LambdaQueryWrapper<TenantPlansDO> queryWrapper = Wrappers.lambdaQuery();

        if (StringUtils.hasText(reqDTO.getPlansName())) {
            queryWrapper.like(TenantPlansDO::getPlansName, reqDTO.getPlansName());
        }
        if (StringUtils.hasText(reqDTO.getPlansCode())) {
            queryWrapper.eq(TenantPlansDO::getPlansCode, reqDTO.getPlansCode());
        }
        if (StringUtils.hasText(reqDTO.getStatus())) {
            queryWrapper.eq(TenantPlansDO::getStatus, reqDTO.getStatus());
        }

        return this.page(page, queryWrapper);
    }
}