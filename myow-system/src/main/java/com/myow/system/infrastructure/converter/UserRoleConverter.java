package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.UserRole;
import com.myow.system.infrastructure.persistence.po.UserRoleDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface UserRoleConverter {

    /**
     * to do
     * @param userRole user role
     * @return user role do
     */
    UserRoleDO toDo(UserRole userRole);

    /**
     * to entity
     * @param userRoleDO user role do
     * @return user role
     */
    UserRole toEntity(UserRoleDO userRoleDO);
}
