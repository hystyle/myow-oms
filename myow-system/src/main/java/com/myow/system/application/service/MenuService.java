package com.myow.system.application.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.MenuApplicationConverter;
import com.myow.system.application.dto.CreateMenuReqDTO;
import com.myow.system.application.dto.MenuRespDTO;
import com.myow.system.application.dto.PageMenuReqDTO;
import com.myow.system.application.dto.UpdateMenuReqDTO;
import com.myow.system.domain.entity.Menu;
import com.myow.system.infrastructure.converter.MenuConverter;
import com.myow.system.infrastructure.persistence.po.MenuDO;
import com.myow.system.infrastructure.persistence.repository.MenuRepository;
import com.myow.system.infrastructure.persistence.repository.RoleMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MenuService {

    private static final String MENU_TYPE_DIR = "M";
    private static final String MENU_TYPE_MENU = "C";
    private static final String MENU_TYPE_BUTTON = "F";

    private final MenuRepository menuRepository;
    private final MenuApplicationConverter menuApplicationConverter;
    private final MenuConverter menuConverter;
    private final RoleMenuRepository roleMenuRepository;

    public Long createMenu(CreateMenuReqDTO createReqDTO) {
        validateMenuForCreate(createReqDTO);
        Menu menu = menuApplicationConverter.convert(createReqDTO);
        menuRepository.save(menuConverter.toDo(menu));
        return menu.getMenuId();
    }

    public void updateMenu(UpdateMenuReqDTO updateReqDTO) {
        validateMenuForUpdate(updateReqDTO);
        Menu menu = menuApplicationConverter.convert(updateReqDTO);
        menuRepository.updateById(menuConverter.toDo(menu));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        MenuDO existMenu = menuRepository.getById(id);
        if (Objects.isNull(existMenu)) {
            throw new BusinessException(UserErrorCode.MENU_NOT_EXIST);
        }
        
        Long childCount = menuRepository.count(Wrappers.lambdaQuery(MenuDO.class)
            .eq(MenuDO::getParentId, id));
        if (childCount > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "该菜单存在子菜单，无法删除");
        }
        
        Long roleCount = roleMenuRepository.count(Wrappers.lambdaQuery(com.myow.system.infrastructure.persistence.po.RoleMenuDO.class)
            .eq(com.myow.system.infrastructure.persistence.po.RoleMenuDO::getMenuId, id));
        if (roleCount > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "该菜单已被角色使用，无法删除");
        }
        
        menuRepository.removeById(id);
    }

    public MenuRespDTO getMenu(Long id) {
        MenuDO menuDO = menuRepository.getById(id);
        if (Objects.isNull(menuDO)) {
            throw new BusinessException(UserErrorCode.MENU_NOT_EXIST);
        }
        return menuApplicationConverter.convert(menuConverter.toEntity(menuDO));
    }

    public PageResult<MenuRespDTO> getMenuPage(PageMenuReqDTO pageMenuReqDTO) {
        Page<MenuDO> menuDOPage = menuRepository.selectPage(pageMenuReqDTO);
        if (Objects.isNull(menuDOPage) || menuDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(menuDOPage, menuApplicationConverter::convert);
    }

    private void validateMenuForCreate(CreateMenuReqDTO createReqDTO) {
        if (StrUtil.isBlank(createReqDTO.getMenuName())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单名称不能为空");
        }
        
        if (StrUtil.isBlank(createReqDTO.getMenuType())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单类型不能为空");
        }
        
        List<String> validMenuTypes = List.of(MENU_TYPE_DIR, MENU_TYPE_MENU, MENU_TYPE_BUTTON);
        if (!validMenuTypes.contains(createReqDTO.getMenuType())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单类型不正确");
        }
        
        if (Objects.equals(createReqDTO.getMenuType(), MENU_TYPE_BUTTON) && StrUtil.isBlank(createReqDTO.getPerms())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "按钮类型菜单必须设置权限标识");
        }
        
        if (!Objects.equals(createReqDTO.getMenuType(), MENU_TYPE_BUTTON) && StrUtil.isBlank(createReqDTO.getPath())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "目录或菜单类型必须设置路由地址");
        }
        
        if (Objects.equals(createReqDTO.getMenuType(), MENU_TYPE_MENU) && StrUtil.isBlank(createReqDTO.getComponent())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单类型必须设置组件路径");
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getIsFrame()) && 
            !Objects.equals(createReqDTO.getIsFrame(), "0") && 
            !Objects.equals(createReqDTO.getIsFrame(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "是否外链参数不正确");
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getIsCache()) && 
            !Objects.equals(createReqDTO.getIsCache(), "0") && 
            !Objects.equals(createReqDTO.getIsCache(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "是否缓存参数不正确");
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getVisible()) && 
            !Objects.equals(createReqDTO.getVisible(), "0") && 
            !Objects.equals(createReqDTO.getVisible(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "显示状态参数不正确");
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getStatus()) && 
            !Objects.equals(createReqDTO.getStatus(), "0") && 
            !Objects.equals(createReqDTO.getStatus(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单状态参数不正确");
        }
        
        if (Objects.nonNull(createReqDTO.getParentId())) {
            MenuDO parentMenu = menuRepository.getById(createReqDTO.getParentId());
            if (Objects.isNull(parentMenu)) {
                throw new BusinessException(UserErrorCode.MENU_NOT_EXIST, "父菜单不存在");
            }
            
            if (Objects.equals(parentMenu.getMenuType(), MENU_TYPE_BUTTON)) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "父菜单不能是按钮类型");
            }
        }
        
        Long parentId = Objects.isNull(createReqDTO.getParentId()) ? 0L : createReqDTO.getParentId();
        Long countByName = menuRepository.count(Wrappers.lambdaQuery(MenuDO.class)
            .eq(MenuDO::getMenuName, createReqDTO.getMenuName())
            .eq(MenuDO::getParentId, parentId));
        if (countByName > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "同一父菜单下菜单名称已存在");
        }
    }

    private void validateMenuForUpdate(UpdateMenuReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getMenuId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID不能为空");
        }
        
        MenuDO existMenu = menuRepository.getById(updateReqDTO.getMenuId());
        if (Objects.isNull(existMenu)) {
            throw new BusinessException(UserErrorCode.MENU_NOT_EXIST);
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getMenuType())) {
            List<String> validMenuTypes = List.of(MENU_TYPE_DIR, MENU_TYPE_MENU, MENU_TYPE_BUTTON);
            if (!validMenuTypes.contains(updateReqDTO.getMenuType())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "菜单类型不正确");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getMenuType()) && 
            Objects.equals(updateReqDTO.getMenuType(), MENU_TYPE_BUTTON) && 
            StrUtil.isBlank(updateReqDTO.getPerms())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "按钮类型菜单必须设置权限标识");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getMenuType()) && 
            !Objects.equals(updateReqDTO.getMenuType(), MENU_TYPE_BUTTON) && 
            StrUtil.isBlank(updateReqDTO.getPath())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "目录或菜单类型必须设置路由地址");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getMenuType()) && 
            Objects.equals(updateReqDTO.getMenuType(), MENU_TYPE_MENU) && 
            StrUtil.isBlank(updateReqDTO.getComponent())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单类型必须设置组件路径");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getIsFrame()) && 
            !Objects.equals(updateReqDTO.getIsFrame(), "0") && 
            !Objects.equals(updateReqDTO.getIsFrame(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "是否外链参数不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getIsCache()) && 
            !Objects.equals(updateReqDTO.getIsCache(), "0") && 
            !Objects.equals(updateReqDTO.getIsCache(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "是否缓存参数不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getVisible()) && 
            !Objects.equals(updateReqDTO.getVisible(), "0") && 
            !Objects.equals(updateReqDTO.getVisible(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "显示状态参数不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getStatus()) && 
            !Objects.equals(updateReqDTO.getStatus(), "0") && 
            !Objects.equals(updateReqDTO.getStatus(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单状态参数不正确");
        }
        
        if (Objects.nonNull(updateReqDTO.getParentId())) {
            if (Objects.equals(updateReqDTO.getParentId(), updateReqDTO.getMenuId())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "不能将菜单的父菜单设置为自己");
            }
            
            MenuDO parentMenu = menuRepository.getById(updateReqDTO.getParentId());
            if (Objects.isNull(parentMenu)) {
                throw new BusinessException(UserErrorCode.MENU_NOT_EXIST, "父菜单不存在");
            }
            
            if (Objects.equals(parentMenu.getMenuType(), MENU_TYPE_BUTTON)) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "父菜单不能是按钮类型");
            }
            
            if (isChildMenu(updateReqDTO.getMenuId(), updateReqDTO.getParentId())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "不能将菜单的父菜单设置为其子菜单");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getMenuName())) {
            Long parentId = Objects.isNull(updateReqDTO.getParentId()) ? 
                Objects.isNull(existMenu.getParentId()) ? 0L : existMenu.getParentId() : 
                updateReqDTO.getParentId();
            
            Long countByName = menuRepository.count(Wrappers.lambdaQuery(MenuDO.class)
                .eq(MenuDO::getMenuName, updateReqDTO.getMenuName())
                .eq(MenuDO::getParentId, parentId)
                .ne(MenuDO::getMenuId, updateReqDTO.getMenuId()));
            if (countByName > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "同一父菜单下菜单名称已存在");
            }
        }
    }

    private boolean isChildMenu(Long menuId, Long parentId) {
        List<MenuDO> childMenus = menuRepository.list(Wrappers.lambdaQuery(MenuDO.class)
            .eq(MenuDO::getParentId, menuId));
        
        if (childMenus.isEmpty()) {
            return false;
        }
        
        for (MenuDO child : childMenus) {
            if (Objects.equals(child.getMenuId(), parentId)) {
                return true;
            }
            if (isChildMenu(child.getMenuId(), parentId)) {
                return true;
            }
        }
        
        return false;
    }
}
