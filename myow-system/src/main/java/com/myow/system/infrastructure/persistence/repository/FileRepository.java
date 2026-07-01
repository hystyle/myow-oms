package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.mapper.FileMapper;
import com.myow.system.infrastructure.persistence.po.FileDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class FileRepository extends ServiceImpl<FileMapper, FileDO> {
    public Page<FileDO> selectPage(String keyword, long pageNum, long pageSize) {
        return page(Page.of(pageNum, pageSize), Wrappers.<FileDO>lambdaQuery()
                .eq(FileDO::getDeletedFlag, false)
                .and(StringUtils.hasText(keyword), wrapper -> wrapper.like(FileDO::getOriginalName, keyword).or().like(FileDO::getModuleName, keyword))
                .orderByDesc(FileDO::getCreateTime));
    }
}
