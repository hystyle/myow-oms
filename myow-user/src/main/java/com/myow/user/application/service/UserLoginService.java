package com.myow.user.application.service;

import cn.dev33.satoken.stp.StpUtil;
import com.myow.common.constant.StringConst;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.common.security.PasswordService;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final TenantUserRepository tenantUserRepository;
    private final UserPermissionRepository userPermissionRepository;

    public UserLoginResultVO login(UserLoginReqDTO loginRequest, String ip, String userAgent) {
        TenantUserDO user = tenantUserRepository.getByLoginName(loginRequest.getLoginName(), false);
        validateLogin(user, loginRequest);

        String saTokenLoginId = loginRequest.getLoginClient() + StringConst.COLON + user.getUserId();
        StpUtil.login(saTokenLoginId, loginRequest.getLoginClient());

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

    private void validateLogin(TenantUserDO user, UserLoginReqDTO loginRequest) {
        if (Objects.isNull(user)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }
        if (!Boolean.TRUE.equals(user.getStatus())) {
            throw new BusinessException(UserErrorCode.USER_ACCOUNT_DISABLED);
        }
        if (!PasswordService.matchesPwd(loginRequest.getPassword(), user.getPassword())) {
            throw new BusinessException(UserErrorCode.USERNAME_OR_PASSWORD_ERROR);
        }
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
        result.setMenuList(getMenuList(user));
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
