package com.myow.customer.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.customer.infrastructure.persistence.mapper.CustomerKycMapper;
import com.myow.customer.infrastructure.persistence.po.CustomerKycDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CustomerKycRepository extends ServiceImpl<CustomerKycMapper, CustomerKycDO> {

    public Page<CustomerKycDO> selectPage(Long customerId, String kycType, String auditStatus, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<CustomerKycDO>lambdaQuery()
                .eq(CustomerKycDO::getCustomerId, customerId)
                .eq(CustomerKycDO::getDeletedFlag, false)
                .eq(StringUtils.hasText(kycType), CustomerKycDO::getKycType, kycType)
                .eq(StringUtils.hasText(auditStatus), CustomerKycDO::getAuditStatus, auditStatus)
                .orderByDesc(CustomerKycDO::getCreateTime));
    }
}
