package com.myow.system.application.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.UUID;
import com.myow.common.constant.StringConst;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.system.application.converter.UserApplicationConverter;
import com.myow.system.application.dto.LoginReqDTO;
import com.myow.system.application.dto.MenuRespDTO;
import com.myow.system.application.dto.RoleRespDTO;
import com.myow.system.application.vo.LoginResultVO;
import com.myow.system.application.vo.UserPermission;
import com.myow.system.domain.enums.LoginResultEnum;
import com.myow.system.infrastructure.persistence.po.UserDO;
import com.myow.system.infrastructure.persistence.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: yss
 * @date: 2026-01-28 21:21
 * @description: 登录服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements StpInterface {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final MenuService menuService;

    private final LoginCacheService loginCacheService;
    private final UserApplicationConverter userApplicationConverter;

    /**
     * 员工登录
     *
     * @return 返回用户登录信息
     */
    public LoginResultVO login(LoginReqDTO loginRequest, String ip, String userAgent) {
        UserDO user = userRepository.getByLoginName(loginRequest.getLoginName(), false);

        validateLogin(user, loginRequest, ip, userAgent, loginRequest.getLoginClient());

        String saTokenLoginId = loginRequest.getLoginClient() + StringConst.COLON + user.getUserId();

        // 登录并生成 Token
        StpUtil.login(saTokenLoginId, loginRequest.getLoginClient());

        log.info("User login successful: {}", user.getNickName());

        String token = StpUtil.getTokenValue();
        LoginResultVO loginResult = getLoginResult(user);

        // 设置token
        loginResult.setToken(token);

        // 保存登录记录
        saveLoginLog(user, ip, userAgent, LoginResultEnum.LOGIN_SUCCESS, loginRequest.getLoginClient());

        // 更新用户权限
        loginCacheService.loadUserPermission(user.getUserId());

        return loginResult;
    }

    /**
     * 校验登录
     */
    private void validateLogin(UserDO user, LoginReqDTO loginRequest, String ip, String userAgent, String loginClient) {
        // 校验用户是否存在
        if (Objects.isNull(user)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }

        // 是否可用
        if (!user.getStatus()) {
            throw new BusinessException(UserErrorCode.USER_ACCOUNT_DISABLED);
        }

        // 校验密码
        if (!SecurityPasswordService.matchesPwd(loginRequest.getPassword(), user.getPassword())) {
            saveLoginLog(user, ip, userAgent, LoginResultEnum.LOGIN_FAIL, loginClient);
            throw new BusinessException(UserErrorCode.USERNAME_OR_PASSWORD_ERROR);
        }
    }

    /**
     * 保存登录日志
     */
    private void saveLoginLog(UserDO user, String ip, String userAgent, LoginResultEnum loginResultEnum, String loginClient) {
        // TODO

    }

    /**
     * 获取登录者信息
     */
    public LoginResultVO getLoginResult(UserDO user) {
        LoginResultVO loginResultVO = userApplicationConverter.toLoginResultVO(user);

        // 前端菜单和功能点清单
        List<RoleRespDTO> roleVOList = roleService.getRoleByUserId(user.getUserId());
        List<MenuRespDTO> menuAndPointsList = menuService.getMenuList(roleVOList.stream().map(RoleRespDTO::getRoleId).collect(Collectors.toList()), user.getAdminFlag());
        loginResultVO.setMenuList(menuAndPointsList);

        return loginResultVO;
    }

    /**
     * 获取当前登录用户信息
     */
    public LoginResultVO getCurrentUser(String userId, HttpServletRequest request) {
        if (userId == null) {
            return null;
        }

        UserDO user = userRepository.getById(userId);

        return getLoginResult(user);
    }

    /**
     * 用户登出
     */
    public void logout() {
        if (StpUtil.isLogin()) {
            log.info("User logout successful, loginId: {}", StpUtil.getLoginId());
            StpUtil.logout();
        }
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = this.getUserIdByLoginId((String) loginId);
        if (userId == null) {
            return Collections.emptyList();
        }

        UserPermission userPermission = loginCacheService.getUserPermission(userId);
        return userPermission.getPermissionList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = this.getUserIdByLoginId((String) loginId);
        if (userId == null) {
            return Collections.emptyList();
        }

        UserPermission userPermission = loginCacheService.getUserPermission(userId);
        return userPermission.getRoleList();
    }

    private Long getUserIdByLoginId(String loginId) {
        if (loginId == null) {
            return null;
        }

        try {
            String userId = loginId.split("_")[1];
            return Long.valueOf(userId);
        } catch (Exception e) {
            log.error("loginId parse error , loginId : {}", loginId, e);
            return null;
        }
    }

    /**
     * token 生成
     */
    private static String generateToken(Long userId) {
        return UUID.randomUUID().toString().replace("-", "") + StringConst.COLON + userId;
    }

    public void clearLoginEmployeeCache(Long userId) {
        loginCacheService.clearUserPermission(userId);
        loginCacheService.clearUserLoginInfo(userId);
    }

}
