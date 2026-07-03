package com.myow.overseas.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.overseas.infrastructure.persistence.mapper.WarehouseMapper;
import com.myow.overseas.infrastructure.persistence.po.WarehouseDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class WarehouseRepository extends ServiceImpl<WarehouseMapper, WarehouseDO> {

    public Page<WarehouseDO> selectPage(Long tenantId, String keyword, String countryCode, String status,
                                        Long serviceProviderCustomerId, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<WarehouseDO>lambdaQuery()
                .eq(WarehouseDO::getTenantId, tenantId)
                .eq(WarehouseDO::getDeletedFlag, false)
                .eq(StringUtils.hasText(countryCode), WarehouseDO::getCountryCode, countryCode)
                .eq(StringUtils.hasText(status), WarehouseDO::getStatus, status)
                .eq(serviceProviderCustomerId != null, WarehouseDO::getServiceProviderCustomerId, serviceProviderCustomerId)
                .and(StringUtils.hasText(keyword), wrapper -> wrapper
                        .like(WarehouseDO::getWarehouseCode, keyword)
                        .or().like(WarehouseDO::getWarehouseName, keyword)
                        .or().like(WarehouseDO::getExternalWarehouseCode, keyword))
                .orderByDesc(WarehouseDO::getCreateTime));
    }

    public boolean existsByCode(Long tenantId, String warehouseCode, Long excludeWarehouseId) {
        return lambdaQuery()
                .eq(WarehouseDO::getTenantId, tenantId)
                .eq(WarehouseDO::getWarehouseCode, warehouseCode)
                .eq(WarehouseDO::getDeletedFlag, false)
                .ne(excludeWarehouseId != null, WarehouseDO::getWarehouseId, excludeWarehouseId)
                .exists();
    }
}
