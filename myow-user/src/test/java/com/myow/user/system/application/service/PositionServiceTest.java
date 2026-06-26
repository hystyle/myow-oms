package com.myow.user.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.user.system.BaseServiceTest;
import com.myow.user.system.application.dto.CreatePositionReqDTO;
import com.myow.user.system.application.dto.PositionRespDTO;
import com.myow.user.system.application.dto.UpdatePositionReqDTO;
import com.myow.user.system.infrastructure.persistence.po.DeptDO;
import com.myow.user.system.infrastructure.persistence.po.PositionDO;
import com.myow.user.system.infrastructure.persistence.repository.DeptRepository;
import com.myow.user.system.infrastructure.persistence.repository.PositionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("PositionService测试")
class PositionServiceTest extends BaseServiceTest {

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private DeptRepository deptRepository;

    @InjectMocks
    private PositionService positionService;

    @Test
    @DisplayName("创建岗位-成功")
    void createPosition_Success() {
        CreatePositionReqDTO createReqDTO = new CreatePositionReqDTO();
        createReqDTO.setPositionName("测试岗位");
        createReqDTO.setPositionCode("test_position");
        createReqDTO.setDeptId(1L);

        when(positionRepository.save(any(PositionDO.class))).thenReturn(true);

        Long positionId = positionService.createPosition(createReqDTO);

        assertThat(positionId).isNotNull();
        verify(positionRepository, times(1)).save(any(PositionDO.class));
    }

    @Test
    @DisplayName("创建岗位-岗位名称为空")
    void createPosition_PositionNameBlank() {
        CreatePositionReqDTO createReqDTO = new CreatePositionReqDTO();
        createReqDTO.setPositionName("");
        createReqDTO.setPositionCode("test_position");

        assertThatThrownBy(() -> positionService.createPosition(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("岗位名称不能为空");
    }

    @Test
    @DisplayName("创建岗位-岗位编码为空")
    void createPosition_PositionCodeBlank() {
        CreatePositionReqDTO createReqDTO = new CreatePositionReqDTO();
        createReqDTO.setPositionName("测试岗位");
        createReqDTO.setPositionCode("");

        assertThatThrownBy(() -> positionService.createPosition(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("岗位编码不能为空");
    }

    @Test
    @DisplayName("创建岗位-岗位编码格式错误")
    void createPosition_PositionCodeFormatError() {
        CreatePositionReqDTO createReqDTO = new CreatePositionReqDTO();
        createReqDTO.setPositionName("测试岗位");
        createReqDTO.setPositionCode("123_invalid");

        assertThatThrownBy(() -> positionService.createPosition(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("岗位编码格式不正确");
    }

    @Test
    @DisplayName("创建岗位-部门不存在")
    void createPosition_DeptNotExist() {
        CreatePositionReqDTO createReqDTO = new CreatePositionReqDTO();
        createReqDTO.setPositionName("测试岗位");
        createReqDTO.setPositionCode("test_position");
        createReqDTO.setDeptId(999L);

        when(deptRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> positionService.createPosition(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("部门不存在");
    }

    @Test
    @DisplayName("创建岗位-岗位名称已存在")
    void createPosition_PositionNameAlreadyExist() {
        CreatePositionReqDTO createReqDTO = new CreatePositionReqDTO();
        createReqDTO.setPositionName("existing_position");
        createReqDTO.setPositionCode("test_position");

        when(positionRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> positionService.createPosition(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("创建岗位-岗位编码已存在")
    void createPosition_PositionCodeAlreadyExist() {
        CreatePositionReqDTO createReqDTO = new CreatePositionReqDTO();
        createReqDTO.setPositionName("测试岗位");
        createReqDTO.setPositionCode("existing_code");

        when(positionRepository.count(any())).thenReturn(0L);
        when(positionRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> positionService.createPosition(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("更新岗位-成功")
    void updatePosition_Success() {
        UpdatePositionReqDTO updateReqDTO = new UpdatePositionReqDTO();
        updateReqDTO.setPositionId(1L);
        updateReqDTO.setPositionName("更新岗位");

        PositionDO mockPositionDO = new PositionDO();
        mockPositionDO.setPositionId(1L);
        when(positionRepository.getById(1L)).thenReturn(mockPositionDO);
        when(positionRepository.updateById(any(PositionDO.class))).thenReturn(true);

        positionService.updatePosition(updateReqDTO);

        verify(positionRepository, times(1)).updateById(any(PositionDO.class));
    }

    @Test
    @DisplayName("更新岗位-岗位不存在")
    void updatePosition_PositionNotExist() {
        UpdatePositionReqDTO updateReqDTO = new UpdatePositionReqDTO();
        updateReqDTO.setPositionId(999L);

        when(positionRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> positionService.updatePosition(updateReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.POSITION_NOT_EXIST);
    }

    @Test
    @DisplayName("删除岗位-成功")
    void deletePosition_Success() {
        PositionDO mockPositionDO = new PositionDO();
        mockPositionDO.setPositionId(1L);
        when(positionRepository.getById(1L)).thenReturn(mockPositionDO);
        when(positionRepository.count(any())).thenReturn(0L);
        when(positionRepository.removeById(1L)).thenReturn(true);

        positionService.deletePosition(1L);

        verify(positionRepository, times(1)).removeById(1L);
    }

    @Test
    @DisplayName("删除岗位-岗位不存在")
    void deletePosition_PositionNotExist() {
        when(positionRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> positionService.deletePosition(999L))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.POSITION_NOT_EXIST);
    }

    @Test
    @DisplayName("删除岗位-已被用户使用")
    void deletePosition_UsedByUser() {
        PositionDO mockPositionDO = new PositionDO();
        mockPositionDO.setPositionId(1L);
        when(positionRepository.getById(1L)).thenReturn(mockPositionDO);
        when(positionRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> positionService.deletePosition(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该岗位已被用户使用，无法删除");
    }

    @Test
    @DisplayName("获取岗位-成功")
    void getPosition_Success() {
        PositionDO mockPositionDO = new PositionDO();
        mockPositionDO.setPositionId(1L);
        mockPositionDO.setPositionName("测试岗位");
        when(positionRepository.getById(1L)).thenReturn(mockPositionDO);

        PositionRespDTO positionRespDTO = positionService.getPosition(1L);

        assertThat(positionRespDTO).isNotNull();
        assertThat(positionRespDTO.getPositionId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取岗位-岗位不存在")
    void getPosition_PositionNotExist() {
        when(positionRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> positionService.getPosition(999L))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.POSITION_NOT_EXIST);
    }
}