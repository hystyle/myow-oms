package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.mapper.MessageTemplateMapper;
import com.myow.system.infrastructure.persistence.po.MessageTemplateDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class MessageTemplateRepository extends ServiceImpl<MessageTemplateMapper, MessageTemplateDO> {
    public Page<MessageTemplateDO> selectPage(String keyword, Integer status, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<MessageTemplateDO>lambdaQuery()
                .eq(status != null, MessageTemplateDO::getStatus, status)
                .and(StringUtils.hasText(keyword), wrapper -> wrapper.like(MessageTemplateDO::getTemplateCode, keyword).or().like(MessageTemplateDO::getTitle, keyword))
                .orderByDesc(MessageTemplateDO::getCreateTime));
    }
}
