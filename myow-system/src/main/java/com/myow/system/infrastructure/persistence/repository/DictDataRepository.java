package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageDictDataReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.DictDataMapper;
import com.myow.system.infrastructure.persistence.po.DictDataDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author yss
 */
@Repository
public class DictDataRepository extends ServiceImpl<DictDataMapper, DictDataDO> {

    public Page<DictDataDO> selectPage(PageDictDataReqDTO reqDTO) {
        Page<DictDataDO> page = MyPageUtil.convert2PageQuery(reqDTO, DictDataDO.class);
        LambdaQueryWrapper<DictDataDO> queryWrapper = Wrappers.lambdaQuery();

        if (reqDTO.getDictId() != null) {
            queryWrapper.eq(DictDataDO::getDictId, reqDTO.getDictId());
        }
        if (StringUtils.hasText(reqDTO.getDataValue())) {
            queryWrapper.like(DictDataDO::getDataValue, reqDTO.getDataValue());
        }
        if (StringUtils.hasText(reqDTO.getDataLabel())) {
            queryWrapper.like(DictDataDO::getDataLabel, reqDTO.getDataLabel());
        }
        if (reqDTO.getDisabledFlag() != null) {
            queryWrapper.eq(DictDataDO::getDisabledFlag, reqDTO.getDisabledFlag());
        }

        queryWrapper.orderByAsc(DictDataDO::getSort);

        return this.page(page, queryWrapper);
    }
}