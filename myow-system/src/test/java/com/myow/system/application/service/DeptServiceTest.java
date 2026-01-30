package com.myow.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.system.BaseServiceTest;
import com.myow.system.application.dto.CreateDeptReqDTO;
import com.myow.system.application.dto.DeptRespDTO;
import com.myow.system.application.dto.UpdateDeptReqDTO;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import com.myow.system.infrastructure.persistence.po.UserDO;
import com.myow.system.infrastructure.persistence.repository.DeptRepository;
import com.myow.system.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("DeptService测试")
class DeptServiceTest extends BaseServiceTest {

    @Mock
    private DeptRepository deptRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeptService deptService;

    @Test
    @DisplayName("创建部门-成功")
    void createDept_Success() {
        CreateDeptReqDTO createReqDTO = new CreateDeptReqDTO();
        createReqDTO.setDeptName("测试部门");
        createReqDTO.setParentId(0L);
        createReqDTO.setManagerId(1L);

        when(deptRepository.save(any(DeptDO.class))).thenReturn(true);

        Long deptId = deptService.createDept(createReqDTO);

        assertThat(deptId).isNotNull();
        verify(deptRepository, times(1)).save(any(DeptDO.class));
    }

    @Test
    @DisplayName("创建部门-部门名称为空")
    void createDept_DeptNameBlank() {
        CreateDeptReqDTO createReqDTO = new CreateDeptReqDTO();
        createReqDTO.setDeptName("");
        createReqDTO.setParentId(0L);

        assertThatThrownBy(() -> deptService.createDept(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("部门名称不能为空");
    }

    @Test
    @DisplayName("创建部门-父部门不存在")
    void createDept_ParentDeptNotExist() {
        CreateDeptReqDTO createReqDTO = new CreateDeptReqDTO();
        createReqDTO.setDeptName("测试部门");
        createReqDTO.setParentId(999L);

        when(deptRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.createDept(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("父部门不存在");
    }

    @Test
    @DisplayName("创建部门-负责人用户不存在")
    void createDept_ManagerNotExist() {
        CreateDeptReqDTO createReqDTO = new CreateDeptReqDTO();
        createReqDTO.setDeptName("测试部门");
        createReqDTO.setParentId(0L);
        createReqDTO.setManagerId(999L);

        when(userRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.createDept(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("负责人用户不存在");
    }

    @Test
    @DisplayName("创建部门-部门名称已存在")
    void createDept_DeptNameAlreadyExist() {
        CreateDeptReqDTO createReqDTO = new CreateDeptReqDTO();
        createReqDTO.setDeptName("existing_dept");
        createReqDTO.setParentId(0L);

        when(deptRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> deptService.createDept(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("更新部门-成功")
    void updateDept_Success() {
        UpdateDeptReqDTO updateReqDTO = new UpdateDeptReqDTO();
        updateReqDTO.setDeptId(1L);
        updateReqDTO.setDeptName("更新部门");

        DeptDO mockDeptDO = new DeptDO();
        mockDeptDO.setDeptId(1L);
        when(deptRepository.getById(1L)).thenReturn(mockDeptDO);
        when(deptRepository.updateById(any(DeptDO.class))).thenReturn(true);

        deptService.updateDept(updateReqDTO);

        verify(deptRepository, times(1)).updateById(any(DeptDO.class));
    }

    @Test
    @DisplayName("更新部门-部门不存在")
    void updateDept_DeptNotExist() {
        UpdateDeptReqDTO updateReqDTO = new UpdateDeptReqDTO();
        updateReqDTO.setDeptId(999L);

        when(deptRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.updateDept(updateReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.DEPT_NOT_EXIST);
    }

    @Test
    @DisplayName("更新部门-不能将父部门设置为自己")
    void updateDept_CannotSetParentToSelf() {
        UpdateDeptReqDTO updateReqDTO = new UpdateDeptReqDTO();
        updateReqDTO.setDeptId(1L);
        updateReqDTO.setParentId(1L);

        DeptDO mockDeptDO = new DeptDO();
        mockDeptDO.setDeptId(1L);
        when(deptRepository.getById(1L)).thenReturn(mockDeptDO);

        assertThatThrownBy(() -> deptService.updateDept(updateReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("不能将部门的父部门设置为自己");
    }

    @Test
    @DisplayName("删除部门-成功")
    void deleteDept_Success() {
        DeptDO mockDeptDO = new DeptDO();
        mockDeptDO.setDeptId(1L);
        when(deptRepository.getById(1L)).thenReturn(mockDeptDO);
        when(deptRepository.count(any())).thenReturn(0L);
        when(userRepository.count(any())).thenReturn(0L);
        when(deptRepository.removeById(1L)).thenReturn(true);

        deptService.deleteDept(1L);

        verify(deptRepository, times(1)).removeById(1L);
    }

    @Test
    @DisplayName("删除部门-部门不存在")
    void deleteDept_DeptNotExist() {
        when(deptRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.deleteDept(999L))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.DEPT_NOT_EXIST);
    }

    @Test
    @DisplayName("删除部门-存在子部门")
    void deleteDept_HasChildren() {
        DeptDO mockDeptDO = new DeptDO();
        mockDeptDO.setDeptId(1L);
        when(deptRepository.getById(1L)).thenReturn(mockDeptDO);
        when(deptRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> deptService.deleteDept(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该部门存在子部门，无法删除");
    }

    @Test
    @DisplayName("删除部门-存在用户")
    void deleteDept_HasUsers() {
        DeptDO mockDeptDO = new DeptDO();
        mockDeptDO.setDeptId(1L);
        when(deptRepository.getById(1L)).thenReturn(mockDeptDO);
        when(deptRepository.count(any())).thenReturn(0L);
        when(userRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> deptService.deleteDept(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该部门存在用户，无法删除");
    }

    @Test
    @DisplayName("获取部门-成功")
    void getDept_Success() {
        DeptDO mockDeptDO = new DeptDO();
        mockDeptDO.setDeptId(1L);
        mockDeptDO.setDeptName("测试部门");
        when(deptRepository.getById(1L)).thenReturn(mockDeptDO);

        DeptRespDTO deptRespDTO = deptService.getDept(1L);

        assertThat(deptRespDTO).isNotNull();
        assertThat(deptRespDTO.getDeptId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取部门-部门不存在")
    void getDept_DeptNotExist() {
        when(deptRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.getDept(999L))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.DEPT_NOT_EXIST);
    }
}