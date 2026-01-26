package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.I18nKeyApplicationConverter;
import com.myow.system.application.dto.CreateI18nKeyReqDTO;
import com.myow.system.application.dto.I18nKeyRespDTO;
import com.myow.system.application.dto.PageI18nKeyReqDTO;
import com.myow.system.application.dto.UpdateI18nKeyReqDTO;
import com.myow.system.domain.entity.I18nKey;
import com.myow.system.infrastructure.converter.I18nKeyConverter;
import com.myow.system.infrastructure.persistence.po.I18nKeyDO;
import com.myow.system.infrastructure.persistence.repository.I18nKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class I18nKeyService {

    private final I18nKeyRepository i18nKeyRepository;
    private final I18nKeyApplicationConverter i18nKeyApplicationConverter;
    private final I18nKeyConverter i18nKeyConverter;

    public Long createI18nKey(CreateI18nKeyReqDTO createReqDTO) {
        I18nKey i18nKey = i18nKeyApplicationConverter.convert(createReqDTO);
        i18nKeyRepository.save(i18nKeyConverter.toDo(i18nKey));
        return i18nKey.getId();
    }

    public void updateI18nKey(UpdateI18nKeyReqDTO updateReqDTO) {
        I18nKey i18nKey = i18nKeyApplicationConverter.convert(updateReqDTO);
        i18nKeyRepository.updateById(i18nKeyConverter.toDo(i18nKey));
    }

    public void deleteI18nKey(Long id) {
        i18nKeyRepository.removeById(id);
    }

    public I18nKeyRespDTO getI18nKey(Long id) {
        I18nKeyDO i18nKeyDO = i18nKeyRepository.getById(id);
        return i18nKeyApplicationConverter.convert(i18nKeyConverter.toEntity(i18nKeyDO));
    }

    public PageResult<I18nKeyRespDTO> getI18nKeyPage(PageI18nKeyReqDTO pageI18nKeyReqDTO) {
        Page<I18nKeyDO> i18nKeyDOPage = i18nKeyRepository.selectPage(pageI18nKeyReqDTO);
        if (Objects.isNull(i18nKeyDOPage) || i18nKeyDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(i18nKeyDOPage, i18nKeyApplicationConverter::convert);
    }
}
