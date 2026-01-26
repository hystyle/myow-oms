package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageDictReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.DictMapper;
import com.myow.system.infrastructure.persistence.po.DictDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author yss
 */
@Repository
public class DictRepository extends ServiceImpl<DictMapper, DictDO> {

    public Page<DictDO> selectPage(PageDictReqDTO reqDTO) {
        Page<DictDO> page = MyPageUtil.convert2PageQuery(reqDTO, DictDO.class);
        LambdaQueryWrapper<DictDO> queryWrapper = Wrappers.lambdaQuery();

        if (StringUtils.hasText(reqDTO.getDictName())) {
            queryWrapper.like(DictDO::getDictName, reqDTO.getDictName());
        }
        if (StringUtils.hasText(reqDTO.getDictCode())) {
            queryWrapper.eq(DictDO::getDictCode, reqDTO.getDictCode());
        }

        return this.page(page, queryWrapper);
    }
}