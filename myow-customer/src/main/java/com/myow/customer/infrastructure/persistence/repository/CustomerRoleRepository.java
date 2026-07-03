package com.myow.customer.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.customer.infrastructure.persistence.mapper.CustomerRoleMapper;
import com.myow.customer.infrastructure.persistence.po.CustomerRoleDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CustomerRoleRepository extends ServiceImpl<CustomerRoleMapper, CustomerRoleDO> {

    public Page<CustomerRoleDO> selectPage(Long customerId, String roleType, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<CustomerRoleDO>lambdaQuery()
                .eq(CustomerRoleDO::getCustomerId, customerId)
                .eq(CustomerRoleDO::getDeletedFlag, false)
                .eq(StringUtils.hasText(roleType), CustomerRoleDO::getRoleType, roleType)
                .orderByAsc(CustomerRoleDO::getRoleType)
                .orderByDesc(CustomerRoleDO::getCreateTime));
    }

    public boolean existsByRoleType(Long tenantId, Long customerId, String roleType, Long excludeRoleId) {
        return lambdaQuery()
                .eq(CustomerRoleDO::getTenantId, tenantId)
                .eq(CustomerRoleDO::getCustomerId, customerId)
                .eq(CustomerRoleDO::getRoleType, roleType)
                .eq(CustomerRoleDO::getDeletedFlag, false)
                .ne(excludeRoleId != null, CustomerRoleDO::getCustomerRoleId, excludeRoleId)
                .exists();
    }

    public boolean hasActiveRole(Long tenantId, Long customerId, String roleType) {
        return lambdaQuery()
                .eq(CustomerRoleDO::getTenantId, tenantId)
                .eq(CustomerRoleDO::getCustomerId, customerId)
                .eq(CustomerRoleDO::getRoleType, roleType)
                .eq(CustomerRoleDO::getRoleStatus, "ACTIVE")
                .eq(CustomerRoleDO::getDeletedFlag, false)
                .exists();
    }

    public java.util.List<CustomerRoleDO> listActiveByRole(Long tenantId, String roleType, long limit) {
        return lambdaQuery()
                .eq(CustomerRoleDO::getTenantId, tenantId)
                .eq(CustomerRoleDO::getRoleType, roleType)
                .eq(CustomerRoleDO::getRoleStatus, "ACTIVE")
                .eq(CustomerRoleDO::getDeletedFlag, false)
                .orderByDesc(CustomerRoleDO::getCreateTime)
                .last("LIMIT " + limit)
                .list();
    }
}
