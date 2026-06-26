package com.myow.user.system.application.converter;

import com.myow.user.system.application.dto.CreateUserRoleReqDTO;
import com.myow.user.system.application.dto.UpdateUserRoleReqDTO;
import com.myow.user.system.application.dto.UserRoleRespDTO;
import com.myow.user.system.domain.entity.UserRole;
import com.myow.user.system.infrastructure.persistence.po.UserRoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface UserRoleApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return user role
     */
    UserRole convert(CreateUserRoleReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return user role
     */
    @Mapping(source = "userId", target = "userId") // Map new userId to userId
    @Mapping(source = "roleId", target = "roleId") // Map new roleId to roleId
    UserRole convert(UpdateUserRoleReqDTO bean);

    /**
     * convert
     * @param userRole user role
     * @return user role resp dto
     */
    UserRoleRespDTO convert(UserRole userRole);

    /**
     * convert
     * @param userRoleDO user role do
     * @return user role resp dto
     */
    UserRoleRespDTO convert(UserRoleDO userRoleDO);
}
