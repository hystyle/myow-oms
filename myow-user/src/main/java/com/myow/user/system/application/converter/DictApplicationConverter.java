package com.myow.user.system.application.converter;

import com.myow.user.system.application.dto.CreateDictReqDTO;
import com.myow.user.system.application.dto.DictRespDTO;
import com.myow.user.system.application.dto.UpdateDictReqDTO;
import com.myow.user.system.domain.entity.Dict;
import com.myow.user.system.infrastructure.persistence.po.DictDO;
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
