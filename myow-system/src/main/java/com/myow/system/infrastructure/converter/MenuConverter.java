package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.Menu;
import com.myow.system.infrastructure.persistence.po.MenuDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface MenuConverter {

    /**
     * to do
     * @param menu menu
     * @return menu do
     */
    MenuDO toDo(Menu menu);

    /**
     * to entity
     * @param menuDO menu do
     * @return menu
     */
    Menu toEntity(MenuDO menuDO);
}
