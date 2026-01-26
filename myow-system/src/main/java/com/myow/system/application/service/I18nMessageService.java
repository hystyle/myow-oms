package com.myow.system.application.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.I18nMessageApplicationConverter;
import com.myow.system.application.dto.CreateI18nMessageReqDTO;
import com.myow.system.application.dto.I18nMessageRespDTO;
import com.myow.system.application.dto.PageI18nMessageReqDTO;
import com.myow.system.application.dto.UpdateI18nMessageReqDTO;
import com.myow.system.domain.entity.I18nMessage;
import com.myow.system.infrastructure.converter.I18nMessageConverter;
import com.myow.system.infrastructure.persistence.po.I18nKeyDO;
import com.myow.system.infrastructure.persistence.po.I18nMessageDO;
import com.myow.system.infrastructure.persistence.repository.I18nKeyRepository;
import com.myow.system.infrastructure.persistence.repository.I18nMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class I18nMessageService {

    private static final List<String> VALID_LANGS = List.of("zh_CN", "en", "zh_TW", "ja", "ko");

    private final I18nMessageRepository i18nMessageRepository;
    private final I18nMessageApplicationConverter i18nMessageApplicationConverter;
    private final I18nMessageConverter i18nMessageConverter;
    private final I18nKeyRepository i18nKeyRepository;

    public Long createI18nMessage(CreateI18nMessageReqDTO createReqDTO) {
        validateI18nMessageForCreate(createReqDTO);
        I18nMessage i18nMessage = i18nMessageApplicationConverter.convert(createReqDTO);
        i18nMessageRepository.save(i18nMessageConverter.toDo(i18nMessage));
        return i18nMessage.getId();
    }

    public void updateI18nMessage(UpdateI18nMessageReqDTO updateReqDTO) {
        validateI18nMessageForUpdate(updateReqDTO);
        I18nMessage i18nMessage = i18nMessageApplicationConverter.convert(updateReqDTO);
        i18nMessageRepository.updateById(i18nMessageConverter.toDo(i18nMessage));
    }

    public void deleteI18nMessage(Long id) {
        I18nMessageDO existMessage = i18nMessageRepository.getById(id);
        if (Objects.isNull(existMessage)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化消息不存在");
        }
        i18nMessageRepository.removeById(id);
    }

    public I18nMessageRespDTO getI18nMessage(Long id) {
        I18nMessageDO i18nMessageDO = i18nMessageRepository.getById(id);
        if (Objects.isNull(i18nMessageDO)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化消息不存在");
        }
        return i18nMessageApplicationConverter.convert(i18nMessageConverter.toEntity(i18nMessageDO));
    }

    public PageResult<I18nMessageRespDTO> getI18nMessagePage(PageI18nMessageReqDTO pageI18nMessageReqDTO) {
        Page<I18nMessageDO> i18nMessageDOPage = i18nMessageRepository.selectPage(pageI18nMessageReqDTO);
        if (Objects.isNull(i18nMessageDOPage) || i18nMessageDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(i18nMessageDOPage, i18nMessageApplicationConverter::convert);
    }

    private void validateI18nMessageForCreate(CreateI18nMessageReqDTO createReqDTO) {
        if (StrUtil.isBlank(createReqDTO.getKeyCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化键编码不能为空");
        }
        
        if (StrUtil.isBlank(createReqDTO.getLang())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "语言不能为空");
        }
        
        if (!VALID_LANGS.contains(createReqDTO.getLang())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "不支持的语言类型");
        }
        
        if (StrUtil.isBlank(createReqDTO.getMessage())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "翻译消息不能为空");
        }
        
        if (Objects.nonNull(createReqDTO.getStatus()) && 
            !Objects.equals(createReqDTO.getStatus(), (short) 0) && 
            !Objects.equals(createReqDTO.getStatus(), (short) 1)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        I18nKeyDO key = i18nKeyRepository.getOne(Wrappers.lambdaQuery(I18nKeyDO.class)
            .eq(I18nKeyDO::getKeyCode, createReqDTO.getKeyCode()));
        if (Objects.isNull(key)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化键不存在");
        }
        
        Long countByKeyCodeAndLang = i18nMessageRepository.count(Wrappers.lambdaQuery(I18nMessageDO.class)
            .eq(I18nMessageDO::getKeyCode, createReqDTO.getKeyCode())
            .eq(I18nMessageDO::getLang, createReqDTO.getLang()));
        if (countByKeyCodeAndLang > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "该国际化键和语言的组合已存在");
        }
    }

    private void validateI18nMessageForUpdate(UpdateI18nMessageReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化消息ID不能为空");
        }
        
        I18nMessageDO existMessage = i18nMessageRepository.getById(updateReqDTO.getId());
        if (Objects.isNull(existMessage)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "国际化消息不存在");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getLang())) {
            if (!VALID_LANGS.contains(updateReqDTO.getLang())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "不支持的语言类型");
            }
        }
        
        if (Objects.nonNull(updateReqDTO.getStatus()) && 
            !Objects.equals(updateReqDTO.getStatus(), (short) 0) && 
            !Objects.equals(updateReqDTO.getStatus(), (short) 1)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getKeyCode())) {
            I18nKeyDO key = i18nKeyRepository.getOne(Wrappers.lambdaQuery(I18nKeyDO.class)
                .eq(I18nKeyDO::getKeyCode, updateReqDTO.getKeyCode()));
            if (Objects.isNull(key)) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "国际化键不存在");
            }
        }
        
        String keyCode = StrUtil.isNotBlank(updateReqDTO.getKeyCode()) ? 
            updateReqDTO.getKeyCode() : existMessage.getKeyCode();
        String lang = StrUtil.isNotBlank(updateReqDTO.getLang()) ? 
            updateReqDTO.getLang() : existMessage.getLang();
        
        if ((StrUtil.isNotBlank(updateReqDTO.getKeyCode()) || StrUtil.isNotBlank(updateReqDTO.getLang())) && 
            (!Objects.equals(existMessage.getKeyCode(), keyCode) || 
             !Objects.equals(existMessage.getLang(), lang))) {
            Long countByKeyCodeAndLang = i18nMessageRepository.count(Wrappers.lambdaQuery(I18nMessageDO.class)
                .eq(I18nMessageDO::getKeyCode, keyCode)
                .eq(I18nMessageDO::getLang, lang)
                .ne(I18nMessageDO::getId, updateReqDTO.getId()));
            if (countByKeyCodeAndLang > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "该国际化键和语言的组合已存在");
            }
        }
    }
}
