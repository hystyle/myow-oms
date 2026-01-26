package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateSerialNoRecordReqDTO;
import com.myow.system.application.dto.SerialNoRecordRespDTO;
import com.myow.system.application.dto.UpdateSerialNoRecordReqDTO;
import com.myow.system.domain.entity.SerialNoRecord;
import com.myow.system.infrastructure.persistence.po.SerialNoRecordDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface SerialNoRecordApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return serial no record
     */
    SerialNoRecord convert(CreateSerialNoRecordReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return serial no record
     */
    SerialNoRecord convert(UpdateSerialNoRecordReqDTO bean);

    /**
     * convert
     * @param serialNoRecord serial no record
     * @return serial no record resp dto
     */
    SerialNoRecordRespDTO convert(SerialNoRecord serialNoRecord);

    /**
     * convert
     * @param serialNoRecordDO serial no record
     * @return serial no record resp dto
     */
    SerialNoRecordRespDTO convert(SerialNoRecordDO serialNoRecordDO);
}
