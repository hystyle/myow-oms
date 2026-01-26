package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateRoleMenuReqDTO;
import com.myow.system.application.dto.RoleMenuRespDTO;
import com.myow.system.application.dto.UpdateRoleMenuReqDTO;
import com.myow.system.domain.entity.RoleMenu;
import com.myow.system.infrastructure.persistence.po.RoleMenuDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface RoleMenuApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return role menu
     */
    RoleMenu convert(CreateRoleMenuReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return role menu
     */
    @Mapping(source = "roleId", target = "roleId") // Map new roleId to roleId
    @Mapping(source = "menuId", target = "menuId") // Map new menuId to menuId
    RoleMenu convert(UpdateRoleMenuReqDTO bean);

    /**
     * convert
     * @param roleMenu role menu
     * @return role menu resp dto
     */
    RoleMenuRespDTO convert(RoleMenu roleMenu);

    /**
     * convert
     * @param roleMenuDO role menu
     * @return role menu resp dto
     */
    RoleMenuRespDTO convert(RoleMenuDO roleMenuDO);
}
