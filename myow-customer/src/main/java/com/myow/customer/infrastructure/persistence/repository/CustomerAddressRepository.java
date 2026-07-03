package com.myow.customer.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.customer.infrastructure.persistence.mapper.CustomerAddressMapper;
import com.myow.customer.infrastructure.persistence.po.CustomerAddressDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CustomerAddressRepository extends ServiceImpl<CustomerAddressMapper, CustomerAddressDO> {

    public Page<CustomerAddressDO> selectPage(Long customerId, String addressType, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<CustomerAddressDO>lambdaQuery()
                .eq(CustomerAddressDO::getCustomerId, customerId)
                .eq(CustomerAddressDO::getDeletedFlag, false)
                .eq(StringUtils.hasText(addressType), CustomerAddressDO::getAddressType, addressType)
                .orderByDesc(CustomerAddressDO::getDefaultAddress)
                .orderByDesc(CustomerAddressDO::getCreateTime));
    }

    public void clearDefault(Long tenantId, Long customerId, String addressType) {
        lambdaUpdate()
                .eq(CustomerAddressDO::getTenantId, tenantId)
                .eq(CustomerAddressDO::getCustomerId, customerId)
                .eq(CustomerAddressDO::getAddressType, addressType)
                .set(CustomerAddressDO::getDefaultAddress, false)
                .update();
    }
}
