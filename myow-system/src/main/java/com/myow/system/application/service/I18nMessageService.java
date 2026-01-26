package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.I18nMessageApplicationConverter;
import com.myow.system.application.dto.CreateI18nMessageReqDTO;
import com.myow.system.application.dto.I18nMessageRespDTO;
import com.myow.system.application.dto.PageI18nMessageReqDTO;
import com.myow.system.application.dto.UpdateI18nMessageReqDTO;
import com.myow.system.domain.entity.I18nMessage;
import com.myow.system.infrastructure.converter.I18nMessageConverter;
import com.myow.system.infrastructure.persistence.po.I18nMessageDO;
import com.myow.system.infrastructure.persistence.repository.I18nMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class I18nMessageService {

    private final I18nMessageRepository i18nMessageRepository;
    private final I18nMessageApplicationConverter i18nMessageApplicationConverter;
    private final I18nMessageConverter i18nMessageConverter;

    public Long createI18nMessage(CreateI18nMessageReqDTO createReqDTO) {
        I18nMessage i18nMessage = i18nMessageApplicationConverter.convert(createReqDTO);
        i18nMessageRepository.save(i18nMessageConverter.toDo(i18nMessage));
        return i18nMessage.getId();
    }

    public void updateI18nMessage(UpdateI18nMessageReqDTO updateReqDTO) {
        I18nMessage i18nMessage = i18nMessageApplicationConverter.convert(updateReqDTO);
        i18nMessageRepository.updateById(i18nMessageConverter.toDo(i18nMessage));
    }

    public void deleteI18nMessage(Long id) {
        i18nMessageRepository.removeById(id);
    }

    public I18nMessageRespDTO getI18nMessage(Long id) {
        I18nMessageDO i18nMessageDO = i18nMessageRepository.getById(id);
        return i18nMessageApplicationConverter.convert(i18nMessageConverter.toEntity(i18nMessageDO));
    }

    public PageResult<I18nMessageRespDTO> getI18nMessagePage(PageI18nMessageReqDTO pageI18nMessageReqDTO) {
        Page<I18nMessageDO> i18nMessageDOPage = i18nMessageRepository.selectPage(pageI18nMessageReqDTO);
        if (Objects.isNull(i18nMessageDOPage) || i18nMessageDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(i18nMessageDOPage, i18nMessageApplicationConverter::convert);
    }
}
