package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageOperLogReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.OperLogMapper;
import com.myow.system.infrastructure.persistence.po.OperLogDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author yss
 */
@Repository
public class OperLogRepository extends ServiceImpl<OperLogMapper, OperLogDO> {

    public Page<OperLogDO> selectPage(PageOperLogReqDTO reqDTO) {
        Page<OperLogDO> page = MyPageUtil.convert2PageQuery(reqDTO, OperLogDO.class);
        LambdaQueryWrapper<OperLogDO> queryWrapper = Wrappers.lambdaQuery();

        if (StringUtils.hasText(reqDTO.getTitle())) {
            queryWrapper.like(OperLogDO::getTitle, reqDTO.getTitle());
        }
        if (reqDTO.getBusinessType() != null) {
            queryWrapper.eq(OperLogDO::getBusinessType, reqDTO.getBusinessType());
        }
        if (reqDTO.getOperatorType() != null) {
            queryWrapper.eq(OperLogDO::getOperatorType, reqDTO.getOperatorType());
        }
        if (StringUtils.hasText(reqDTO.getOperName())) {
            queryWrapper.like(OperLogDO::getOperName, reqDTO.getOperName());
        }
        if (StringUtils.hasText(reqDTO.getStatus())) {
            queryWrapper.eq(OperLogDO::getStatus, reqDTO.getStatus());
        }
        if (reqDTO.getBeginTime() != null && reqDTO.getEndTime() != null) {
            queryWrapper.between(OperLogDO::getOperTime, reqDTO.getBeginTime(), reqDTO.getEndTime());
        }

        queryWrapper.orderByDesc(OperLogDO::getOperTime); // Order by operTime descending

        return this.page(page, queryWrapper);
    }
}