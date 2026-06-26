package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.domain.entity.RoleMenu;
import com.myow.user.system.infrastructure.persistence.po.RoleMenuDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface RoleMenuConverter {

    /**
     * to do
     * @param roleMenu role menu
     * @return role menu do
     */
    RoleMenuDO toDo(RoleMenu roleMenu);

    /**
     * to entity
     * @param roleMenuDO role menu do
     * @return role menu
     */
    RoleMenu toEntity(RoleMenuDO roleMenuDO);
}
