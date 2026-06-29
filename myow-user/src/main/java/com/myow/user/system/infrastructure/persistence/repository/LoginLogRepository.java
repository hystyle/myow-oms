package com.myow.user.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.mybatis.util.MyPageUtil;
import com.myow.user.system.application.dto.PageLoginLogReqDTO;
import com.myow.user.system.infrastructure.persistence.mapper.LoginLogMapper;
import com.myow.user.system.infrastructure.persistence.po.LoginLogDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class LoginLogRepository extends ServiceImpl<LoginLogMapper, LoginLogDO> {

    public Page<LoginLogDO> selectPage(PageLoginLogReqDTO reqDTO) {
        Page<LoginLogDO> page = MyPageUtil.convert2PageQuery(reqDTO, LoginLogDO.class);
        LambdaQueryWrapper<LoginLogDO> queryWrapper = Wrappers.lambdaQuery();
        if (reqDTO.getTenantId() != null) {
            queryWrapper.eq(LoginLogDO::getTenantId, reqDTO.getTenantId());
        }
        if (reqDTO.getUserId() != null) {
            queryWrapper.eq(LoginLogDO::getUserId, reqDTO.getUserId());
        }
        if (StringUtils.hasText(reqDTO.getLoginName())) {
            queryWrapper.like(LoginLogDO::getLoginName, reqDTO.getLoginName());
        }
        if (reqDTO.getStatus() != null) {
            queryWrapper.eq(LoginLogDO::getStatus, reqDTO.getStatus());
        }
        if (reqDTO.getLoginTimeStart() != null) {
            queryWrapper.ge(LoginLogDO::getLoginTime, reqDTO.getLoginTimeStart());
        }
        if (reqDTO.getLoginTimeEnd() != null) {
            queryWrapper.le(LoginLogDO::getLoginTime, reqDTO.getLoginTimeEnd());
        }
        queryWrapper.orderByDesc(LoginLogDO::getLoginTime);
        return this.page(page, queryWrapper);
    }
}
