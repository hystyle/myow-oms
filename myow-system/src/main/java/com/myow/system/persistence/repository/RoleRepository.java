package com.myow.system.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.persistence.mapper.RoleMapper;
import com.myow.system.persistence.po.RoleDO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class RoleRepository extends ServiceImpl<RoleMapper, RoleDO> {

}
