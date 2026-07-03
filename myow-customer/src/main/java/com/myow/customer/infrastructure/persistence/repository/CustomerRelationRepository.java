package com.myow.customer.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.customer.infrastructure.persistence.mapper.CustomerRelationMapper;
import com.myow.customer.infrastructure.persistence.po.CustomerRelationDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CustomerRelationRepository extends ServiceImpl<CustomerRelationMapper, CustomerRelationDO> {

    public Page<CustomerRelationDO> selectPage(Long customerId, String relationType, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<CustomerRelationDO>lambdaQuery()
                .eq(CustomerRelationDO::getDeletedFlag, false)
                .eq(StringUtils.hasText(relationType), CustomerRelationDO::getRelationType, relationType)
                .and(wrapper -> wrapper
                        .eq(CustomerRelationDO::getParentCustomerId, customerId)
                        .or()
                        .eq(CustomerRelationDO::getChildCustomerId, customerId))
                .orderByDesc(CustomerRelationDO::getCreateTime));
    }

    public boolean existsRelation(Long tenantId, Long parentCustomerId, Long childCustomerId, String relationType, Long excludeRelationId) {
        return lambdaQuery()
                .eq(CustomerRelationDO::getTenantId, tenantId)
                .eq(CustomerRelationDO::getParentCustomerId, parentCustomerId)
                .eq(CustomerRelationDO::getChildCustomerId, childCustomerId)
                .eq(CustomerRelationDO::getRelationType, relationType)
                .eq(CustomerRelationDO::getDeletedFlag, false)
                .ne(excludeRelationId != null, CustomerRelationDO::getRelationId, excludeRelationId)
                .exists();
    }
}
