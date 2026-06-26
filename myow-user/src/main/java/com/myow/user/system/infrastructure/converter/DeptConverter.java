package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.domain.entity.Dept;
import com.myow.user.system.infrastructure.persistence.po.DeptDO;
import org.mapstruct.Mapper;

/**
 * @author yss
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
