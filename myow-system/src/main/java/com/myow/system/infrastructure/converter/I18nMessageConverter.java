package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.I18nMessage;
import com.myow.system.infrastructure.persistence.po.I18nMessageDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface I18nMessageConverter {

    /**
     * to do
     * @param i18nMessage i18n message
     * @return i18n message do
     */
    I18nMessageDO toDo(I18nMessage i18nMessage);

    /**
     * to entity
     * @param i18nMessageDO i18n message do
     * @return i18n message
     */
    I18nMessage toEntity(I18nMessageDO i18nMessageDO);
}
