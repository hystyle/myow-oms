package com.myow.system.persistence.repository;

import com.myow.system.persistence.po.RoleDO;
import com.myow.system.persistence.mapper.RoleMapper;
import com.myow.system.persistence.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class RoleRepository extends ServiceImpl<RoleMapper, RoleDO> implements RoleService {

}
