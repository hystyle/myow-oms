package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.DictData;
import com.myow.system.infrastructure.persistence.po.DictDataDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface DictDataConverter {

    /**
     * to do
     * @param dictData dict data
     * @return dict data do
     */
    DictDataDO toDo(DictData dictData);

    /**
     * to entity
     * @param dictDataDO dict data do
     * @return dict data
     */
    DictData toEntity(DictDataDO dictDataDO);
}
