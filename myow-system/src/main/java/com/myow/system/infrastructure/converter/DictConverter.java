package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.Dict;
import com.myow.system.infrastructure.persistence.po.DictDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface DictConverter {

    /**
     * to do
     * @param dict dict
     * @return dict do
     */
    DictDO toDo(Dict dict);

    /**
     * to entity
     * @param dictDO dict do
     * @return dict
     */
    Dict toEntity(DictDO dictDO);
}
