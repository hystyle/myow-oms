package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.mapper.SiteConfigMapper;
import com.myow.system.infrastructure.persistence.po.SiteConfigDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class SiteConfigRepository extends ServiceImpl<SiteConfigMapper, SiteConfigDO> {
    public Page<SiteConfigDO> selectPage(String keyword, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<SiteConfigDO>lambdaQuery()
                .and(StringUtils.hasText(keyword), wrapper -> wrapper.like(SiteConfigDO::getSiteCode, keyword).or().like(SiteConfigDO::getConfigKey, keyword))
                .orderByDesc(SiteConfigDO::getCreateTime));
    }

    public List<SiteConfigDO> listBySiteCode(String siteCode) {
        return list(Wrappers.<SiteConfigDO>lambdaQuery().eq(SiteConfigDO::getSiteCode, siteCode));
    }
}
