package com.myow.system.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.persistence.mapper.UserRoleMapper;
import com.myow.system.persistence.po.UserRoleDO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class UserRoleRepository extends ServiceImpl<UserRoleMapper, UserRoleDO> {

}
