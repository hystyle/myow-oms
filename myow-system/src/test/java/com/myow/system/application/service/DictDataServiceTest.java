package com.myow.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.system.BaseServiceTest;
import com.myow.system.application.dto.CreateDictDataReqDTO;
import com.myow.system.application.dto.DictDataRespDTO;
import com.myow.system.application.dto.UpdateDictDataReqDTO;
import com.myow.system.infrastructure.persistence.po.DictDataDO;
import com.myow.system.infrastructure.persistence.po.DictDO;
import com.myow.system.infrastructure.persistence.repository.DictDataRepository;
import com.myow.system.infrastructure.persistence.repository.DictRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("DictDataService测试")
class DictDataServiceTest extends BaseServiceTest {

    @Mock
    private DictDataRepository dictDataRepository;

    @Mock
    private DictRepository dictRepository;

    @InjectMocks
    private DictDataService dictDataService;

    @Test
    @DisplayName("创建字典数据-成功")
    void createDictData_Success() {
        CreateDictDataReqDTO createReqDTO = new CreateDictDataReqDTO();
        createReqDTO.setDictId(1L);
        createReqDTO.setDataLabel("测试标签");
        createReqDTO.setDataValue("test_value");

        when(dictRepository.getById(1L)).thenReturn(new DictDO());
        when(dictDataRepository.save(any(DictDataDO.class))).thenReturn(true);

        Long dictDataId = dictDataService.createDictData(createReqDTO);

        assertThat(dictDataId).isNotNull();
        verify(dictDataRepository, times(1)).save(any(DictDataDO.class));
    }

    @Test
    @DisplayName("创建字典数据-字典ID为空")
    void createDictData_DictIdNull() {
        CreateDictDataReqDTO createReqDTO = new CreateDictDataReqDTO();
        createReqDTO.setDataLabel("测试标签");
        createReqDTO.setDataValue("test_value");

        assertThatThrownBy(() -> dictDataService.createDictData(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典ID不能为空");
    }

    @Test
    @DisplayName("创建字典数据-字典标签为空")
    void createDictData_DataLabelBlank() {
        CreateDictDataReqDTO createReqDTO = new CreateDictDataReqDTO();
        createReqDTO.setDictId(1L);
        createReqDTO.setDataLabel("");
        createReqDTO.setDataValue("test_value");

        assertThatThrownBy(() -> dictDataService.createDictData(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典标签不能为空");
    }

    @Test
    @DisplayName("创建字典数据-字典值为空")
    void createDictData_DataValueBlank() {
        CreateDictDataReqDTO createReqDTO = new CreateDictDataReqDTO();
        createReqDTO.setDictId(1L);
        createReqDTO.setDataLabel("测试标签");
        createReqDTO.setDataValue("");

        assertThatThrownBy(() -> dictDataService.createDictData(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典值不能为空");
    }

    @Test
    @DisplayName("创建字典数据-字典不存在")
    void createDictData_DictNotExist() {
        CreateDictDataReqDTO createReqDTO = new CreateDictDataReqDTO();
        createReqDTO.setDictId(999L);
        createReqDTO.setDataLabel("测试标签");
        createReqDTO.setDataValue("test_value");

        when(dictRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> dictDataService.createDictData(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典不存在");
    }

    @Test
    @DisplayName("创建字典数据-字典标签已存在")
    void createDictData_DataLabelAlreadyExist() {
        CreateDictDataReqDTO createReqDTO = new CreateDictDataReqDTO();
        createReqDTO.setDictId(1L);
        createReqDTO.setDataLabel("existing_label");
        createReqDTO.setDataValue("test_value");

        DictDO mockDictDO = new DictDO();
        mockDictDO.setDictId(1L);
        when(dictRepository.getById(1L)).thenReturn(mockDictDO);
        when(dictDataRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> dictDataService.createDictData(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("创建字典数据-字典值已存在")
    void createDictData_DataValueAlreadyExist() {
        CreateDictDataReqDTO createReqDTO = new CreateDictDataReqDTO();
        createReqDTO.setDictId(1L);
        createReqDTO.setDataLabel("测试标签");
        createReqDTO.setDataValue("existing_value");

        DictDO mockDictDO = new DictDO();
        mockDictDO.setDictId(1L);
        when(dictRepository.getById(1L)).thenReturn(mockDictDO);
        when(dictDataRepository.count(any())).thenReturn(0L);
        when(dictDataRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> dictDataService.createDictData(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("更新字典数据-成功")
    void updateDictData_Success() {
        UpdateDictDataReqDTO updateReqDTO = new UpdateDictDataReqDTO();
        updateReqDTO.setDictDataId(1L);
        updateReqDTO.setDataLabel("更新标签");

        DictDataDO mockDictDataDO = new DictDataDO();
        mockDictDataDO.setDictDataId(1L);
        when(dictDataRepository.getById(1L)).thenReturn(mockDictDataDO);
        when(dictDataRepository.updateById(any(DictDataDO.class))).thenReturn(true);

        dictDataService.updateDictData(updateReqDTO);

        verify(dictDataRepository, times(1)).updateById(any(DictDataDO.class));
    }

    @Test
    @DisplayName("更新字典数据-字典数据不存在")
    void updateDictData_DictDataNotExist() {
        UpdateDictDataReqDTO updateReqDTO = new UpdateDictDataReqDTO();
        updateReqDTO.setDictDataId(999L);

        when(dictDataRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> dictDataService.updateDictData(updateReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典数据不存在");
    }

    @Test
    @DisplayName("删除字典数据-成功")
    void deleteDictData_Success() {
        DictDataDO mockDictDataDO = new DictDataDO();
        mockDictDataDO.setDictDataId(1L);
        when(dictDataRepository.getById(1L)).thenReturn(mockDictDataDO);
        when(dictDataRepository.removeById(1L)).thenReturn(true);

        dictDataService.deleteDictData(1L);

        verify(dictDataRepository, times(1)).removeById(1L);
    }

    @Test
    @DisplayName("删除字典数据-字典数据不存在")
    void deleteDictData_DictDataNotExist() {
        when(dictDataRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> dictDataService.deleteDictData(999L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典数据不存在");
    }

    @Test
    @DisplayName("获取字典数据-成功")
    void getDictData_Success() {
        DictDataDO mockDictDataDO = new DictDataDO();
        mockDictDataDO.setDictDataId(1L);
        mockDictDataDO.setDataLabel("测试标签");
        when(dictDataRepository.getById(1L)).thenReturn(mockDictDataDO);

        DictDataRespDTO dictDataRespDTO = dictDataService.getDictData(1L);

        assertThat(dictDataRespDTO).isNotNull();
        assertThat(dictDataRespDTO.getDictDataId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取字典数据-字典数据不存在")
    void getDictData_DictDataNotExist() {
        when(dictDataRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> dictDataService.getDictData(999L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("字典数据不存在");
    }
}