package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.mapper.JobLogMapper;
import com.myow.system.infrastructure.persistence.po.JobLogDO;
import org.springframework.stereotype.Repository;

@Repository
public class JobLogRepository extends ServiceImpl<JobLogMapper, JobLogDO> {
    public Page<JobLogDO> selectPage(long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize));
    }
}
