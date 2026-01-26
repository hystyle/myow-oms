package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateI18nKeyReqDTO;
import com.myow.system.application.dto.I18nKeyRespDTO;
import com.myow.system.application.dto.UpdateI18nKeyReqDTO;
import com.myow.system.domain.entity.I18nKey;
import com.myow.system.infrastructure.persistence.po.I18nKeyDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface I18nKeyApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return i18n key
     */
    I18nKey convert(CreateI18nKeyReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return i18n key
     */
    I18nKey convert(UpdateI18nKeyReqDTO bean);

    /**
     * convert
     * @param i18nKey i18n key
     * @return i18n key resp dto
     */
    I18nKeyRespDTO convert(I18nKey i18nKey);

    /**
     * convert
     * @param i18nKeyDO i18n keys
     * @return i18n key resp dtos
     */
    I18nKeyRespDTO convert(I18nKeyDO i18nKeyDO);
}
