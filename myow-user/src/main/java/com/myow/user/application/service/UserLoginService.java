package com.myow.user.application.service;

import cn.dev33.satoken.stp.StpUtil;
import com.myow.common.constant.StringConst;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.security.PasswordService;
import com.myow.user.system.application.dto.CreateLoginLogReqDTO;
import com.myow.user.system.application.service.LoginLogService;
import com.myow.user.application.dto.UserLoginReqDTO;
import com.myow.user.application.vo.UserLoginResultVO;
import com.myow.user.application.vo.UserMenuRespVO;
import com.myow.user.infrastructure.persistence.po.MenuDO;
import com.myow.user.infrastructure.persistence.po.RoleDO;
import com.myow.user.infrastructure.persistence.po.TenantUserDO;
import com.myow.user.infrastructure.persistence.repository.TenantUserRepository;
import com.myow.user.infrastructure.persistence.repository.UserPermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final TenantUserRepository tenantUserRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final LoginLogService loginLogService;
    private final SecurityPolicyService securityPolicyService;

    public UserLoginResultVO login(UserLoginReqDTO loginRequest, String ip, String userAgent) {
        TenantUserDO user = tenantUserRepository.getByLoginName(loginRequest.getLoginName(), false);
        try {
            validateLogin(user, loginRequest);
        } catch (BusinessException ex) {
            recordLoginLog(user, loginRequest, ip, userAgent, 0, ex.getMessage());
            throw ex;
        }

        String saTokenLoginId = loginRequest.getLoginClient() + StringConst.COLON + user.getUserId();
        StpUtil.login(saTokenLoginId, loginRequest.getLoginClient());
        markLoginSuccess(user, ip);
        recordLoginLog(user, loginRequest, ip, userAgent, 1, null);

        log.info("User login successful: {}, ip: {}, userAgent: {}", user.getNickName(), ip, userAgent);

        UserLoginResultVO loginResult = toLoginResult(user);
        loginResult.setToken(StpUtil.getTokenValue());
        return loginResult;
    }

    public void logout() {
        if (StpUtil.isLogin()) {
            log.info("User logout successful, loginId: {}", StpUtil.getLoginId());
            StpUtil.logout();
        }
    }

    public String refresh() {
        StpUtil.checkLogin();
        return StpUtil.getTokenValue();
    }

    private void validateLogin(TenantUserDO user, UserLoginReqDTO loginRequest) {
        if (Objects.isNull(user)) {
            throw new BusinessException(UserErrorCode.USERNAME_OR_PASSWORD_ERROR);
        }
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new BusinessException(UserErrorCode.LOGIN_FAIL_LOCK);
        }
        if (!Boolean.TRUE.equals(user.getStatus())) {
            throw new BusinessException(UserErrorCode.USER_ACCOUNT_DISABLED);
        }
        if (securityPolicyService.isCaptchaRequired(user.getTenantId(), user.getFailedLoginCount())
                && (isBlank(loginRequest.getCaptchaUuid()) || isBlank(loginRequest.getCaptchaCode()))) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "captcha is required");
        }
        if (!PasswordService.matchesPwd(loginRequest.getPassword(), user.getPassword())) {
            markLoginFailure(user);
            throw new BusinessException(UserErrorCode.USERNAME_OR_PASSWORD_ERROR);
        }
    }

    private void markLoginFailure(TenantUserDO user) {
        int failedCount = user.getFailedLoginCount() == null ? 0 : user.getFailedLoginCount();
        failedCount++;
        user.setFailedLoginCount(failedCount);
        int maxFailCount = securityPolicyService.getMaxFailCount(user.getTenantId());
        if (failedCount >= maxFailCount) {
            int lockMinutes = securityPolicyService.getLockMinutes(user.getTenantId());
            user.setLockedUntil(LocalDateTime.now().plusMinutes(lockMinutes));
        }
        tenantUserRepository.updateById(user);
    }

    private void markLoginSuccess(TenantUserDO user, String ip) {
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(ip);
        tenantUserRepository.updateById(user);
    }

    private void recordLoginLog(TenantUserDO user, UserLoginReqDTO loginRequest, String ip, String userAgent, Integer status, String failReason) {
        CreateLoginLogReqDTO logReqDTO = new CreateLoginLogReqDTO();
        logReqDTO.setTenantId(parseTenantId(user == null ? null : user.getTenantId()));
        logReqDTO.setUserId(user == null ? null : user.getUserId());
        logReqDTO.setLoginName(loginRequest.getLoginName());
        logReqDTO.setLoginType("PASSWORD");
        logReqDTO.setLoginClient(loginRequest.getLoginClient());
        logReqDTO.setLoginIp(ip);
        logReqDTO.setUserAgent(userAgent);
        logReqDTO.setStatus(status);
        logReqDTO.setFailReason(failReason);
        loginLogService.createLoginLog(logReqDTO);
    }

    private Long parseTenantId(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(tenantId);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private UserLoginResultVO toLoginResult(TenantUserDO user) {
        UserLoginResultVO result = new UserLoginResultVO();
        result.setUserId(user.getUserId());
        result.setTenantId(user.getTenantId());
        result.setUserCode(user.getUserCode());
        result.setLoginName(user.getLoginName());
        result.setNickName(user.getNickName());
        result.setPhone(user.getPhone());
        result.setEmail(user.getEmail());
        result.setAdminFlag(user.getAdminFlag());
        boolean passwordExpired = user.getPasswordExpireTime() != null && user.getPasswordExpireTime().isBefore(LocalDateTime.now());
        result.setMustChangePassword(Boolean.TRUE.equals(user.getMustChangePassword()) || passwordExpired);
        result.setPasswordExpireTime(user.getPasswordExpireTime());
        if (Boolean.TRUE.equals(result.getMustChangePassword())) {
            result.setMenuList(List.of());
        } else {
            result.setMenuList(getMenuList(user));
        }
        return result;
    }

    private List<UserMenuRespVO> getMenuList(TenantUserDO user) {
        List<RoleDO> roleList = userPermissionRepository.listRolesByUserId(user.getUserId());
        List<Long> roleIdList = roleList.stream()
                .map(RoleDO::getRoleId)
                .filter(Objects::nonNull)
                .toList();

        return userPermissionRepository.listMenusByRoleIds(roleIdList, user.getAdminFlag()).stream()
                .map(this::toMenuRespVO)
                .toList();
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
