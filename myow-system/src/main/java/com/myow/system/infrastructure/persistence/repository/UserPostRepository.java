package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageUserPostReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.UserPostMapper;
import com.myow.system.infrastructure.persistence.po.UserPostDO;
import org.springframework.stereotype.Repository;

/**
 * @author gemini
 */
@Repository
public class UserPostRepository extends ServiceImpl<UserPostMapper, UserPostDO> {

    public Page<UserPostDO> selectPage(PageUserPostReqDTO reqDTO) {
        Page<UserPostDO> page = MyPageUtil.convert2PageQuery(reqDTO, UserPostDO.class);
        LambdaQueryWrapper<UserPostDO> queryWrapper = Wrappers.lambdaQuery();

        if (reqDTO.getUserId() != null) {
            queryWrapper.eq(UserPostDO::getUserId, reqDTO.getUserId());
        }
        if (reqDTO.getPostId() != null) {
            queryWrapper.eq(UserPostDO::getPostId, reqDTO.getPostId());
        }

        return this.page(page, queryWrapper);
    }

    public UserPostDO getByCompositeKey(Long userId, Long postId) {
        return this.getOne(Wrappers.<UserPostDO>lambdaQuery()
                .eq(UserPostDO::getUserId, userId)
                .eq(UserPostDO::getPostId, postId));
    }

    public boolean removeByCompositeKey(Long userId, Long postId) {
        return this.remove(Wrappers.<UserPostDO>lambdaQuery()
                .eq(UserPostDO::getUserId, userId)
                .eq(UserPostDO::getPostId, postId));
    }
}