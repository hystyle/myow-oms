package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.domain.entity.Role;
import com.myow.user.system.infrastructure.persistence.po.RoleDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface RoleConverter {

    /**
     * to do
     * @param role role
     * @return role do
     */
    RoleDO toDo(Role role);

    /**
     * to entity
     * @param roleDO role do
     * @return role
     */
    Role toEntity(RoleDO roleDO);
}
