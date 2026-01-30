package com.myow.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.system.BaseServiceTest;
import com.myow.system.application.dto.CreateRoleDeptReqDTO;
import com.myow.system.application.dto.RoleDeptRespDTO;
import com.myow.system.application.dto.UpdateRoleDeptReqDTO;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import com.myow.system.infrastructure.persistence.po.RoleDO;
import com.myow.system.infrastructure.persistence.po.RoleDeptDO;
import com.myow.system.infrastructure.persistence.repository.DeptRepository;
import com.myow.system.infrastructure.persistence.repository.RoleDeptRepository;
import com.myow.system.infrastructure.persistence.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("RoleDeptService测试")
class RoleDeptServiceTest extends BaseServiceTest {

    @Mock
    private RoleDeptRepository roleDeptRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private DeptRepository deptRepository;

    @InjectMocks
    private RoleDeptService roleDeptService;

    @Test
    @DisplayName("创建角色部门关联-成功")
    void createRoleDept_Success() {
        CreateRoleDeptReqDTO createReqDTO = new CreateRoleDeptReqDTO();
        createReqDTO.setRoleId(1L);
        createReqDTO.setDeptId(2L);

        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        DeptDO mockDeptDO = new DeptDO();
        mockDeptDO.setDeptId(2L);
        RoleDeptDO mockRoleDeptDO = new RoleDeptDO();
        mockRoleDeptDO.setRoleId(1L);
        mockRoleDeptDO.setDeptId(2L);

        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);
        when(deptRepository.getById(2L)).thenReturn(mockDeptDO);
        when(roleDeptRepository.save(any(RoleDeptDO.class))).thenReturn(true);

        boolean result = roleDeptService.createRoleDept(createReqDTO);

        assertThat(result).isTrue();
        verify(roleDeptRepository, times(1)).save(any(RoleDeptDO.class));
    }

    @Test
    @DisplayName("创建角色部门关联-角色ID为空")
    void createRoleDept_RoleIdNull() {
        CreateRoleDeptReqDTO createReqDTO = new CreateRoleDeptReqDTO();
        createReqDTO.setRoleId(null);
        createReqDTO.setDeptId(2L);

        assertThatThrownBy(() -> roleDeptService.createRoleDept(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("角色ID不能为空");
    }

    @Test
    @DisplayName("创建角色部门关联-部门ID为空")
    void createRoleDept_DeptIdNull() {
        CreateRoleDeptReqDTO createReqDTO = new CreateRoleDeptReqDTO();
        createReqDTO.setRoleId(1L);
        createReqDTO.setDeptId(null);

        assertThatThrownBy(() -> roleDeptService.createRoleDept(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("部门ID不能为空");
    }

    @Test
    @DisplayName("创建角色部门关联-角色不存在")
    void createRoleDept_RoleNotExist() {
        CreateRoleDeptReqDTO createReqDTO = new CreateRoleDeptReqDTO();
        createReqDTO.setRoleId(999L);
        createReqDTO.setDeptId(2L);

        when(roleRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> roleDeptService.createRoleDept(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ROLE_NOT_EXIST);
    }

    @Test
    @DisplayName("创建角色部门关联-部门不存在")
    void createRoleDept_DeptNotExist() {
        CreateRoleDeptReqDTO createReqDTO = new CreateRoleDeptReqDTO();
        createReqDTO.setRoleId(1L);
        createReqDTO.setDeptId(999L);

        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);
        when(deptRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> roleDeptService.createRoleDept(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.DEPT_NOT_EXIST);
    }

    @Test
    @DisplayName("创建角色部门关联-关联已存在")
    void createRoleDept_AlreadyExist() {
        CreateRoleDeptReqDTO createReqDTO = new CreateRoleDeptReqDTO();
        createReqDTO.setRoleId(1L);
        createReqDTO.setDeptId(2L);

        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        DeptDO mockDeptDO = new DeptDO();
        mockDeptDO.setDeptId(2L);
        RoleDeptDO mockRoleDeptDO = new RoleDeptDO();
        mockRoleDeptDO.setRoleId(1L);
        mockRoleDeptDO.setDeptId(2L);

        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);
        when(deptRepository.getById(2L)).thenReturn(mockDeptDO);
        when(roleDeptRepository.getByCompositeKey(1L, 2L)).thenReturn(mockRoleDeptDO);

        assertThatThrownBy(() -> roleDeptService.createRoleDept(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该角色部门关联已存在");
    }

    @Test
    @DisplayName("更新角色部门关联-成功")
    void updateRoleDept_Success() {
        UpdateRoleDeptReqDTO updateReqDTO = new UpdateRoleDeptReqDTO();
        updateReqDTO.setRoleId(1L);
        updateReqDTO.setDeptId(2L);
        updateReqDTO.setOriginalRoleId(3L);
        updateReqDTO.setOriginalDeptId(4L);

        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        DeptDO mockDeptDO = new DeptDO();
        mockDeptDO.setDeptId(2L);

        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);
        when(deptRepository.getById(2L)).thenReturn(mockDeptDO);
        when(roleDeptRepository.getByCompositeKey(1L, 2L)).thenReturn(null);

        roleDeptService.updateRoleDept(updateReqDTO);

        verify(roleDeptRepository, times(1)).removeByCompositeKey(3L, 4L);
        verify(roleDeptRepository, times(1)).save(any(RoleDeptDO.class));
    }

    @Test
    @DisplayName("删除角色部门关联-成功")
    void deleteRoleDept_Success() {
        RoleDeptDO mockRoleDeptDO = new RoleDeptDO();
        mockRoleDeptDO.setRoleId(1L);
        mockRoleDeptDO.setDeptId(2L);

        when(roleDeptRepository.getByCompositeKey(1L, 2L)).thenReturn(mockRoleDeptDO);
        when(roleDeptRepository.removeByCompositeKey(1L, 2L)).thenReturn(true);

        roleDeptService.deleteRoleDept(1L, 2L);

        verify(roleDeptRepository, times(1)).removeByCompositeKey(1L, 2L);
    }

    @Test
    @DisplayName("删除角色部门关联-关联不存在")
    void deleteRoleDept_NotExist() {
        when(roleDeptRepository.getByCompositeKey(1L, 2L)).thenReturn(null);

        assertThatThrownBy(() -> roleDeptService.deleteRoleDept(1L, 2L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("角色部门关联不存在");
    }

    @Test
    @DisplayName("获取角色部门关联-成功")
    void getRoleDept_Success() {
        RoleDeptDO mockRoleDeptDO = new RoleDeptDO();
        mockRoleDeptDO.setRoleId(1L);
        mockRoleDeptDO.setDeptId(2L);

        when(roleDeptRepository.getByCompositeKey(1L, 2L)).thenReturn(mockRoleDeptDO);

        RoleDeptRespDTO roleDeptRespDTO = roleDeptService.getRoleDept(1L, 2L);

        assertThat(roleDeptRespDTO).isNotNull();
        assertThat(roleDeptRespDTO.getRoleId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取角色部门关联-关联不存在")
    void getRoleDept_NotExist() {
        when(roleDeptRepository.getByCompositeKey(1L, 2L)).thenReturn(null);

        assertThatThrownBy(() -> roleDeptService.getRoleDept(1L, 2L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("角色部门关联不存在");
    }
}