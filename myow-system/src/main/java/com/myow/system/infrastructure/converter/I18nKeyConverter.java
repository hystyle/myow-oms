package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.I18nKey;
import com.myow.system.infrastructure.persistence.po.I18nKeyDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface I18nKeyConverter {

    /**
     * to do
     * @param i18nKey i18n key
     * @return i18n key do
     */
    I18nKeyDO toDo(I18nKey i18nKey);

    /**
     * to entity
     * @param i18nKeyDO i18n key do
     * @return i18n key
     */
    I18nKey toEntity(I18nKeyDO i18nKeyDO);
}
