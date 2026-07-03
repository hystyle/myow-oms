package com.myow.customer.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.customer.infrastructure.persistence.mapper.CustomerAttachmentMapper;
import com.myow.customer.infrastructure.persistence.po.CustomerAttachmentDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CustomerAttachmentRepository extends ServiceImpl<CustomerAttachmentMapper, CustomerAttachmentDO> {

    public Page<CustomerAttachmentDO> selectPage(Long customerId, String attachmentType, String auditStatus, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<CustomerAttachmentDO>lambdaQuery()
                .eq(CustomerAttachmentDO::getCustomerId, customerId)
                .eq(CustomerAttachmentDO::getDeletedFlag, false)
                .eq(StringUtils.hasText(attachmentType), CustomerAttachmentDO::getAttachmentType, attachmentType)
                .eq(StringUtils.hasText(auditStatus), CustomerAttachmentDO::getAuditStatus, auditStatus)
                .orderByAsc(CustomerAttachmentDO::getExpireDate)
                .orderByDesc(CustomerAttachmentDO::getCreateTime));
    }
}
