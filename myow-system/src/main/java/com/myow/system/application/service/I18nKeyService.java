package com.myow.system.application.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.I18nKeyApplicationConverter;
import com.myow.system.application.dto.CreateI18nKeyReqDTO;
import com.myow.system.application.dto.I18nKeyRespDTO;
import com.myow.system.application.dto.PageI18nKeyReqDTO;
import com.myow.system.application.dto.UpdateI18nKeyReqDTO;
import com.myow.system.domain.entity.I18nKey;
import com.myow.system.infrastructure.converter.I18nKeyConverter;
import com.myow.system.infrastructure.persistence.po.I18nKeyDO;
import com.myow.system.infrastructure.persistence.po.I18nMessageDO;
import com.myow.system.infrastructure.persistence.repository.I18nKeyRepository;
import com.myow.system.infrastructure.persistence.repository.I18nMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class I18nKeyService {

    private final I18nKeyRepository i18nKeyRepository;
    private final I18nKeyApplicationConverter i18nKeyApplicationConverter;
    private final I18nKeyConverter i18nKeyConverter;
    private final I18nMessageRepository i18nMessageRepository;

    public Long createI18nKey(CreateI18nKeyReqDTO createReqDTO) {
        validateI18nKeyForCreate(createReqDTO);
        I18nKey i18nKey = i18nKeyApplicationConverter.convert(createReqDTO);
        i18nKeyRepository.save(i18nKeyConverter.toDo(i18nKey));
        return i18nKey.getId();
    }

    public void updateI18nKey(UpdateI18nKeyReqDTO updateReqDTO) {
        validateI18nKeyForUpdate(updateReqDTO);
        I18nKey i18nKey = i18nKeyApplicationConverter.convert(updateReqDTO);
        i18nKeyRepository.updateById(i18nKeyConverter.toDo(i18nKey));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteI18nKey(Long id) {
        I18nKeyDO existKey = i18nKeyRepository.getById(id);
        if (Objects.isNull(existKey)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化键不存在");
        }
        
        Long messageCount = i18nMessageRepository.count(Wrappers.lambdaQuery(I18nMessageDO.class)
            .eq(I18nMessageDO::getKeyCode, existKey.getKeyCode()));
        if (messageCount > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "该国际化键存在对应的翻译消息，无法删除");
        }
        
        i18nKeyRepository.removeById(id);
    }

    public I18nKeyRespDTO getI18nKey(Long id) {
        I18nKeyDO i18nKeyDO = i18nKeyRepository.getById(id);
        if (Objects.isNull(i18nKeyDO)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化键不存在");
        }
        return i18nKeyApplicationConverter.convert(i18nKeyConverter.toEntity(i18nKeyDO));
    }

    public PageResult<I18nKeyRespDTO> getI18nKeyPage(PageI18nKeyReqDTO pageI18nKeyReqDTO) {
        Page<I18nKeyDO> i18nKeyDOPage = i18nKeyRepository.selectPage(pageI18nKeyReqDTO);
        if (Objects.isNull(i18nKeyDOPage) || i18nKeyDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(i18nKeyDOPage, i18nKeyApplicationConverter::convert);
    }

    private void validateI18nKeyForCreate(CreateI18nKeyReqDTO createReqDTO) {
        if (StrUtil.isBlank(createReqDTO.getKeyCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化键编码不能为空");
        }
        
        if (!createReqDTO.getKeyCode().matches("^[a-zA-Z][a-zA-Z0-9_.]*$")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化键编码格式不正确，只能包含字母、数字、下划线和点，且必须以字母开头");
        }
        
        if (Objects.nonNull(createReqDTO.getStatus()) && 
            !Objects.equals(createReqDTO.getStatus(), (short) 0) && 
            !Objects.equals(createReqDTO.getStatus(), (short) 1)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        Long countByKeyCode = i18nKeyRepository.count(Wrappers.lambdaQuery(I18nKeyDO.class)
            .eq(I18nKeyDO::getKeyCode, createReqDTO.getKeyCode()));
        if (countByKeyCode > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "国际化键编码已存在");
        }
    }

    private void validateI18nKeyForUpdate(UpdateI18nKeyReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化键ID不能为空");
        }
        
        I18nKeyDO existKey = i18nKeyRepository.getById(updateReqDTO.getId());
        if (Objects.isNull(existKey)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化键不存在");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getKeyCode())) {
            if (!updateReqDTO.getKeyCode().matches("^[a-zA-Z][a-zA-Z0-9_.]*$")) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "国际化键编码格式不正确，只能包含字母、数字、下划线和点，且必须以字母开头");
            }
        }
        
        if (Objects.nonNull(updateReqDTO.getStatus()) && 
            !Objects.equals(updateReqDTO.getStatus(), (short) 0) && 
            !Objects.equals(updateReqDTO.getStatus(), (short) 1)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getKeyCode()) && 
            !Objects.equals(existKey.getKeyCode(), updateReqDTO.getKeyCode())) {
            Long countByKeyCode = i18nKeyRepository.count(Wrappers.lambdaQuery(I18nKeyDO.class)
                .eq(I18nKeyDO::getKeyCode, updateReqDTO.getKeyCode())
                .ne(I18nKeyDO::getId, updateReqDTO.getId()));
            if (countByKeyCode > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "国际化键编码已存在");
            }
        }
    }
}
