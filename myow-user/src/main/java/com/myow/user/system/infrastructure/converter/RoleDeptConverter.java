package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.domain.entity.RoleDept;
import com.myow.user.system.infrastructure.persistence.po.RoleDeptDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface RoleDeptConverter {

    /**
     * to do
     * @param roleDept role dept
     * @return role dept do
     */
    RoleDeptDO toDo(RoleDept roleDept);

    /**
     * to entity
     * @param roleDeptDO role dept do
     * @return role dept
     */
    RoleDept toEntity(RoleDeptDO roleDeptDO);
}
