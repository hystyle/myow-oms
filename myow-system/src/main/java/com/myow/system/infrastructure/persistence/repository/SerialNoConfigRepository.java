package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageSerialNoConfigReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.SerialNoConfigMapper;
import com.myow.system.infrastructure.persistence.po.SerialNoConfigDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author yss
 */
@Repository
public class SerialNoConfigRepository extends ServiceImpl<SerialNoConfigMapper, SerialNoConfigDO> {

    public Page<SerialNoConfigDO> selectPage(PageSerialNoConfigReqDTO reqDTO) {
        Page<SerialNoConfigDO> page = MyPageUtil.convert2PageQuery(reqDTO, SerialNoConfigDO.class);
        LambdaQueryWrapper<SerialNoConfigDO> queryWrapper = Wrappers.lambdaQuery();

        if (StringUtils.hasText(reqDTO.getBusinessName())) {
            queryWrapper.like(SerialNoConfigDO::getBusinessName, reqDTO.getBusinessName());
        }
        if (StringUtils.hasText(reqDTO.getRuleType())) {
            queryWrapper.eq(SerialNoConfigDO::getRuleType, reqDTO.getRuleType());
        }

        return this.page(page, queryWrapper);
    }
}