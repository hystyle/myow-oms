package com.myow.customer.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.customer.infrastructure.persistence.mapper.CustomerMapper;
import com.myow.customer.infrastructure.persistence.po.CustomerDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CustomerRepository extends ServiceImpl<CustomerMapper, CustomerDO> {

    public Page<CustomerDO> selectPage(Long tenantId, String keyword, String status, Long salesOwnerId,
                                       String poolStatus, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<CustomerDO>lambdaQuery()
                .eq(CustomerDO::getTenantId, tenantId)
                .eq(CustomerDO::getDeletedFlag, false)
                .eq(StringUtils.hasText(status), CustomerDO::getStatus, status)
                .eq(salesOwnerId != null, CustomerDO::getSalesOwnerId, salesOwnerId)
                .eq(StringUtils.hasText(poolStatus), CustomerDO::getPoolStatus, poolStatus)
                .and(StringUtils.hasText(keyword), wrapper -> wrapper
                        .like(CustomerDO::getCustomerCode, keyword)
                        .or().like(CustomerDO::getCustomerName, keyword)
                        .or().like(CustomerDO::getBizLicenseNo, keyword)
                        .or().like(CustomerDO::getTaxNo, keyword))
                .orderByDesc(CustomerDO::getCreateTime));
    }

    public boolean existsByCode(Long tenantId, String customerCode, Long excludeCustomerId) {
        return lambdaQuery()
                .eq(CustomerDO::getTenantId, tenantId)
                .eq(CustomerDO::getCustomerCode, customerCode)
                .eq(CustomerDO::getDeletedFlag, false)
                .ne(excludeCustomerId != null, CustomerDO::getCustomerId, excludeCustomerId)
                .exists();
    }
}
