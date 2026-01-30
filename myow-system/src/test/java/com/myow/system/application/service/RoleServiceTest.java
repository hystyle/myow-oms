package com.myow.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.system.BaseServiceTest;
import com.myow.system.application.dto.CreateRoleReqDTO;
import com.myow.system.application.dto.RoleRespDTO;
import com.myow.system.application.dto.UpdateRoleReqDTO;
import com.myow.system.infrastructure.persistence.po.RoleDO;
import com.myow.system.infrastructure.persistence.repository.RoleRepository;
import com.myow.system.infrastructure.persistence.repository.UserRoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("RoleService测试")
class RoleServiceTest extends BaseServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    @DisplayName("创建角色-成功")
    void createRole_Success() {
        CreateRoleReqDTO createReqDTO = new CreateRoleReqDTO();
        createReqDTO.setRoleName("测试角色");
        createReqDTO.setRoleCode("test_role");
        createReqDTO.setStatus("1");
        createReqDTO.setDataScope("1");

        when(roleRepository.save(any(RoleDO.class))).thenReturn(true);

        Long roleId = roleService.createRole(createReqDTO);

        assertThat(roleId).isNotNull();
        verify(roleRepository, times(1)).save(any(RoleDO.class));
    }

    @Test
    @DisplayName("创建角色-角色名称为空")
    void createRole_RoleNameBlank() {
        CreateRoleReqDTO createReqDTO = new CreateRoleReqDTO();
        createReqDTO.setRoleName("");
        createReqDTO.setRoleCode("test_role");

        assertThatThrownBy(() -> roleService.createRole(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("角色名称不能为空");
    }

    @Test
    @DisplayName("创建角色-角色编码为空")
    void createRole_RoleCodeBlank() {
        CreateRoleReqDTO createReqDTO = new CreateRoleReqDTO();
        createReqDTO.setRoleName("测试角色");
        createReqDTO.setRoleCode("");

        assertThatThrownBy(() -> roleService.createRole(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("角色编码不能为空");
    }

    @Test
    @DisplayName("创建角色-数据范围参数错误")
    void createRole_DataScopeError() {
        CreateRoleReqDTO createReqDTO = new CreateRoleReqDTO();
        createReqDTO.setRoleName("测试角色");
        createReqDTO.setRoleCode("test_role");
        createReqDTO.setDataScope("9");

        assertThatThrownBy(() -> roleService.createRole(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("数据范围参数不正确");
    }

    @Test
    @DisplayName("创建角色-角色名称已存在")
    void createRole_RoleNameAlreadyExist() {
        CreateRoleReqDTO createReqDTO = new CreateRoleReqDTO();
        createReqDTO.setRoleName("existing_role");
        createReqDTO.setRoleCode("test_role");

        when(roleRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> roleService.createRole(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("创建角色-角色编码已存在")
    void createRole_RoleCodeAlreadyExist() {
        CreateRoleReqDTO createReqDTO = new CreateRoleReqDTO();
        createReqDTO.setRoleName("测试角色");
        createReqDTO.setRoleCode("existing_code");

        when(roleRepository.count(any())).thenReturn(0L);
        when(roleRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> roleService.createRole(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("更新角色-成功")
    void updateRole_Success() {
        UpdateRoleReqDTO updateReqDTO = new UpdateRoleReqDTO();
        updateReqDTO.setRoleId(1L);
        updateReqDTO.setRoleName("更新角色");

        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);
        when(roleRepository.updateById(any(RoleDO.class))).thenReturn(true);

        roleService.updateRole(updateReqDTO);

        verify(roleRepository, times(1)).updateById(any(RoleDO.class));
    }

    @Test
    @DisplayName("更新角色-角色不存在")
    void updateRole_RoleNotExist() {
        UpdateRoleReqDTO updateReqDTO = new UpdateRoleReqDTO();
        updateReqDTO.setRoleId(999L);

        when(roleRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> roleService.updateRole(updateReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ROLE_NOT_EXIST);
    }

    @Test
    @DisplayName("删除角色-成功")
    void deleteRole_Success() {
        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);
        when(userRoleRepository.count(any())).thenReturn(0L);
        when(roleRepository.removeById(1L)).thenReturn(true);

        roleService.deleteRole(1L);

        verify(roleRepository, times(1)).removeById(1L);
    }

    @Test
    @DisplayName("删除角色-角色不存在")
    void deleteRole_RoleNotExist() {
        when(roleRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> roleService.deleteRole(999L))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ROLE_NOT_EXIST);
    }

    @Test
    @DisplayName("删除角色-角色已被用户使用")
    void deleteRole_RoleUsedByUser() {
        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);
        when(userRoleRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> roleService.deleteRole(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该角色已被用户使用，无法删除");
    }

    @Test
    @DisplayName("获取角色-成功")
    void getRole_Success() {
        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        mockRoleDO.setRoleName("测试角色");
        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);

        RoleRespDTO roleRespDTO = roleService.getRole(1L);

        assertThat(roleRespDTO).isNotNull();
        assertThat(roleRespDTO.getRoleId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取角色-角色不存在")
    void getRole_RoleNotExist() {
        when(roleRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> roleService.getRole(999L))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ROLE_NOT_EXIST);
    }
}