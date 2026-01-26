package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateDictDataReqDTO;
import com.myow.system.application.dto.DictDataRespDTO;
import com.myow.system.application.dto.UpdateDictDataReqDTO;
import com.myow.system.domain.entity.DictData;
import com.myow.system.infrastructure.persistence.po.DictDataDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface DictDataApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return dict data
     */
    DictData convert(CreateDictDataReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return dict data
     */
    DictData convert(UpdateDictDataReqDTO bean);

    /**
     * convert
     * @param dictData dict data
     * @return dict data resp dto
     */
    DictDataRespDTO convert(DictData dictData);

    /**
     * convert
     * @param dictDataDO dict data
     * @return dict data
     */
    DictDataRespDTO convert(DictDataDO dictDataDO);
}
