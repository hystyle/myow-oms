package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.Role;
import com.myow.system.infrastructure.persistence.po.RoleDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
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
