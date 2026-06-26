package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.domain.entity.OperLog;
import com.myow.user.system.infrastructure.persistence.po.OperLogDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface OperLogConverter {

    /**
     * to do
     * @param operLog oper log
     * @return oper log do
     */
    OperLogDO toDo(OperLog operLog);

    /**
     * to entity
     * @param operLogDO oper log do
     * @return oper log
     */
    OperLog toEntity(OperLogDO operLogDO);
}
