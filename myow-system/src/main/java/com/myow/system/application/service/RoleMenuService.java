package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.RoleMenuApplicationConverter;
import com.myow.system.application.dto.CreateRoleMenuReqDTO;
import com.myow.system.application.dto.PageRoleMenuReqDTO;
import com.myow.system.application.dto.RoleMenuRespDTO;
import com.myow.system.application.dto.UpdateRoleMenuReqDTO;
import com.myow.system.domain.entity.RoleMenu;
import com.myow.system.infrastructure.converter.RoleMenuConverter;
import com.myow.system.infrastructure.persistence.po.RoleMenuDO;
import com.myow.system.infrastructure.persistence.repository.RoleMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class RoleMenuService {

    private final RoleMenuRepository roleMenuRepository;
    private final RoleMenuApplicationConverter roleMenuApplicationConverter;
    private final RoleMenuConverter roleMenuConverter;

    public boolean createRoleMenu(CreateRoleMenuReqDTO createReqDTO) {
        RoleMenu roleMenu = roleMenuApplicationConverter.convert(createReqDTO);
        return roleMenuRepository.save(roleMenuConverter.toDo(roleMenu));
    }

    public void updateRoleMenu(UpdateRoleMenuReqDTO updateReqDTO) {
        roleMenuRepository.removeByCompositeKey(updateReqDTO.getOriginalRoleId(), updateReqDTO.getOriginalMenuId());
        RoleMenu newRoleMenu = new RoleMenu();
        newRoleMenu.setRoleId(updateReqDTO.getRoleId());
        newRoleMenu.setMenuId(updateReqDTO.getMenuId());
        roleMenuRepository.save(roleMenuConverter.toDo(newRoleMenu));
    }

    public void deleteRoleMenu(Long roleId, Long menuId) {
        roleMenuRepository.removeByCompositeKey(roleId, menuId);
    }

    public RoleMenuRespDTO getRoleMenu(Long roleId, Long menuId) {
        RoleMenuDO roleMenuDO = roleMenuRepository.getByCompositeKey(roleId, menuId);
        return roleMenuApplicationConverter.convert(roleMenuConverter.toEntity(roleMenuDO));
    }

    public PageResult<RoleMenuRespDTO> getRoleMenuPage(PageRoleMenuReqDTO pageRoleMenuReqDTO) {
        Page<RoleMenuDO> roleMenuDOPage = roleMenuRepository.selectPage(pageRoleMenuReqDTO);
        if (Objects.isNull(roleMenuDOPage) || roleMenuDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(roleMenuDOPage, roleMenuApplicationConverter::convert);
    }
}
