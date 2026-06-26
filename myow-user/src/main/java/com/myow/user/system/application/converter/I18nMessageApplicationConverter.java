package com.myow.user.system.application.converter;

import com.myow.user.system.application.dto.CreateI18nMessageReqDTO;
import com.myow.user.system.application.dto.I18nMessageRespDTO;
import com.myow.user.system.application.dto.UpdateI18nMessageReqDTO;
import com.myow.user.system.domain.entity.I18nMessage;
import com.myow.user.system.infrastructure.persistence.po.I18nMessageDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface I18nMessageApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return i18n message
     */
    I18nMessage convert(CreateI18nMessageReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return i18n message
     */
    I18nMessage convert(UpdateI18nMessageReqDTO bean);

    /**
     * convert
     * @param i18nMessage i18n message
     * @return i18n message resp dto
     */
    I18nMessageRespDTO convert(I18nMessage i18nMessage);

    /**
     * convert
     * @param i18nMessageDO i18n message
     * @return i18n message resp dto
     */
    I18nMessageRespDTO convert(I18nMessageDO i18nMessageDO);
}
