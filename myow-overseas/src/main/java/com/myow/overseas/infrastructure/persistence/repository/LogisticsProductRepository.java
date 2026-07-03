package com.myow.overseas.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.overseas.infrastructure.persistence.mapper.LogisticsProductMapper;
import com.myow.overseas.infrastructure.persistence.po.LogisticsProductDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class LogisticsProductRepository extends ServiceImpl<LogisticsProductMapper, LogisticsProductDO> {

    public Page<LogisticsProductDO> selectPage(Long tenantId, String keyword, String productType, String status,
                                               Long carrierCustomerId, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<LogisticsProductDO>lambdaQuery()
                .eq(LogisticsProductDO::getTenantId, tenantId)
                .eq(LogisticsProductDO::getDeletedFlag, false)
                .eq(StringUtils.hasText(productType), LogisticsProductDO::getProductType, productType)
                .eq(StringUtils.hasText(status), LogisticsProductDO::getStatus, status)
                .eq(carrierCustomerId != null, LogisticsProductDO::getCarrierCustomerId, carrierCustomerId)
                .and(StringUtils.hasText(keyword), wrapper -> wrapper
                        .like(LogisticsProductDO::getProductCode, keyword)
                        .or().like(LogisticsProductDO::getProductName, keyword))
                .orderByDesc(LogisticsProductDO::getCreateTime));
    }

    public boolean existsByCode(Long tenantId, String productCode, Long excludeProductId) {
        return lambdaQuery()
                .eq(LogisticsProductDO::getTenantId, tenantId)
                .eq(LogisticsProductDO::getProductCode, productCode)
                .eq(LogisticsProductDO::getDeletedFlag, false)
                .ne(excludeProductId != null, LogisticsProductDO::getProductId, excludeProductId)
                .exists();
    }
}
