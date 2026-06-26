package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.application.dto.MenuRespDTO;
import com.myow.user.system.domain.entity.Menu;
import com.myow.user.system.infrastructure.persistence.po.MenuDO;
import org.mapstruct.Mapper;

import java.util.List;

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

    /**
     * to dto
     * @param menuDO menu do
     * @return menu dto
     */
    List<MenuRespDTO> convert(List<MenuDO> menuDOList);
}
