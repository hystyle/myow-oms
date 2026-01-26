package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.SerialNoRecord;
import com.myow.system.infrastructure.persistence.po.SerialNoRecordDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface SerialNoRecordConverter {

    /**
     * to do
     * @param serialNoRecord serial no record
     * @return serial no record do
     */
    SerialNoRecordDO toDo(SerialNoRecord serialNoRecord);

    /**
     * to entity
     * @param serialNoRecordDO serial no record do
     * @return serial no record
     */
    SerialNoRecord toEntity(SerialNoRecordDO serialNoRecordDO);
}
