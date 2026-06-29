package com.myow.user.application.service;

import cn.dev33.satoken.stp.StpUtil;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.common.security.UserContext;
import com.myow.user.application.vo.UserSessionRespVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserSessionService {

    public List<UserSessionRespVO> listCurrentUserSessions() {
        String currentLoginId = getRequiredCurrentLoginId();
        String currentToken = StpUtil.getTokenValue();
        return StpUtil.getTokenValueListByLoginId(currentLoginId).stream()
                .map(token -> toRespVO(token, currentToken))
                .toList();
    }

    public void kickoutCurrentUserSession(String token) {
        String currentLoginId = getRequiredCurrentLoginId();
        Object tokenLoginId = StpUtil.getLoginIdByToken(token);
        if (!Objects.equals(currentLoginId, tokenLoginId == null ? null : String.valueOf(tokenLoginId))) {
            throw new BusinessException(UserErrorCode.NO_PERMISSION);
        }
        StpUtil.kickoutByTokenValue(token);
    }

    private String getRequiredCurrentLoginId() {
        String loginId = UserContext.getLoginId();
        if (loginId == null || loginId.isBlank()) {
            throw new BusinessException(UserErrorCode.LOGIN_STATE_INVALID);
        }
        return loginId;
    }

    private UserSessionRespVO toRespVO(String token, String currentToken) {
        UserSessionRespVO result = new UserSessionRespVO();
        result.setToken(token);
        result.setCurrent(Objects.equals(token, currentToken));
        result.setTokenTimeout(StpUtil.getTokenTimeout(token));
        return result;
    }
}
