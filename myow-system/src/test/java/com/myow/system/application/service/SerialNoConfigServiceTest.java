package com.myow.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.system.BaseServiceTest;
import com.myow.system.application.dto.CreateSerialNoConfigReqDTO;
import com.myow.system.application.dto.SerialNoConfigRespDTO;
import com.myow.system.application.dto.UpdateSerialNoConfigReqDTO;
import com.myow.system.infrastructure.persistence.po.SerialNoConfigDO;
import com.myow.system.infrastructure.persistence.po.SerialNoRecordDO;
import com.myow.system.infrastructure.persistence.repository.SerialNoConfigRepository;
import com.myow.system.infrastructure.persistence.repository.SerialNoRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@DisplayName("SerialNoConfigService测试")
class SerialNoConfigServiceTest extends BaseServiceTest {

    @Mock
    private SerialNoConfigRepository serialNoConfigRepository;

    @Mock
    private SerialNoRecordRepository serialNoRecordRepository;

    @InjectMocks
    private SerialNoConfigService serialNoConfigService;

    @Test
    @DisplayName("创建序列号配置-成功")
    void createSerialNoConfig_Success() {
        CreateSerialNoConfigReqDTO createReqDTO = new CreateSerialNoConfigReqDTO();
        createReqDTO.setBusinessName("测试业务");
        createReqDTO.setFormat("TEST-{seq}");
        createReqDTO.setRuleType("date");

        when(serialNoConfigRepository.save(any(SerialNoConfigDO.class))).thenReturn(true);

        Integer configId = serialNoConfigService.createSerialNoConfig(createReqDTO);

        assertThat(configId).isNotNull();
        verify(serialNoConfigRepository, times(1)).save(any(SerialNoConfigDO.class));
    }

    @Test
    @DisplayName("创建序列号配置-业务名称为空")
    void createSerialNoConfig_BusinessNameBlank() {
        CreateSerialNoConfigReqDTO createReqDTO = new CreateSerialNoConfigReqDTO();
        createReqDTO.setBusinessName("");
        createReqDTO.setFormat("TEST-{seq}");

        assertThatThrownBy(() -> serialNoConfigService.createSerialNoConfig(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("业务名称不能为空");
    }

    @Test
    @DisplayName("创建序列号配置-序列号格式为空")
    void createSerialNoConfig_FormatBlank() {
        CreateSerialNoConfigReqDTO createReqDTO = new CreateSerialNoConfigReqDTO();
        createReqDTO.setBusinessName("测试业务");
        createReqDTO.setFormat("");

        assertThatThrownBy(() -> serialNoConfigService.createSerialNoConfig(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("序列号格式不能为空");
    }

    @Test
    @DisplayName("创建序列号配置-规则类型为空")
    void createSerialNoConfig_RuleTypeBlank() {
        CreateSerialNoConfigReqDTO createReqDTO = new CreateSerialNoConfigReqDTO();
        createReqDTO.setBusinessName("测试业务");
        createReqDTO.setFormat("TEST-{seq}");
        createReqDTO.setRuleType("");

        assertThatThrownBy(() -> serialNoConfigService.createSerialNoConfig(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("规则类型不能为空");
    }

    @Test
    @DisplayName("创建序列号配置-初始号码小于0")
    void createSerialNoConfig_InitNumberNegative() {
        CreateSerialNoConfigReqDTO createReqDTO = new CreateSerialNoConfigReqDTO();
        createReqDTO.setBusinessName("测试业务");
        createReqDTO.setFormat("TEST-{seq}");
        createReqDTO.setRuleType("date");
        createReqDTO.setInitNumber(-1L);

        assertThatThrownBy(() -> serialNoConfigService.createSerialNoConfig(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("初始号码不能小于0");
    }

    @Test
    @DisplayName("创建序列号配置-步长随机范围小于0")
    void createSerialNoConfig_StepRandomRangeNegative() {
        CreateSerialNoConfigReqDTO createReqDTO = new CreateSerialNoConfigReqDTO();
        createReqDTO.setBusinessName("测试业务");
        createReqDTO.setFormat("TEST-{seq}");
        createReqDTO.setRuleType("date");
        createReqDTO.setStepRandomRange(-1);

        assertThatThrownBy(() -> serialNoConfigService.createSerialNoConfig(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("步长随机范围不能小于0");
    }

    @Test
    @DisplayName("创建序列号配置-业务名称已存在")
    void createSerialNoConfig_BusinessNameAlreadyExist() {
        CreateSerialNoConfigReqDTO createReqDTO = new CreateSerialNoConfigReqDTO();
        createReqDTO.setBusinessName("existing_business");
        createReqDTO.setFormat("TEST-{seq}");
        createReqDTO.setRuleType("date");

        when(serialNoConfigRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> serialNoConfigService.createSerialNoConfig(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("业务名称已存在");
    }

    @Test
    @DisplayName("更新序列号配置-成功")
    void updateSerialNoConfig_Success() {
        UpdateSerialNoConfigReqDTO updateReqDTO = new UpdateSerialNoConfigReqDTO();
        updateReqDTO.setSerialNumberId(1);
        updateReqDTO.setBusinessName("更新业务");

        SerialNoConfigDO mockConfigDO = new SerialNoConfigDO();
        mockConfigDO.setSerialNumberId(1);
        when(serialNoConfigRepository.getById(1)).thenReturn(mockConfigDO);
        when(serialNoConfigRepository.updateById(any(SerialNoConfigDO.class))).thenReturn(true);

        serialNoConfigService.updateSerialNoConfig(updateReqDTO);

        verify(serialNoConfigRepository, times(1)).updateById(any(SerialNoConfigDO.class));
    }

    @Test
    @DisplayName("更新序列号配置-配置不存在")
    void updateSerialNoConfig_ConfigNotExist() {
        UpdateSerialNoConfigReqDTO updateReqDTO = new UpdateSerialNoConfigReqDTO();
        updateReqDTO.setSerialNumberId(999);

        when(serialNoConfigRepository.getById(999)).thenReturn(null);

        assertThatThrownBy(() -> serialNoConfigService.updateSerialNoConfig(updateReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("序列号配置不存在");
    }

    @Test
    @DisplayName("删除序列号配置-成功")
    void deleteSerialNoConfig_Success() {
        SerialNoConfigDO mockConfigDO = new SerialNoConfigDO();
        mockConfigDO.setSerialNumberId(1);
        when(serialNoConfigRepository.getById(1)).thenReturn(mockConfigDO);
        when(serialNoRecordRepository.count(any())).thenReturn(0L);
        when(serialNoConfigRepository.removeById(1)).thenReturn(true);

        serialNoConfigService.deleteSerialNoConfig(1);

        verify(serialNoConfigRepository, times(1)).removeById(1);
    }

    @Test
    @DisplayName("删除序列号配置-配置不存在")
    void deleteSerialNoConfig_ConfigNotExist() {
        when(serialNoConfigRepository.getById(999)).thenReturn(null);

        assertThatThrownBy(() -> serialNoConfigService.deleteSerialNoConfig(999))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("序列号配置不存在");
    }

    @Test
    @DisplayName("删除序列号配置-存在记录")
    void deleteSerialNoConfig_HasRecords() {
        SerialNoConfigDO mockConfigDO = new SerialNoConfigDO();
        mockConfigDO.setSerialNumberId(1);
        when(serialNoConfigRepository.getById(1)).thenReturn(mockConfigDO);
        when(serialNoRecordRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> serialNoConfigService.deleteSerialNoConfig(1))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该序列号配置存在记录，无法删除");
    }

    @Test
    @DisplayName("获取序列号配置-成功")
    void getSerialNoConfig_Success() {
        SerialNoConfigDO mockConfigDO = new SerialNoConfigDO();
        mockConfigDO.setSerialNumberId(1);
        mockConfigDO.setBusinessName("测试业务");
        when(serialNoConfigRepository.getById(1)).thenReturn(mockConfigDO);

        SerialNoConfigRespDTO configRespDTO = serialNoConfigService.getSerialNoConfig(1);

        assertThat(configRespDTO).isNotNull();
        assertThat(configRespDTO.getSerialNumberId()).isEqualTo(1);
    }

    @Test
    @DisplayName("获取序列号配置-配置不存在")
    void getSerialNoConfig_ConfigNotExist() {
        when(serialNoConfigRepository.getById(999)).thenReturn(null);

        assertThatThrownBy(() -> serialNoConfigService.getSerialNoConfig(999))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("序列号配置不存在");
    }
}