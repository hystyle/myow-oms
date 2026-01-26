package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.Position;
import com.myow.system.infrastructure.persistence.po.PositionDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface PositionConverter {

    /**
     * to do
     * @param position position
     * @return position do
     */
    PositionDO toDo(Position position);

    /**
     * to entity
     * @param positionDO position do
     * @return position
     */
    Position toEntity(PositionDO positionDO);
}
