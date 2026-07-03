package com.myow.customer.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.customer.infrastructure.persistence.mapper.CustomerContactMapper;
import com.myow.customer.infrastructure.persistence.po.CustomerContactDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CustomerContactRepository extends ServiceImpl<CustomerContactMapper, CustomerContactDO> {

    public Page<CustomerContactDO> selectPage(Long customerId, String keyword, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<CustomerContactDO>lambdaQuery()
                .eq(CustomerContactDO::getCustomerId, customerId)
                .eq(CustomerContactDO::getDeletedFlag, false)
                .and(StringUtils.hasText(keyword), wrapper -> wrapper
                        .like(CustomerContactDO::getContactName, keyword)
                        .or().like(CustomerContactDO::getPhone, keyword)
                        .or().like(CustomerContactDO::getEmail, keyword))
                .orderByDesc(CustomerContactDO::getPrimary)
                .orderByDesc(CustomerContactDO::getCreateTime));
    }

    public void clearPrimary(Long tenantId, Long customerId) {
        lambdaUpdate()
                .eq(CustomerContactDO::getTenantId, tenantId)
                .eq(CustomerContactDO::getCustomerId, customerId)
                .set(CustomerContactDO::getPrimary, false)
                .update();
    }
}
