package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageI18nMessageReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.I18nMessageMapper;
import com.myow.system.infrastructure.persistence.po.I18nMessageDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author yss
 */
@Repository
public class I18nMessageRepository extends ServiceImpl<I18nMessageMapper, I18nMessageDO> {

    public Page<I18nMessageDO> selectPage(PageI18nMessageReqDTO reqDTO) {
        Page<I18nMessageDO> page = MyPageUtil.convert2PageQuery(reqDTO, I18nMessageDO.class);
        LambdaQueryWrapper<I18nMessageDO> queryWrapper = Wrappers.lambdaQuery();

        if (StringUtils.hasText(reqDTO.getKeyCode())) {
            queryWrapper.eq(I18nMessageDO::getKeyCode, reqDTO.getKeyCode());
        }
        if (StringUtils.hasText(reqDTO.getLang())) {
            queryWrapper.eq(I18nMessageDO::getLang, reqDTO.getLang());
        }
        if (StringUtils.hasText(reqDTO.getMessage())) {
            queryWrapper.like(I18nMessageDO::getMessage, reqDTO.getMessage());
        }
        if (reqDTO.getStatus() != null) {
            queryWrapper.eq(I18nMessageDO::getStatus, reqDTO.getStatus());
        }

        return this.page(page, queryWrapper);
    }
}