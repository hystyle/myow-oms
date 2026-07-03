package com.myow.customer.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.customer.infrastructure.persistence.mapper.CustomerBlacklistMapper;
import com.myow.customer.infrastructure.persistence.po.CustomerBlacklistDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public class CustomerBlacklistRepository extends ServiceImpl<CustomerBlacklistMapper, CustomerBlacklistDO> {

    public Page<CustomerBlacklistDO> selectPage(Long tenantId, String keyword, String targetType, String status, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<CustomerBlacklistDO>lambdaQuery()
                .eq(CustomerBlacklistDO::getTenantId, tenantId)
                .eq(CustomerBlacklistDO::getDeletedFlag, false)
                .eq(StringUtils.hasText(targetType), CustomerBlacklistDO::getTargetType, targetType)
                .eq(StringUtils.hasText(status), CustomerBlacklistDO::getStatus, status)
                .and(StringUtils.hasText(keyword), wrapper -> wrapper
                        .like(CustomerBlacklistDO::getTargetValue, keyword)
                        .or().like(CustomerBlacklistDO::getReason, keyword))
                .orderByDesc(CustomerBlacklistDO::getCreateTime));
    }

    public boolean existsByTarget(Long tenantId, String targetType, String targetValue, Long excludeId) {
        return lambdaQuery()
                .eq(CustomerBlacklistDO::getTenantId, tenantId)
                .eq(CustomerBlacklistDO::getTargetType, targetType)
                .eq(CustomerBlacklistDO::getTargetValue, targetValue)
                .eq(CustomerBlacklistDO::getDeletedFlag, false)
                .ne(excludeId != null, CustomerBlacklistDO::getBlacklistId, excludeId)
                .exists();
    }

    public List<CustomerBlacklistDO> listActiveHits(Long tenantId, Collection<String> targetTypes, Collection<String> targetValues, LocalDateTime now) {
        if (targetTypes.isEmpty() || targetValues.isEmpty()) {
            return List.of();
        }
        return list(Wrappers.<CustomerBlacklistDO>lambdaQuery()
                .eq(CustomerBlacklistDO::getTenantId, tenantId)
                .in(CustomerBlacklistDO::getTargetType, targetTypes)
                .in(CustomerBlacklistDO::getTargetValue, targetValues)
                .eq(CustomerBlacklistDO::getStatus, "ACTIVE")
                .eq(CustomerBlacklistDO::getDeletedFlag, false)
                .and(wrapper -> wrapper.isNull(CustomerBlacklistDO::getEffectiveTime).or().le(CustomerBlacklistDO::getEffectiveTime, now))
                .and(wrapper -> wrapper.isNull(CustomerBlacklistDO::getExpireTime).or().gt(CustomerBlacklistDO::getExpireTime, now)));
    }
}
