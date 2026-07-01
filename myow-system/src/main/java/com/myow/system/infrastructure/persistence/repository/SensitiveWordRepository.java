package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.mapper.SensitiveWordMapper;
import com.myow.system.infrastructure.persistence.po.SensitiveWordDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class SensitiveWordRepository extends ServiceImpl<SensitiveWordMapper, SensitiveWordDO> {
    public Page<SensitiveWordDO> selectPage(String keyword, Integer status, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<SensitiveWordDO>lambdaQuery()
                .eq(status != null, SensitiveWordDO::getStatus, status)
                .and(StringUtils.hasText(keyword), wrapper -> wrapper.like(SensitiveWordDO::getWord, keyword).or().like(SensitiveWordDO::getCategory, keyword))
                .orderByDesc(SensitiveWordDO::getCreateTime));
    }
}
