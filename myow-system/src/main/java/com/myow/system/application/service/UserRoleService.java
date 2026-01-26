package com.myow.system.application.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.UserRoleApplicationConverter;
import com.myow.system.application.dto.CreateUserRoleReqDTO;
import com.myow.system.application.dto.PageUserRoleReqDTO;
import com.myow.system.application.dto.UpdateUserRoleReqDTO;
import com.myow.system.application.dto.UserRoleRespDTO;
import com.myow.system.domain.entity.UserRole;
import com.myow.system.infrastructure.converter.UserRoleConverter;
import com.myow.system.infrastructure.persistence.po.RoleDO;
import com.myow.system.infrastructure.persistence.po.UserDO;
import com.myow.system.infrastructure.persistence.po.UserRoleDO;
import com.myow.system.infrastructure.persistence.repository.RoleRepository;
import com.myow.system.infrastructure.persistence.repository.UserRepository;
import com.myow.system.infrastructure.persistence.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRoleApplicationConverter userRoleApplicationConverter;
    private final UserRoleConverter userRoleConverter;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public boolean createUserRole(CreateUserRoleReqDTO createReqDTO) {
        validateUserRoleForCreate(createReqDTO);
        UserRole userRole = userRoleApplicationConverter.convert(createReqDTO);
        return userRoleRepository.save(userRoleConverter.toDo(userRole));
    }

    public void updateUserRole(UpdateUserRoleReqDTO updateReqDTO) {
        validateUserRoleForUpdate(updateReqDTO);
        userRoleRepository.removeByCompositeKey(updateReqDTO.getOriginalUserId(), updateReqDTO.getOriginalRoleId());
        UserRole newUserRole = new UserRole();
        newUserRole.setUserId(updateReqDTO.getUserId());
        newUserRole.setRoleId(updateReqDTO.getRoleId());
        userRoleRepository.save(userRoleConverter.toDo(newUserRole));
    }

    public void deleteUserRole(Long userId, Long roleId) {
        UserRoleDO existUserRole = userRoleRepository.getByCompositeKey(userId, roleId);
        if (Objects.isNull(existUserRole)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户角色关联不存在");
        }
        userRoleRepository.removeByCompositeKey(userId, roleId);
    }

    public UserRoleRespDTO getUserRole(Long userId, Long roleId) {
        UserRoleDO userRoleDO = userRoleRepository.getByCompositeKey(userId, roleId);
        if (Objects.isNull(userRoleDO)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户角色关联不存在");
        }
        return userRoleApplicationConverter.convert(userRoleConverter.toEntity(userRoleDO));
    }

    public PageResult<UserRoleRespDTO> getUserRolePage(PageUserRoleReqDTO pageUserRoleReqDTO) {
        Page<UserRoleDO> userRoleDOPage = userRoleRepository.selectPage(pageUserRoleReqDTO);
        return MyPageUtil.of(userRoleDOPage, userRoleApplicationConverter::convert);
    }

    private void validateUserRoleForCreate(CreateUserRoleReqDTO createReqDTO) {
        if (Objects.isNull(createReqDTO.getUserId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }
        
        if (Objects.isNull(createReqDTO.getRoleId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }
        
        UserDO user = userRepository.getById(createReqDTO.getUserId());
        if (Objects.isNull(user)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }
        
        RoleDO role = roleRepository.getById(createReqDTO.getRoleId());
        if (Objects.isNull(role)) {
            throw new BusinessException(UserErrorCode.ROLE_NOT_EXIST);
        }
        
        UserRoleDO existUserRole = userRoleRepository.getByCompositeKey(createReqDTO.getUserId(), createReqDTO.getRoleId());
        if (Objects.nonNull(existUserRole)) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "该用户角色关联已存在");
        }
    }

    private void validateUserRoleForUpdate(UpdateUserRoleReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getUserId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getRoleId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getOriginalUserId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "原用户ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getOriginalRoleId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "原角色ID不能为空");
        }
        
        UserDO user = userRepository.getById(updateReqDTO.getUserId());
        if (Objects.isNull(user)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }
        
        RoleDO role = roleRepository.getById(updateReqDTO.getRoleId());
        if (Objects.isNull(role)) {
            throw new BusinessException(UserErrorCode.ROLE_NOT_EXIST);
        }
        
        UserRoleDO existUserRole = userRoleRepository.getByCompositeKey(updateReqDTO.getUserId(), updateReqDTO.getRoleId());
        if (Objects.nonNull(existUserRole)) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "该用户角色关联已存在");
        }
    }
}
