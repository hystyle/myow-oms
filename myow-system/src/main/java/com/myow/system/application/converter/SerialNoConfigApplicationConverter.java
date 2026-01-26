package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateSerialNoConfigReqDTO;
import com.myow.system.application.dto.SerialNoConfigRespDTO;
import com.myow.system.application.dto.UpdateSerialNoConfigReqDTO;
import com.myow.system.domain.entity.SerialNoConfig;
import com.myow.system.infrastructure.persistence.po.SerialNoConfigDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface SerialNoConfigApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return serial no config
     */
    SerialNoConfig convert(CreateSerialNoConfigReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return serial no config
     */
    SerialNoConfig convert(UpdateSerialNoConfigReqDTO bean);

    /**
     * convert
     * @param serialNoConfig serial no config
     * @return serial no config resp dto
     */
    SerialNoConfigRespDTO convert(SerialNoConfig serialNoConfig);

    /**
     * convert
     * @param serialNoConfigDO serial no config
     * @return serial no config
     */
    SerialNoConfigRespDTO convert(SerialNoConfigDO serialNoConfigDO);
}
