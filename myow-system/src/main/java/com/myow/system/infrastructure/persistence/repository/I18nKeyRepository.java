package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageI18nKeyReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.I18nKeyMapper;
import com.myow.system.infrastructure.persistence.po.I18nKeyDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author yss
 */
@Repository
public class I18nKeyRepository extends ServiceImpl<I18nKeyMapper, I18nKeyDO> {

    public Page<I18nKeyDO> selectPage(PageI18nKeyReqDTO reqDTO) {
        Page<I18nKeyDO> page = MyPageUtil.convert2PageQuery(reqDTO, I18nKeyDO.class);
        LambdaQueryWrapper<I18nKeyDO> queryWrapper = Wrappers.lambdaQuery();

        if (StringUtils.hasText(reqDTO.getKeyCode())) {
            queryWrapper.like(I18nKeyDO::getKeyCode, reqDTO.getKeyCode());
        }
        if (StringUtils.hasText(reqDTO.getBizDomain())) {
            queryWrapper.eq(I18nKeyDO::getBizDomain, reqDTO.getBizDomain());
        }
        if (reqDTO.getStatus() != null) {
            queryWrapper.eq(I18nKeyDO::getStatus, reqDTO.getStatus());
        }

        return this.page(page, queryWrapper);
    }
}