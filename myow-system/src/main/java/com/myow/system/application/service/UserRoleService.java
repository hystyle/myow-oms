package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.UserRoleApplicationConverter;
import com.myow.system.application.dto.CreateUserRoleReqDTO;
import com.myow.system.application.dto.PageUserRoleReqDTO;
import com.myow.system.application.dto.UpdateUserRoleReqDTO;
import com.myow.system.application.dto.UserRoleRespDTO;
import com.myow.system.domain.entity.UserRole;
import com.myow.system.infrastructure.converter.UserRoleConverter;
import com.myow.system.infrastructure.persistence.po.UserRoleDO;
import com.myow.system.infrastructure.persistence.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRoleApplicationConverter userRoleApplicationConverter;
    private final UserRoleConverter userRoleConverter;

    public boolean createUserRole(CreateUserRoleReqDTO createReqDTO) {
        UserRole userRole = userRoleApplicationConverter.convert(createReqDTO);
        return userRoleRepository.save(userRoleConverter.toDo(userRole));
    }

    public void updateUserRole(UpdateUserRoleReqDTO updateReqDTO) {
        userRoleRepository.removeByCompositeKey(updateReqDTO.getOriginalUserId(), updateReqDTO.getOriginalRoleId());
        UserRole newUserRole = new UserRole();
        newUserRole.setUserId(updateReqDTO.getUserId());
        newUserRole.setRoleId(updateReqDTO.getRoleId());
        userRoleRepository.save(userRoleConverter.toDo(newUserRole));
    }

    public void deleteUserRole(Long userId, Long roleId) {
        userRoleRepository.removeByCompositeKey(userId, roleId);
    }

    public UserRoleRespDTO getUserRole(Long userId, Long roleId) {
        UserRoleDO userRoleDO = userRoleRepository.getByCompositeKey(userId, roleId);
        return userRoleApplicationConverter.convert(userRoleConverter.toEntity(userRoleDO));
    }

    public PageResult<UserRoleRespDTO> getUserRolePage(PageUserRoleReqDTO pageUserRoleReqDTO) {
        Page<UserRoleDO> userRoleDOPage = userRoleRepository.selectPage(pageUserRoleReqDTO);
        return MyPageUtil.of(userRoleDOPage, userRoleApplicationConverter::convert);
    }
}
