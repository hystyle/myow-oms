package com.myow.system.persistence.repository;

import com.myow.system.persistence.po.RoleMenuDO;
import com.myow.system.persistence.mapper.RoleMenuMapper;
import com.myow.system.persistence.service.RoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class RoleMenuRepository extends ServiceImpl<RoleMenuMapper, RoleMenuDO> implements RoleMenuService {

}
