package com.myow.user.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.user.system.BaseServiceTest;
import com.myow.user.system.application.dto.CreateI18nKeyReqDTO;
import com.myow.user.system.application.dto.I18nKeyRespDTO;
import com.myow.user.system.application.dto.UpdateI18nKeyReqDTO;
import com.myow.user.system.infrastructure.persistence.po.I18nKeyDO;
import com.myow.user.system.infrastructure.persistence.po.I18nMessageDO;
import com.myow.user.system.infrastructure.persistence.repository.I18nKeyRepository;
import com.myow.user.system.infrastructure.persistence.repository.I18nMessageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("I18nKeyService测试")
class I18nKeyServiceTest extends BaseServiceTest {

    @Mock
    private I18nKeyRepository i18nKeyRepository;

    @Mock
    private I18nMessageRepository i18nMessageRepository;

    @InjectMocks
    private I18nKeyService i18nKeyService;

    @Test
    @DisplayName("创建国际化键-成功")
    void createI18nKey_Success() {
        CreateI18nKeyReqDTO createReqDTO = new CreateI18nKeyReqDTO();
        createReqDTO.setKeyCode("test.key");
        createReqDTO.setDescription("测试键");

        when(i18nKeyRepository.save(any(I18nKeyDO.class))).thenReturn(true);

        Long keyId = i18nKeyService.createI18nKey(createReqDTO);

        assertThat(keyId).isNotNull();
        verify(i18nKeyRepository, times(1)).save(any(I18nKeyDO.class));
    }

    @Test
    @DisplayName("创建国际化键-键编码为空")
    void createI18nKey_KeyCodeBlank() {
        CreateI18nKeyReqDTO createReqDTO = new CreateI18nKeyReqDTO();
        createReqDTO.setKeyCode("");
        createReqDTO.setDescription("测试键");

        assertThatThrownBy(() -> i18nKeyService.createI18nKey(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("国际化键编码不能为空");
    }

    @Test
    @DisplayName("创建国际化键-键编码格式错误")
    void createI18nKey_KeyCodeFormatError() {
        CreateI18nKeyReqDTO createReqDTO = new CreateI18nKeyReqDTO();
        createReqDTO.setKeyCode("123_invalid");
        createReqDTO.setDescription("测试键");

        assertThatThrownBy(() -> i18nKeyService.createI18nKey(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("国际化键编码格式不正确");
    }

    @Test
    @DisplayName("创建国际化键-键编码已存在")
    void createI18nKey_KeyCodeAlreadyExist() {
        CreateI18nKeyReqDTO createReqDTO = new CreateI18nKeyReqDTO();
        createReqDTO.setKeyCode("existing_key");
        createReqDTO.setDescription("测试键");

        when(i18nKeyRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> i18nKeyService.createI18nKey(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("国际化键编码已存在");
    }

    @Test
    @DisplayName("更新国际化键-成功")
    void updateI18nKey_Success() {
        UpdateI18nKeyReqDTO updateReqDTO = new UpdateI18nKeyReqDTO();
        updateReqDTO.setId(1L);
        updateReqDTO.setDescription("更新键");

        I18nKeyDO mockKeyDO = new I18nKeyDO();
        mockKeyDO.setId(1L);
        when(i18nKeyRepository.getById(1L)).thenReturn(mockKeyDO);
        when(i18nKeyRepository.updateById(any(I18nKeyDO.class))).thenReturn(true);

        i18nKeyService.updateI18nKey(updateReqDTO);

        verify(i18nKeyRepository, times(1)).updateById(any(I18nKeyDO.class));
    }

    @Test
    @DisplayName("更新国际化键-键不存在")
    void updateI18nKey_KeyNotExist() {
        UpdateI18nKeyReqDTO updateReqDTO = new UpdateI18nKeyReqDTO();
        updateReqDTO.setId(999L);

        when(i18nKeyRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> i18nKeyService.updateI18nKey(updateReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("国际化键不存在");
    }

    @Test
    @DisplayName("删除国际化键-成功")
    void deleteI18nKey_Success() {
        I18nKeyDO mockKeyDO = new I18nKeyDO();
        mockKeyDO.setId(1L);
        mockKeyDO.setKeyCode("test.key");
        when(i18nKeyRepository.getById(1L)).thenReturn(mockKeyDO);
        when(i18nMessageRepository.count(any())).thenReturn(0L);
        when(i18nKeyRepository.removeById(1L)).thenReturn(true);

        i18nKeyService.deleteI18nKey(1L);

        verify(i18nKeyRepository, times(1)).removeById(1L);
    }

    @Test
    @DisplayName("删除国际化键-键不存在")
    void deleteI18nKey_KeyNotExist() {
        when(i18nKeyRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> i18nKeyService.deleteI18nKey(999L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("国际化键不存在");
    }

    @Test
    @DisplayName("删除国际化键-存在翻译消息")
    void deleteI18nKey_HasMessages() {
        I18nKeyDO mockKeyDO = new I18nKeyDO();
        mockKeyDO.setId(1L);
        mockKeyDO.setKeyCode("test.key");
        when(i18nKeyRepository.getById(1L)).thenReturn(mockKeyDO);
        when(i18nMessageRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> i18nKeyService.deleteI18nKey(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该国际化键存在对应的翻译消息，无法删除");
    }

    @Test
    @DisplayName("获取国际化键-成功")
    void getI18nKey_Success() {
        I18nKeyDO mockKeyDO = new I18nKeyDO();
        mockKeyDO.setId(1L);
        mockKeyDO.setKeyCode("test.key");
        when(i18nKeyRepository.getById(1L)).thenReturn(mockKeyDO);

        I18nKeyRespDTO keyRespDTO = i18nKeyService.getI18nKey(1L);

        assertThat(keyRespDTO).isNotNull();
        assertThat(keyRespDTO.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取国际化键-键不存在")
    void getI18nKey_KeyNotExist() {
        when(i18nKeyRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> i18nKeyService.getI18nKey(999L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("国际化键不存在");
    }
}