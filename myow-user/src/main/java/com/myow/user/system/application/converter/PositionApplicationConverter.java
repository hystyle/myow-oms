package com.myow.user.system.application.converter;

import com.myow.user.system.application.dto.CreatePositionReqDTO;
import com.myow.user.system.application.dto.PositionRespDTO;
import com.myow.user.system.application.dto.UpdatePositionReqDTO;
import com.myow.user.system.domain.entity.Position;
import com.myow.user.system.infrastructure.persistence.po.PositionDO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author yss
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

    /**
     * convert
     * @param positionDOList positionDOList
     * @return PositionRespDTO
     */
    List<PositionRespDTO> convert(List<PositionDO> positionDOList);


}
