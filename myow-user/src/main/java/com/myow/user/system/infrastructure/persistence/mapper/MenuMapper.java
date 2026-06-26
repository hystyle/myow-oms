package com.myow.user.system.infrastructure.persistence.mapper;

import com.myow.user.system.infrastructure.persistence.po.MenuDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
public interface MenuMapper extends BaseMapper<MenuDO> {

    List<MenuDO> selectMenuListByRoleIdList(@Param("roleIdList") List<Long> roleIdList, @Param("deletedFlag") Boolean deleteFlag);
}
