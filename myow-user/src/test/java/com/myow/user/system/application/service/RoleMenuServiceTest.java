package com.myow.user.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.user.system.BaseServiceTest;
import com.myow.user.system.application.dto.CreateRoleMenuReqDTO;
import com.myow.user.system.application.dto.RoleMenuRespDTO;
import com.myow.user.system.application.dto.UpdateRoleMenuReqDTO;
import com.myow.user.system.infrastructure.persistence.po.MenuDO;
import com.myow.user.system.infrastructure.persistence.po.RoleDO;
import com.myow.user.system.infrastructure.persistence.po.RoleMenuDO;
import com.myow.user.system.infrastructure.persistence.repository.MenuRepository;
import com.myow.user.system.infrastructure.persistence.repository.RoleMenuRepository;
import com.myow.user.system.infrastructure.persistence.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("RoleMenuService测试")
class RoleMenuServiceTest extends BaseServiceTest {

    @Mock
    private RoleMenuRepository roleMenuRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private RoleMenuService roleMenuService;

    @Test
    @DisplayName("创建角色菜单关联-成功")
    void createRoleMenu_Success() {
        CreateRoleMenuReqDTO createReqDTO = new CreateRoleMenuReqDTO();
        createReqDTO.setRoleId(1L);
        createReqDTO.setMenuId(2L);

        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        MenuDO mockMenuDO = new MenuDO();
        mockMenuDO.setMenuId(2L);
        RoleMenuDO mockRoleMenuDO = new RoleMenuDO();
        mockRoleMenuDO.setRoleId(1L);
        mockRoleMenuDO.setMenuId(2L);

        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);
        when(menuRepository.getById(2L)).thenReturn(mockMenuDO);
        when(roleMenuRepository.save(any(RoleMenuDO.class))).thenReturn(true);

        boolean result = roleMenuService.createRoleMenu(createReqDTO);

        assertThat(result).isTrue();
        verify(roleMenuRepository, times(1)).save(any(RoleMenuDO.class));
    }

    @Test
    @DisplayName("创建角色菜单关联-角色ID为空")
    void createRoleMenu_RoleIdNull() {
        CreateRoleMenuReqDTO createReqDTO = new CreateRoleMenuReqDTO();
        createReqDTO.setRoleId(null);
        createReqDTO.setMenuId(2L);

        assertThatThrownBy(() -> roleMenuService.createRoleMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("角色ID不能为空");
    }

    @Test
    @DisplayName("创建角色菜单关联-菜单ID为空")
    void createRoleMenu_MenuIdNull() {
        CreateRoleMenuReqDTO createReqDTO = new CreateRoleMenuReqDTO();
        createReqDTO.setRoleId(1L);
        createReqDTO.setMenuId(null);

        assertThatThrownBy(() -> roleMenuService.createRoleMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("菜单ID不能为空");
    }

    @Test
    @DisplayName("创建角色菜单关联-角色不存在")
    void createRoleMenu_RoleNotExist() {
        CreateRoleMenuReqDTO createReqDTO = new CreateRoleMenuReqDTO();
        createReqDTO.setRoleId(999L);
        createReqDTO.setMenuId(2L);

        when(roleRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> roleMenuService.createRoleMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ROLE_NOT_EXIST);
    }

    @Test
    @DisplayName("创建角色菜单关联-菜单不存在")
    void createRoleMenu_MenuNotExist() {
        CreateRoleMenuReqDTO createReqDTO = new CreateRoleMenuReqDTO();
        createReqDTO.setRoleId(1L);
        createReqDTO.setMenuId(999L);

        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);
        when(menuRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> roleMenuService.createRoleMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.MENU_NOT_EXIST);
    }

    @Test
    @DisplayName("创建角色菜单关联-关联已存在")
    void createRoleMenu_AlreadyExist() {
        CreateRoleMenuReqDTO createReqDTO = new CreateRoleMenuReqDTO();
        createReqDTO.setRoleId(1L);
        createReqDTO.setMenuId(2L);

        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        MenuDO mockMenuDO = new MenuDO();
        mockMenuDO.setMenuId(2L);
        RoleMenuDO mockRoleMenuDO = new RoleMenuDO();
        mockRoleMenuDO.setRoleId(1L);
        mockRoleMenuDO.setMenuId(2L);

        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);
        when(menuRepository.getById(2L)).thenReturn(mockMenuDO);
        when(roleMenuRepository.getByCompositeKey(1L, 2L)).thenReturn(mockRoleMenuDO);

        assertThatThrownBy(() -> roleMenuService.createRoleMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该角色菜单关联已存在");
    }

    @Test
    @DisplayName("更新角色菜单关联-成功")
    void updateRoleMenu_Success() {
        UpdateRoleMenuReqDTO updateReqDTO = new UpdateRoleMenuReqDTO();
        updateReqDTO.setRoleId(1L);
        updateReqDTO.setMenuId(2L);
        updateReqDTO.setOriginalRoleId(3L);
        updateReqDTO.setOriginalMenuId(4L);

        RoleDO mockRoleDO = new RoleDO();
        mockRoleDO.setRoleId(1L);
        MenuDO mockMenuDO = new MenuDO();
        mockMenuDO.setMenuId(2L);

        when(roleRepository.getById(1L)).thenReturn(mockRoleDO);
        when(menuRepository.getById(2L)).thenReturn(mockMenuDO);
        when(roleMenuRepository.getByCompositeKey(1L, 2L)).thenReturn(null);

        roleMenuService.updateRoleMenu(updateReqDTO);

        verify(roleMenuRepository, times(1)).removeByCompositeKey(3L, 4L);
        verify(roleMenuRepository, times(1)).save(any(RoleMenuDO.class));
    }

    @Test
    @DisplayName("删除角色菜单关联-成功")
    void deleteRoleMenu_Success() {
        RoleMenuDO mockRoleMenuDO = new RoleMenuDO();
        mockRoleMenuDO.setRoleId(1L);
        mockRoleMenuDO.setMenuId(2L);

        when(roleMenuRepository.getByCompositeKey(1L, 2L)).thenReturn(mockRoleMenuDO);
        when(roleMenuRepository.removeByCompositeKey(1L, 2L)).thenReturn(true);

        roleMenuService.deleteRoleMenu(1L, 2L);

        verify(roleMenuRepository, times(1)).removeByCompositeKey(1L, 2L);
    }

    @Test
    @DisplayName("删除角色菜单关联-关联不存在")
    void deleteRoleMenu_NotExist() {
        when(roleMenuRepository.getByCompositeKey(1L, 2L)).thenReturn(null);

        assertThatThrownBy(() -> roleMenuService.deleteRoleMenu(1L, 2L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("角色菜单关联不存在");
    }

    @Test
    @DisplayName("获取角色菜单关联-成功")
    void getRoleMenu_Success() {
        RoleMenuDO mockRoleMenuDO = new RoleMenuDO();
        mockRoleMenuDO.setRoleId(1L);
        mockRoleMenuDO.setMenuId(2L);

        when(roleMenuRepository.getByCompositeKey(1L, 2L)).thenReturn(mockRoleMenuDO);

        RoleMenuRespDTO roleMenuRespDTO = roleMenuService.getRoleMenu(1L, 2L);

        assertThat(roleMenuRespDTO).isNotNull();
        assertThat(roleMenuRespDTO.getRoleId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取角色菜单关联-关联不存在")
    void getRoleMenu_NotExist() {
        when(roleMenuRepository.getByCompositeKey(1L, 2L)).thenReturn(null);

        assertThatThrownBy(() -> roleMenuService.getRoleMenu(1L, 2L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("角色菜单关联不存在");
    }
}