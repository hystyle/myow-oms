package com.myow.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.system.BaseServiceTest;
import com.myow.system.application.dto.CreateMenuReqDTO;
import com.myow.system.application.dto.MenuRespDTO;
import com.myow.system.application.dto.UpdateMenuReqDTO;
import com.myow.system.infrastructure.persistence.po.MenuDO;
import com.myow.system.infrastructure.persistence.repository.MenuRepository;
import com.myow.system.infrastructure.persistence.repository.RoleMenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("MenuService测试")
class MenuServiceTest extends BaseServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RoleMenuRepository roleMenuRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("创建菜单-成功")
    void createMenu_Success() {
        CreateMenuReqDTO createReqDTO = new CreateMenuReqDTO();
        createReqDTO.setMenuName("测试菜单");
        createReqDTO.setMenuType("C");
        createReqDTO.setPath("/test");
        createReqDTO.setComponent("TestComponent");
        createReqDTO.setParentId(0L);

        when(menuRepository.save(any(MenuDO.class))).thenReturn(true);

        Long menuId = menuService.createMenu(createReqDTO);

        assertThat(menuId).isNotNull();
        verify(menuRepository, times(1)).save(any(MenuDO.class));
    }

    @Test
    @DisplayName("创建菜单-菜单名称为空")
    void createMenu_MenuNameBlank() {
        CreateMenuReqDTO createReqDTO = new CreateMenuReqDTO();
        createReqDTO.setMenuName("");
        createReqDTO.setMenuType("C");

        assertThatThrownBy(() -> menuService.createMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("菜单名称不能为空");
    }

    @Test
    @DisplayName("创建菜单-菜单类型为空")
    void createMenu_MenuTypeBlank() {
        CreateMenuReqDTO createReqDTO = new CreateMenuReqDTO();
        createReqDTO.setMenuName("测试菜单");
        createReqDTO.setMenuType("");

        assertThatThrownBy(() -> menuService.createMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("菜单类型不能为空");
    }

    @Test
    @DisplayName("创建菜单-菜单类型错误")
    void createMenu_MenuTypeError() {
        CreateMenuReqDTO createReqDTO = new CreateMenuReqDTO();
        createReqDTO.setMenuName("测试菜单");
        createReqDTO.setMenuType("X");

        assertThatThrownBy(() -> menuService.createMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("菜单类型不正确");
    }

    @Test
    @DisplayName("创建菜单-按钮类型必须设置权限标识")
    void createMenu_ButtonMustHavePerms() {
        CreateMenuReqDTO createReqDTO = new CreateMenuReqDTO();
        createReqDTO.setMenuName("测试按钮");
        createReqDTO.setMenuType("F");
        createReqDTO.setPerms("");

        assertThatThrownBy(() -> menuService.createMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("按钮类型菜单必须设置权限标识");
    }

    @Test
    @DisplayName("创建菜单-目录或菜单必须设置路由地址")
    void createMenu_DirOrMenuMustHavePath() {
        CreateMenuReqDTO createReqDTO = new CreateMenuReqDTO();
        createReqDTO.setMenuName("测试菜单");
        createReqDTO.setMenuType("C");
        createReqDTO.setPath("");

        assertThatThrownBy(() -> menuService.createMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("目录或菜单类型必须设置路由地址");
    }

    @Test
    @DisplayName("创建菜单-菜单类型必须设置组件路径")
    void createMenu_MenuMustHaveComponent() {
        CreateMenuReqDTO createReqDTO = new CreateMenuReqDTO();
        createReqDTO.setMenuName("测试菜单");
        createReqDTO.setMenuType("C");
        createReqDTO.setPath("/test");
        createReqDTO.setComponent("");

        assertThatThrownBy(() -> menuService.createMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("菜单类型必须设置组件路径");
    }

    @Test
    @DisplayName("创建菜单-菜单名称已存在")
    void createMenu_MenuNameAlreadyExist() {
        CreateMenuReqDTO createReqDTO = new CreateMenuReqDTO();
        createReqDTO.setMenuName("existing_menu");
        createReqDTO.setMenuType("C");
        createReqDTO.setPath("/test");
        createReqDTO.setComponent("TestComponent");

        when(menuRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> menuService.createMenu(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("更新菜单-成功")
    void updateMenu_Success() {
        UpdateMenuReqDTO updateReqDTO = new UpdateMenuReqDTO();
        updateReqDTO.setMenuId(1L);
        updateReqDTO.setMenuName("更新菜单");

        MenuDO mockMenuDO = new MenuDO();
        mockMenuDO.setMenuId(1L);
        when(menuRepository.getById(1L)).thenReturn(mockMenuDO);
        when(menuRepository.updateById(any(MenuDO.class))).thenReturn(true);

        menuService.updateMenu(updateReqDTO);

        verify(menuRepository, times(1)).updateById(any(MenuDO.class));
    }

    @Test
    @DisplayName("更新菜单-菜单不存在")
    void updateMenu_MenuNotExist() {
        UpdateMenuReqDTO updateReqDTO = new UpdateMenuReqDTO();
        updateReqDTO.setMenuId(999L);

        when(menuRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> menuService.updateMenu(updateReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.MENU_NOT_EXIST);
    }

    @Test
    @DisplayName("删除菜单-成功")
    void deleteMenu_Success() {
        MenuDO mockMenuDO = new MenuDO();
        mockMenuDO.setMenuId(1L);
        when(menuRepository.getById(1L)).thenReturn(mockMenuDO);
        when(menuRepository.count(any())).thenReturn(0L);
        when(roleMenuRepository.count(any())).thenReturn(0L);
        when(menuRepository.removeById(1L)).thenReturn(true);

        menuService.deleteMenu(1L);

        verify(menuRepository, times(1)).removeById(1L);
    }

    @Test
    @DisplayName("删除菜单-菜单不存在")
    void deleteMenu_MenuNotExist() {
        when(menuRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> menuService.deleteMenu(999L))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.MENU_NOT_EXIST);
    }

    @Test
    @DisplayName("删除菜单-存在子菜单")
    void deleteMenu_HasChildren() {
        MenuDO mockMenuDO = new MenuDO();
        mockMenuDO.setMenuId(1L);
        when(menuRepository.getById(1L)).thenReturn(mockMenuDO);
        when(menuRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> menuService.deleteMenu(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该菜单存在子菜单，无法删除");
    }

    @Test
    @DisplayName("删除菜单-已被角色使用")
    void deleteMenu_UsedByRole() {
        MenuDO mockMenuDO = new MenuDO();
        mockMenuDO.setMenuId(1L);
        when(menuRepository.getById(1L)).thenReturn(mockMenuDO);
        when(menuRepository.count(any())).thenReturn(0L);
        when(roleMenuRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> menuService.deleteMenu(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该菜单已被角色使用，无法删除");
    }

    @Test
    @DisplayName("获取菜单-成功")
    void getMenu_Success() {
        MenuDO mockMenuDO = new MenuDO();
        mockMenuDO.setMenuId(1L);
        mockMenuDO.setMenuName("测试菜单");
        when(menuRepository.getById(1L)).thenReturn(mockMenuDO);

        MenuRespDTO menuRespDTO = menuService.getMenu(1L);

        assertThat(menuRespDTO).isNotNull();
        assertThat(menuRespDTO.getMenuId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取菜单-菜单不存在")
    void getMenu_MenuNotExist() {
        when(menuRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> menuService.getMenu(999L))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.MENU_NOT_EXIST);
    }
}