package com.myow.overseas.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.overseas.infrastructure.persistence.mapper.LogisticsChannelMapper;
import com.myow.overseas.infrastructure.persistence.po.LogisticsChannelDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class LogisticsChannelRepository extends ServiceImpl<LogisticsChannelMapper, LogisticsChannelDO> {

    public Page<LogisticsChannelDO> selectPage(Long tenantId, String keyword, String labelSource, String status,
                                               Long carrierCustomerId, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<LogisticsChannelDO>lambdaQuery()
                .eq(LogisticsChannelDO::getTenantId, tenantId)
                .eq(LogisticsChannelDO::getDeletedFlag, false)
                .eq(StringUtils.hasText(labelSource), LogisticsChannelDO::getLabelSource, labelSource)
                .eq(StringUtils.hasText(status), LogisticsChannelDO::getStatus, status)
                .eq(carrierCustomerId != null, LogisticsChannelDO::getCarrierCustomerId, carrierCustomerId)
                .and(StringUtils.hasText(keyword), wrapper -> wrapper
                        .like(LogisticsChannelDO::getChannelCode, keyword)
                        .or().like(LogisticsChannelDO::getChannelName, keyword))
                .orderByDesc(LogisticsChannelDO::getCreateTime));
    }

    public boolean existsByCode(Long tenantId, String channelCode, Long excludeChannelId) {
        return lambdaQuery()
                .eq(LogisticsChannelDO::getTenantId, tenantId)
                .eq(LogisticsChannelDO::getChannelCode, channelCode)
                .eq(LogisticsChannelDO::getDeletedFlag, false)
                .ne(excludeChannelId != null, LogisticsChannelDO::getChannelId, excludeChannelId)
                .exists();
    }
}
