package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.RoleMenu;
import com.myow.system.infrastructure.persistence.po.RoleMenuDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
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
