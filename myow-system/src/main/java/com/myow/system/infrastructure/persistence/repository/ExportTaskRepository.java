package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.mapper.ExportTaskMapper;
import com.myow.system.infrastructure.persistence.po.ExportTaskDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ExportTaskRepository extends ServiceImpl<ExportTaskMapper, ExportTaskDO> {
    public Page<ExportTaskDO> selectPage(String keyword, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<ExportTaskDO>lambdaQuery()
                .like(StringUtils.hasText(keyword), ExportTaskDO::getModuleName, keyword)
                .orderByDesc(ExportTaskDO::getCreateTime));
    }
}
