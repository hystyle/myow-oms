package com.myow.system.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.persistence.mapper.MenuMapper;
import com.myow.system.persistence.po.MenuDO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class MenuRepository extends ServiceImpl<MenuMapper, MenuDO> {

}
