package com.myow.system.application.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.RoleMenuApplicationConverter;
import com.myow.system.application.dto.CreateRoleMenuReqDTO;
import com.myow.system.application.dto.PageRoleMenuReqDTO;
import com.myow.system.application.dto.RoleMenuRespDTO;
import com.myow.system.application.dto.UpdateRoleMenuReqDTO;
import com.myow.system.domain.entity.RoleMenu;
import com.myow.system.infrastructure.converter.RoleMenuConverter;
import com.myow.system.infrastructure.persistence.po.MenuDO;
import com.myow.system.infrastructure.persistence.po.RoleDO;
import com.myow.system.infrastructure.persistence.po.RoleMenuDO;
import com.myow.system.infrastructure.persistence.repository.MenuRepository;
import com.myow.system.infrastructure.persistence.repository.RoleMenuRepository;
import com.myow.system.infrastructure.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleMenuService {

    private final RoleMenuRepository roleMenuRepository;
    private final RoleMenuApplicationConverter roleMenuApplicationConverter;
    private final RoleMenuConverter roleMenuConverter;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;

    public boolean createRoleMenu(CreateRoleMenuReqDTO createReqDTO) {
        validateRoleMenuForCreate(createReqDTO);
        RoleMenu roleMenu = roleMenuApplicationConverter.convert(createReqDTO);
        return roleMenuRepository.save(roleMenuConverter.toDo(roleMenu));
    }

    public void updateRoleMenu(UpdateRoleMenuReqDTO updateReqDTO) {
        validateRoleMenuForUpdate(updateReqDTO);
        roleMenuRepository.removeByCompositeKey(updateReqDTO.getOriginalRoleId(), updateReqDTO.getOriginalMenuId());
        RoleMenu newRoleMenu = new RoleMenu();
        newRoleMenu.setRoleId(updateReqDTO.getRoleId());
        newRoleMenu.setMenuId(updateReqDTO.getMenuId());
        roleMenuRepository.save(roleMenuConverter.toDo(newRoleMenu));
    }

    public void deleteRoleMenu(Long roleId, Long menuId) {
        RoleMenuDO existRoleMenu = roleMenuRepository.getByCompositeKey(roleId, menuId);
        if (Objects.isNull(existRoleMenu)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色菜单关联不存在");
        }
        roleMenuRepository.removeByCompositeKey(roleId, menuId);
    }

    public RoleMenuRespDTO getRoleMenu(Long roleId, Long menuId) {
        RoleMenuDO roleMenuDO = roleMenuRepository.getByCompositeKey(roleId, menuId);
        if (Objects.isNull(roleMenuDO)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色菜单关联不存在");
        }
        return roleMenuApplicationConverter.convert(roleMenuConverter.toEntity(roleMenuDO));
    }

    public PageResult<RoleMenuRespDTO> getRoleMenuPage(PageRoleMenuReqDTO pageRoleMenuReqDTO) {
        Page<RoleMenuDO> roleMenuDOPage = roleMenuRepository.selectPage(pageRoleMenuReqDTO);
        if (Objects.isNull(roleMenuDOPage) || roleMenuDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(roleMenuDOPage, roleMenuApplicationConverter::convert);
    }

    private void validateRoleMenuForCreate(CreateRoleMenuReqDTO createReqDTO) {
        if (Objects.isNull(createReqDTO.getRoleId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }
        
        if (Objects.isNull(createReqDTO.getMenuId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID不能为空");
        }
        
        RoleDO role = roleRepository.getById(createReqDTO.getRoleId());
        if (Objects.isNull(role)) {
            throw new BusinessException(UserErrorCode.ROLE_NOT_EXIST);
        }
        
        MenuDO menu = menuRepository.getById(createReqDTO.getMenuId());
        if (Objects.isNull(menu)) {
            throw new BusinessException(UserErrorCode.MENU_NOT_EXIST);
        }
        
        RoleMenuDO existRoleMenu = roleMenuRepository.getByCompositeKey(createReqDTO.getRoleId(), createReqDTO.getMenuId());
        if (Objects.nonNull(existRoleMenu)) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "该角色菜单关联已存在");
        }
    }

    private void validateRoleMenuForUpdate(UpdateRoleMenuReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getRoleId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getMenuId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getOriginalRoleId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "原角色ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getOriginalMenuId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "原菜单ID不能为空");
        }
        
        RoleDO role = roleRepository.getById(updateReqDTO.getRoleId());
        if (Objects.isNull(role)) {
            throw new BusinessException(UserErrorCode.ROLE_NOT_EXIST);
        }
        
        MenuDO menu = menuRepository.getById(updateReqDTO.getMenuId());
        if (Objects.isNull(menu)) {
            throw new BusinessException(UserErrorCode.MENU_NOT_EXIST);
        }
        
        RoleMenuDO existRoleMenu = roleMenuRepository.getByCompositeKey(updateReqDTO.getRoleId(), updateReqDTO.getMenuId());
        if (Objects.nonNull(existRoleMenu)) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "该角色菜单关联已存在");
        }
    }
}
