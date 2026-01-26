package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateRoleReqDTO;
import com.myow.system.application.dto.RoleRespDTO;
import com.myow.system.application.dto.UpdateRoleReqDTO;
import com.myow.system.domain.entity.Role;
import com.myow.system.infrastructure.persistence.po.RoleDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface RoleApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return role
     */
    Role convert(CreateRoleReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return role
     */
    Role convert(UpdateRoleReqDTO bean);

    /**
     * convert
     * @param role role
     * @return role resp dto
     */
    RoleRespDTO convert(Role role);

    /**
     * convert
     * @param roleDO role
     * @return role
     */
    RoleRespDTO convert(RoleDO roleDO);
}
