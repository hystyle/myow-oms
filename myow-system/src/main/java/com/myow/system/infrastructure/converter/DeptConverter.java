package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.Dept;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface DeptConverter {

    /**
     * to do
     * @param dept dept
     * @return dept do
     */
    DeptDO toDo(Dept dept);

    /**
     * to entity
     * @param deptDO dept do
     * @return dept
     */
    Dept toEntity(DeptDO deptDO);
}
