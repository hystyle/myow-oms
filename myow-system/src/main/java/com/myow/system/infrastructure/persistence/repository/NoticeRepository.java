package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.mapper.NoticeMapper;
import com.myow.system.infrastructure.persistence.po.NoticeDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class NoticeRepository extends ServiceImpl<NoticeMapper, NoticeDO> {
    public Page<NoticeDO> selectPage(String keyword, Integer status, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<NoticeDO>lambdaQuery()
                .eq(NoticeDO::getDeletedFlag, false)
                .eq(status != null, NoticeDO::getStatus, status)
                .like(StringUtils.hasText(keyword), NoticeDO::getTitle, keyword)
                .orderByDesc(NoticeDO::getCreateTime));
    }
}
