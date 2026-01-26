package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PagePositionReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.PositionMapper;
import com.myow.system.infrastructure.persistence.po.PositionDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author gemini
 */
@Repository
public class PositionRepository extends ServiceImpl<PositionMapper, PositionDO> {

    public Page<PositionDO> selectPage(PagePositionReqDTO reqDTO) {
        Page<PositionDO> page = MyPageUtil.convert2PageQuery(reqDTO, PositionDO.class);
        LambdaQueryWrapper<PositionDO> queryWrapper = Wrappers.lambdaQuery();

        if (StringUtils.hasText(reqDTO.getPositionName())) {
            queryWrapper.like(PositionDO::getPositionName, reqDTO.getPositionName());
        }
        if (StringUtils.hasText(reqDTO.getPositionCode())) {
            queryWrapper.eq(PositionDO::getPositionCode, reqDTO.getPositionCode());
        }
        if (reqDTO.getDeptId() != null) {
            queryWrapper.eq(PositionDO::getDeptId, reqDTO.getDeptId());
        }
        if (StringUtils.hasText(reqDTO.getStatus())) {
            queryWrapper.eq(PositionDO::getStatus, reqDTO.getStatus());
        }

        return this.page(page, queryWrapper);
    }
}