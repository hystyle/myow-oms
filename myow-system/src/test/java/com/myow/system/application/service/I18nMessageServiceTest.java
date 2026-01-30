package com.myow.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.system.BaseServiceTest;
import com.myow.system.application.dto.CreateI18nMessageReqDTO;
import com.myow.system.application.dto.I18nMessageRespDTO;
import com.myow.system.application.dto.UpdateI18nMessageReqDTO;
import com.myow.system.infrastructure.persistence.po.I18nKeyDO;
import com.myow.system.infrastructure.persistence.po.I18nMessageDO;
import com.myow.system.infrastructure.persistence.repository.I18nKeyRepository;
import com.myow.system.infrastructure.persistence.repository.I18nMessageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("I18nMessageService测试")
class I18nMessageServiceTest extends BaseServiceTest {

    @Mock
    private I18nMessageRepository i18nMessageRepository;

    @Mock
    private I18nKeyRepository i18nKeyRepository;

    @InjectMocks
    private I18nMessageService i18nMessageService;

    @Test
    @DisplayName("创建国际化消息-成功")
    void createI18nMessage_Success() {
        CreateI18nMessageReqDTO createReqDTO = new CreateI18nMessageReqDTO();
        createReqDTO.setKeyCode("test.key");
        createReqDTO.setLang("zh_CN");
        createReqDTO.setMessage("测试消息");

        I18nKeyDO mockKeyDO = new I18nKeyDO();
        mockKeyDO.setId(1L);
        when(i18nKeyRepository.getOne(any())).thenReturn(mockKeyDO);
        when(i18nMessageRepository.save(any(I18nMessageDO.class))).thenReturn(true);

        Long messageId = i18nMessageService.createI18nMessage(createReqDTO);

        assertThat(messageId).isNotNull();
        verify(i18nMessageRepository, times(1)).save(any(I18nMessageDO.class));
    }

    @Test
    @DisplayName("创建国际化消息-键编码为空")
    void createI18nMessage_KeyCodeBlank() {
        CreateI18nMessageReqDTO createReqDTO = new CreateI18nMessageReqDTO();
        createReqDTO.setKeyCode("");
        createReqDTO.setLang("zh_CN");
        createReqDTO.setMessage("测试消息");

        assertThatThrownBy(() -> i18nMessageService.createI18nMessage(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("国际化键编码不能为空");
    }

    @Test
    @DisplayName("创建国际化消息-语言为空")
    void createI18nMessage_LangBlank() {
        CreateI18nMessageReqDTO createReqDTO = new CreateI18nMessageReqDTO();
        createReqDTO.setKeyCode("test.key");
        createReqDTO.setLang("");
        createReqDTO.setMessage("测试消息");

        assertThatThrownBy(() -> i18nMessageService.createI18nMessage(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("语言不能为空");
    }

    @Test
    @DisplayName("创建国际化消息-消息为空")
    void createI18nMessage_MessageBlank() {
        CreateI18nMessageReqDTO createReqDTO = new CreateI18nMessageReqDTO();
        createReqDTO.setKeyCode("test.key");
        createReqDTO.setLang("zh_CN");
        createReqDTO.setMessage("");

        assertThatThrownBy(() -> i18nMessageService.createI18nMessage(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("翻译消息不能为空");
    }

    @Test
    @DisplayName("创建国际化消息-语言不支持")
    void createI18nMessage_LangNotSupported() {
        CreateI18nMessageReqDTO createReqDTO = new CreateI18nMessageReqDTO();
        createReqDTO.setKeyCode("test.key");
        createReqDTO.setLang("unsupported");
        createReqDTO.setMessage("测试消息");

        assertThatThrownBy(() -> i18nMessageService.createI18nMessage(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("不支持的语言类型");
    }

    @Test
    @DisplayName("创建国际化消息-键不存在")
    void createI18nMessage_KeyNotExist() {
        CreateI18nMessageReqDTO createReqDTO = new CreateI18nMessageReqDTO();
        createReqDTO.setKeyCode("nonexistent.key");
        createReqDTO.setLang("zh_CN");
        createReqDTO.setMessage("测试消息");

        when(i18nKeyRepository.getOne(any())).thenReturn(null);

        assertThatThrownBy(() -> i18nMessageService.createI18nMessage(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("国际化键不存在");
    }

    @Test
    @DisplayName("创建国际化消息-键和语言组合已存在")
    void createI18nMessage_KeyLangAlreadyExist() {
        CreateI18nMessageReqDTO createReqDTO = new CreateI18nMessageReqDTO();
        createReqDTO.setKeyCode("existing.key");
        createReqDTO.setLang("zh_CN");
        createReqDTO.setMessage("测试消息");

        I18nKeyDO mockKeyDO = new I18nKeyDO();
        mockKeyDO.setId(1L);
        when(i18nKeyRepository.getOne(any())).thenReturn(mockKeyDO);
        when(i18nMessageRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> i18nMessageService.createI18nMessage(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该国际化键和语言的组合已存在");
    }

    @Test
    @DisplayName("更新国际化消息-成功")
    void updateI18nMessage_Success() {
        UpdateI18nMessageReqDTO updateReqDTO = new UpdateI18nMessageReqDTO();
        updateReqDTO.setId(1L);
        updateReqDTO.setMessage("更新消息");

        I18nMessageDO mockMessageDO = new I18nMessageDO();
        mockMessageDO.setId(1L);
        when(i18nMessageRepository.getById(1L)).thenReturn(mockMessageDO);
        when(i18nMessageRepository.updateById(any(I18nMessageDO.class))).thenReturn(true);

        i18nMessageService.updateI18nMessage(updateReqDTO);

        verify(i18nMessageRepository, times(1)).updateById(any(I18nMessageDO.class));
    }

    @Test
    @DisplayName("更新国际化消息-消息不存在")
    void updateI18nMessage_MessageNotExist() {
        UpdateI18nMessageReqDTO updateReqDTO = new UpdateI18nMessageReqDTO();
        updateReqDTO.setId(999L);

        when(i18nMessageRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> i18nMessageService.updateI18nMessage(updateReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("国际化消息不存在");
    }

    @Test
    @DisplayName("删除国际化消息-成功")
    void deleteI18nMessage_Success() {
        I18nMessageDO mockMessageDO = new I18nMessageDO();
        mockMessageDO.setId(1L);
        when(i18nMessageRepository.getById(1L)).thenReturn(mockMessageDO);
        when(i18nMessageRepository.removeById(1L)).thenReturn(true);

        i18nMessageService.deleteI18nMessage(1L);

        verify(i18nMessageRepository, times(1)).removeById(1L);
    }

    @Test
    @DisplayName("删除国际化消息-消息不存在")
    void deleteI18nMessage_MessageNotExist() {
        when(i18nMessageRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> i18nMessageService.deleteI18nMessage(999L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("国际化消息不存在");
    }

    @Test
    @DisplayName("获取国际化消息-成功")
    void getI18nMessage_Success() {
        I18nMessageDO mockMessageDO = new I18nMessageDO();
        mockMessageDO.setId(1L);
        mockMessageDO.setMessage("测试消息");
        when(i18nMessageRepository.getById(1L)).thenReturn(mockMessageDO);

        I18nMessageRespDTO messageRespDTO = i18nMessageService.getI18nMessage(1L);

        assertThat(messageRespDTO).isNotNull();
        assertThat(messageRespDTO.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取国际化消息-消息不存在")
    void getI18nMessage_MessageNotExist() {
        when(i18nMessageRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> i18nMessageService.getI18nMessage(999L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("国际化消息不存在");
    }
}