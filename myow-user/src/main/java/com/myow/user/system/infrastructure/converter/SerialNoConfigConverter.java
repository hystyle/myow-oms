package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.domain.entity.SerialNoConfig;
import com.myow.user.system.infrastructure.persistence.po.SerialNoConfigDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface SerialNoConfigConverter {

    /**
     * to do
     * @param serialNoConfig serial no config
     * @return serial no config do
     */
    SerialNoConfigDO toDo(SerialNoConfig serialNoConfig);

    /**
     * to entity
     * @param serialNoConfigDO serial no config do
     * @return serial no config
     */
    SerialNoConfig toEntity(SerialNoConfigDO serialNoConfigDO);
}
