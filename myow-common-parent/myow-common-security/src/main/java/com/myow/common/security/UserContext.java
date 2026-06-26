package com.myow.common.security;

import cn.dev33.satoken.stp.StpUtil;

/**
 * Central access point for the current login user.
 */
public final class UserContext {

    private UserContext() {
    }

    public static Long getUserId() {
        String loginId = getLoginId();
        if (loginId == null || loginId.isBlank()) {
            return null;
        }

        int separatorIndex = loginId.indexOf(':');
        String rawUserId = separatorIndex >= 0 ? loginId.substring(separatorIndex + 1) : loginId;
        try {
            return Long.valueOf(rawUserId);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static String getLoginId() {
        try {
            if (!StpUtil.isLogin()) {
                return null;
            }
            return StpUtil.getLoginIdAsString();
        } catch (Exception ignored) {
            return null;
        }
    }
}
