package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.mapper.JobMapper;
import com.myow.system.infrastructure.persistence.po.JobDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class JobRepository extends ServiceImpl<JobMapper, JobDO> {
    public Page<JobDO> selectPage(String keyword, Integer status, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<JobDO>lambdaQuery()
                .eq(JobDO::getDeletedFlag, false)
                .eq(status != null, JobDO::getStatus, status)
                .and(StringUtils.hasText(keyword), wrapper -> wrapper.like(JobDO::getJobName, keyword).or().like(JobDO::getJobGroup, keyword))
                .orderByDesc(JobDO::getCreateTime));
    }
}
