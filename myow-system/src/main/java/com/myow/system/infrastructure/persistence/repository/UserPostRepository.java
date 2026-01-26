package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.po.UserPostDO;
import com.myow.system.infrastructure.persistence.mapper.UserPostMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户与岗位关联表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class UserPostRepository extends ServiceImpl<UserPostMapper, UserPostDO>  {

}
