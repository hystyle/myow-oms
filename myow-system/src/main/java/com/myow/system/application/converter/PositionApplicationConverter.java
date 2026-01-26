package com.myow.system.application.converter;

import com.myow.system.application.dto.CreatePositionReqDTO;
import com.myow.system.application.dto.PositionRespDTO;
import com.myow.system.application.dto.UpdatePositionReqDTO;
import com.myow.system.domain.entity.Position;
import com.myow.system.infrastructure.persistence.po.PositionDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface PositionApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return position
     */
    Position convert(CreatePositionReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return position
     */
    Position convert(UpdatePositionReqDTO bean);

    /**
     * convert
     * @param position position
     * @return position resp dto
     */
    PositionRespDTO convert(Position position);

    /**
     * convert
     * @param positionDO positionDO
     * @return PositionRespDTO
     */
    PositionRespDTO convert(PositionDO positionDO);
}
