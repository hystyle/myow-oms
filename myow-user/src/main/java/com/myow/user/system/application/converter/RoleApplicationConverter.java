package com.myow.user.system.application.converter;

import com.myow.user.system.application.dto.CreateRoleReqDTO;
import com.myow.user.system.application.dto.RoleRespDTO;
import com.myow.user.system.application.dto.UpdateRoleReqDTO;
import com.myow.user.system.domain.entity.Role;
import com.myow.user.system.infrastructure.persistence.po.RoleDO;
import org.mapstruct.Mapper;

import java.util.List;

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

    /**
     * convert
     * @param roleByUserId role
     * @return role
     */
    List<RoleRespDTO> convert(List<RoleDO> roleByUserId);
}
