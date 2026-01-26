package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateDictReqDTO;
import com.myow.system.application.dto.DictRespDTO;
import com.myow.system.application.dto.UpdateDictReqDTO;
import com.myow.system.domain.entity.Dict;
import com.myow.system.infrastructure.persistence.po.DictDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface DictApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return dict
     */
    Dict convert(CreateDictReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return dict
     */
    Dict convert(UpdateDictReqDTO bean);

    /**
     * convert
     * @param dict dict
     * @return dict resp dto
     */
    DictRespDTO convert(Dict dict);

    /**
     * convert
     * @param dictDO dict
     * @return dict resp dto
     */
    DictRespDTO convert(DictDO dictDO);
}
