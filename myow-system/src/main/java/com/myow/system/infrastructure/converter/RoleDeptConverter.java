package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.RoleDept;
import com.myow.system.infrastructure.persistence.po.RoleDeptDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
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
