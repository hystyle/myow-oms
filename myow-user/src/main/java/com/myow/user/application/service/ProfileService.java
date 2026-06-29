package com.myow.user.application.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.myow.common.exception.BusinessException;
import com.myow.common.port.DeptInfoPort;
import com.myow.common.port.PositionInfoPort;
import com.myow.common.port.UserLoginCachePort;
import com.myow.common.response.UserErrorCode;
import com.myow.common.security.PasswordService;
import com.myow.common.security.UserContext;
import com.myow.user.application.dto.ChangePasswordReqDTO;
import com.myow.user.application.dto.UpdateProfileReqDTO;
import com.myow.user.application.vo.UserMenuRespVO;
import com.myow.user.application.vo.UserRespVO;
import com.myow.user.infrastructure.persistence.po.MenuDO;
import com.myow.user.infrastructure.persistence.po.RoleDO;
import com.myow.user.infrastructure.persistence.po.TenantUserDO;
import com.myow.user.infrastructure.persistence.repository.TenantUserRepository;
import com.myow.user.infrastructure.persistence.repository.UserPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final TenantUserRepository tenantUserRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final UserLoginCachePort userLoginCachePort;
    private final DeptInfoPort deptInfoPort;
    private final PositionInfoPort positionInfoPort;
    private final SecurityPolicyService securityPolicyService;

    public UserRespVO getCurrentUser() {
        TenantUserDO user = getRequiredCurrentUser();
        UserRespVO result = toRespVO(user);
        fillDeptAndPosition(result);
        result.setRoleIdList(listRoles(user.getUserId()).stream()
                .map(RoleDO::getRoleId)
                .filter(Objects::nonNull)
                .toList());
        result.setRoleNameList(listRoles(user.getUserId()).stream()
                .map(RoleDO::getRoleName)
                .filter(StrUtil::isNotBlank)
                .toList());
        return result;
    }

    public List<UserMenuRespVO> getCurrentMenus() {
        TenantUserDO user = getRequiredCurrentUser();
        return listMenus(user).stream()
                .map(this::toMenuRespVO)
                .toList();
    }

    public List<String> getCurrentPermissions() {
        TenantUserDO user = getRequiredCurrentUser();
        return listMenus(user).stream()
                .map(MenuDO::getApiPerms)
                .filter(StrUtil::isNotBlank)
                .flatMap(apiPerms -> Arrays.stream(apiPerms.split(",")))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(UpdateProfileReqDTO reqDTO) {
        TenantUserDO currentUser = getRequiredCurrentUser();

        TenantUserDO user = new TenantUserDO();
        user.setUserId(currentUser.getUserId());
        user.setNickName(reqDTO.getNickName());
        user.setEmail(reqDTO.getEmail());
        user.setPhone(reqDTO.getPhone());
        user.setGender(reqDTO.getGender() == null ? null : String.valueOf(reqDTO.getGender()));
        user.setAvatar(reqDTO.getAvatar());
        tenantUserRepository.updateById(user);
        userLoginCachePort.clearUserLoginCache(currentUser.getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void changePassword(ChangePasswordReqDTO reqDTO) {
        TenantUserDO user = getRequiredCurrentUser();
        if (!Objects.equals(reqDTO.getNewPassword(), reqDTO.getConfirmPassword())) {
            throw new BusinessException(UserErrorCode.USER_STATUS_ERROR);
        }
        if (!PasswordService.matchesPwd(reqDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException(UserErrorCode.USERNAME_OR_PASSWORD_ERROR);
        }
        if (PasswordService.matchesPwd(reqDTO.getNewPassword(), user.getPassword())) {
            throw new BusinessException(UserErrorCode.USER_STATUS_ERROR);
        }
        securityPolicyService.validatePassword(user.getTenantId(), reqDTO.getNewPassword());

        user.setPassword(PasswordService.getEncryptPwd(reqDTO.getNewPassword()));
        user.setMustChangePassword(false);
        user.setPasswordUpdateTime(LocalDateTime.now());
        user.setPasswordExpireTime(securityPolicyService.calculatePasswordExpireTime(user.getTenantId()));
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        tenantUserRepository.updateById(user);
        userLoginCachePort.clearUserLoginCache(user.getUserId());
        StpUtil.logout();
    }

    private TenantUserDO getRequiredCurrentUser() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(UserErrorCode.LOGIN_STATE_INVALID);
        }
        TenantUserDO user = tenantUserRepository.getById(userId);
        if (user == null || Boolean.TRUE.equals(user.getDeletedFlag())) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }
        return user;
    }

    private List<RoleDO> listRoles(Long userId) {
        return userPermissionRepository.listRolesByUserId(userId);
    }

    private List<MenuDO> listMenus(TenantUserDO user) {
        List<Long> roleIdList = listRoles(user.getUserId()).stream()
                .map(RoleDO::getRoleId)
                .filter(Objects::nonNull)
                .toList();
        return userPermissionRepository.listMenusByRoleIds(roleIdList, user.getAdminFlag());
    }

    private void fillDeptAndPosition(UserRespVO result) {
        if (result.getDeptId() != null) {
            Map<Long, String> deptNameMap = deptInfoPort.getDeptNameMap(List.of(result.getDeptId()));
            result.setDeptName(deptNameMap.get(result.getDeptId()));
        }
        if (result.getPositionId() != null) {
            Map<Long, String> positionNameMap = positionInfoPort.getPositionNameMap(List.of(result.getPositionId()));
            result.setPositionName(positionNameMap.get(result.getPositionId()));
        }
    }

    private UserRespVO toRespVO(TenantUserDO user) {
        UserRespVO result = new UserRespVO();
        result.setUserId(user.getUserId());
        result.setTenantId(user.getTenantId());
        result.setDeptId(user.getDeptId());
        result.setPositionId(user.getPositionId());
        result.setUserName(user.getLoginName());
        result.setNickName(user.getNickName());
        result.setUserType(user.getUserType());
        result.setEmail(user.getEmail());
        result.setPhone(user.getPhone());
        result.setGender(user.getGender());
        result.setAvatar(user.getAvatar());
        result.setStatus(user.getStatus() == null ? null : String.valueOf(user.getStatus()));
        result.setCreateTime(user.getCreateTime());
        result.setRemark(user.getRemark());
        result.setAdminFlag(user.getAdminFlag());
        result.setFailedLoginCount(user.getFailedLoginCount());
        result.setLockedUntil(user.getLockedUntil());
        result.setPasswordUpdateTime(user.getPasswordUpdateTime());
        result.setPasswordExpireTime(user.getPasswordExpireTime());
        result.setMustChangePassword(user.getMustChangePassword());
        result.setLastLoginTime(user.getLastLoginTime());
        result.setLastLoginIp(user.getLastLoginIp());
        return result;
    }

    private UserMenuRespVO toMenuRespVO(MenuDO menu) {
        UserMenuRespVO result = new UserMenuRespVO();
        result.setMenuId(menu.getMenuId());
        result.setMenuName(menu.getMenuName());
        result.setParentId(menu.getParentId());
        result.setSort(menu.getSort());
        result.setPath(menu.getPath());
        result.setComponent(menu.getComponent());
        result.setQueryParam(menu.getQueryParam());
        result.setIsFrame(menu.getIsFrame());
        result.setIsCache(menu.getIsCache());
        result.setMenuType(menu.getMenuType());
        result.setVisible(menu.getVisible());
        result.setStatus(menu.getStatus());
        result.setApiPerms(menu.getApiPerms());
        result.setIcon(menu.getIcon());
        result.setRemark(menu.getRemark());
        return result;
    }

}
