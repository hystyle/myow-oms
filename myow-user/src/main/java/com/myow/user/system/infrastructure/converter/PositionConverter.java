package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.domain.entity.Position;
import com.myow.user.system.infrastructure.persistence.po.PositionDO;
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
