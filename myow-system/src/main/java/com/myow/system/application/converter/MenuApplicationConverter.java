package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateMenuReqDTO;
import com.myow.system.application.dto.MenuRespDTO;
import com.myow.system.application.dto.UpdateMenuReqDTO;
import com.myow.system.domain.entity.Menu;
import com.myow.system.infrastructure.persistence.po.MenuDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface MenuApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return menu
     */
    Menu convert(CreateMenuReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return menu
     */
    Menu convert(UpdateMenuReqDTO bean);

    /**
     * convert
     * @param menu menu
     * @return menu resp dto
     */
    MenuRespDTO convert(Menu menu);

    /**
     * convert
     * @param menuDO menu
     * @return menu resp dto
     */
    MenuRespDTO convert(MenuDO menuDO);
}
