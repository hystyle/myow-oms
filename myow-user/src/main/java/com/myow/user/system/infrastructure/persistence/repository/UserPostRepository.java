package com.myow.user.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.mybatis.util.MyPageUtil;
import com.myow.user.system.application.dto.PageUserPostReqDTO;
import com.myow.user.system.infrastructure.persistence.mapper.UserPostMapper;
import com.myow.user.system.infrastructure.persistence.po.UserPostDO;
import org.springframework.stereotype.Repository;

/**
 * @author yss
 */
@Repository
public class UserPostRepository extends ServiceImpl<UserPostMapper, UserPostDO> {

    public Page<UserPostDO> selectPage(PageUserPostReqDTO reqDTO) {
        Page<UserPostDO> page = MyPageUtil.convert2PageQuery(reqDTO, UserPostDO.class);
        LambdaQueryWrapper<UserPostDO> queryWrapper = Wrappers.lambdaQuery();

        if (reqDTO.getUserId() != null) {
            queryWrapper.eq(UserPostDO::getUserId, reqDTO.getUserId());
        }
        if (reqDTO.getPositionId() != null) {
            queryWrapper.eq(UserPostDO::getPositionId, reqDTO.getPositionId());
        }

        return this.page(page, queryWrapper);
    }

    public UserPostDO getByCompositeKey(Long userId, Long positionId) {
        return this.getOne(Wrappers.<UserPostDO>lambdaQuery()
                .eq(UserPostDO::getUserId, userId)
                .eq(UserPostDO::getPositionId, positionId));
    }

    public boolean removeByCompositeKey(Long userId, Long positionId) {
        return this.remove(Wrappers.<UserPostDO>lambdaQuery()
                .eq(UserPostDO::getUserId, userId)
                .eq(UserPostDO::getPositionId, positionId));
    }
}
