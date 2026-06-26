package com.myow.user.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.user.system.BaseServiceTest;
import com.myow.user.system.application.dto.CreateDictReqDTO;
import com.myow.user.system.application.dto.DictRespDTO;
import com.myow.user.system.application.dto.UpdateDictReqDTO;
import com.myow.user.system.infrastructure.persistence.po.DictDO;
import com.myow.user.system.infrastructure.persistence.repository.DictDataRepository;
import com.myow.user.system.infrastructure.persistence.repository.DictRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("DictService测试")
class DictServiceTest extends BaseServiceTest {

    @Mock
    private DictRepository dictRepository;

    @Mock
    private DictDataRepository dictDataRepository;

    @InjectMocks
    private DictService dictService;

    @Test
    @DisplayName("创建字典-成功")
    void createDict_Success() {
        CreateDictReqDTO createReqDTO = new CreateDictReqDTO();
        createReqDTO.setDictName("测试字典");
        createReqDTO.setDictCode("test_dict");

        when(dictRepository.save(any(DictDO.class))).thenReturn(true);

        Long dictId = dictService.createDict(createReqDTO);

        assertThat(dictId).isNotNull();
        verify(dictRepository, times(1)).save(any(DictDO.class));
    }

    @Test
    @DisplayName("创建字典-字典名称为空")
    void createDict_DictNameBlank() {
        CreateDictReqDTO createReqDTO = new CreateDictReqDTO();
        createReqDTO.setDictName("");
        createReqDTO.setDictCode("test_dict");

        assertThatThrownBy(() -> dictService.createDict(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典名称不能为空");
    }

    @Test
    @DisplayName("创建字典-字典编码为空")
    void createDict_DictCodeBlank() {
        CreateDictReqDTO createReqDTO = new CreateDictReqDTO();
        createReqDTO.setDictName("测试字典");
        createReqDTO.setDictCode("");

        assertThatThrownBy(() -> dictService.createDict(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典编码不能为空");
    }

    @Test
    @DisplayName("创建字典-字典编码格式错误")
    void createDict_DictCodeFormatError() {
        CreateDictReqDTO createReqDTO = new CreateDictReqDTO();
        createReqDTO.setDictName("测试字典");
        createReqDTO.setDictCode("123_invalid");

        assertThatThrownBy(() -> dictService.createDict(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典编码格式不正确");
    }

    @Test
    @DisplayName("创建字典-字典名称已存在")
    void createDict_DictNameAlreadyExist() {
        CreateDictReqDTO createReqDTO = new CreateDictReqDTO();
        createReqDTO.setDictName("existing_dict");
        createReqDTO.setDictCode("test_dict");

        when(dictRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> dictService.createDict(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("创建字典-字典编码已存在")
    void createDict_DictCodeAlreadyExist() {
        CreateDictReqDTO createReqDTO = new CreateDictReqDTO();
        createReqDTO.setDictName("测试字典");
        createReqDTO.setDictCode("existing_code");

        when(dictRepository.count(any())).thenReturn(0L);
        when(dictRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> dictService.createDict(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("更新字典-成功")
    void updateDict_Success() {
        UpdateDictReqDTO updateReqDTO = new UpdateDictReqDTO();
        updateReqDTO.setDictId(1L);
        updateReqDTO.setDictName("更新字典");

        DictDO mockDictDO = new DictDO();
        mockDictDO.setDictId(1L);
        when(dictRepository.getById(1L)).thenReturn(mockDictDO);
        when(dictRepository.updateById(any(DictDO.class))).thenReturn(true);

        dictService.updateDict(updateReqDTO);

        verify(dictRepository, times(1)).updateById(any(DictDO.class));
    }

    @Test
    @DisplayName("更新字典-字典不存在")
    void updateDict_DictNotExist() {
        UpdateDictReqDTO updateReqDTO = new UpdateDictReqDTO();
        updateReqDTO.setDictId(999L);

        when(dictRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> dictService.updateDict(updateReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典不存在");
    }

    @Test
    @DisplayName("删除字典-成功")
    void deleteDict_Success() {
        DictDO mockDictDO = new DictDO();
        mockDictDO.setDictId(1L);
        when(dictRepository.getById(1L)).thenReturn(mockDictDO);
        when(dictDataRepository.count(any())).thenReturn(0L);
        when(dictRepository.removeById(1L)).thenReturn(true);

        dictService.deleteDict(1L);

        verify(dictRepository, times(1)).removeById(1L);
    }

    @Test
    @DisplayName("删除字典-字典不存在")
    void deleteDict_DictNotExist() {
        when(dictRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> dictService.deleteDict(999L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典不存在");
    }

    @Test
    @DisplayName("删除字典-存在字典数据")
    void deleteDict_HasDictData() {
        DictDO mockDictDO = new DictDO();
        mockDictDO.setDictId(1L);
        when(dictRepository.getById(1L)).thenReturn(mockDictDO);
        when(dictDataRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> dictService.deleteDict(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该字典存在字典数据，无法删除");
    }

    @Test
    @DisplayName("获取字典-成功")
    void getDict_Success() {
        DictDO mockDictDO = new DictDO();
        mockDictDO.setDictId(1L);
        mockDictDO.setDictName("测试字典");
        when(dictRepository.getById(1L)).thenReturn(mockDictDO);

        DictRespDTO dictRespDTO = dictService.getDict(1L);

        assertThat(dictRespDTO).isNotNull();
        assertThat(dictRespDTO.getDictId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取字典-字典不存在")
    void getDict_DictNotExist() {
        when(dictRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> dictService.getDict(999L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典不存在");
    }
}