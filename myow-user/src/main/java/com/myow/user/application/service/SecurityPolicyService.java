package com.myow.user.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.common.response.ResultCode;
import com.myow.user.system.application.dto.ConfigRespDTO;
import com.myow.user.system.application.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SecurityPolicyService {

    private static final String PASSWORD_MIN_LENGTH = "security.password.min-length";
    private static final String PASSWORD_REQUIRE_LETTER = "security.password.require-letter";
    private static final String PASSWORD_REQUIRE_NUMBER = "security.password.require-number";
    private static final String PASSWORD_EXPIRE_DAYS = "security.password.expire-days";
    private static final String LOGIN_MAX_FAIL_COUNT = "security.login.max-fail-count";
    private static final String LOGIN_LOCK_MINUTES = "security.login.lock-minutes";
    private static final String LOGIN_CAPTCHA_ENABLED = "security.login.captcha-enabled";
    private static final String LOGIN_CAPTCHA_AFTER_FAIL_COUNT = "security.login.captcha-after-fail-count";

    private final ConfigService configService;

    public int getMaxFailCount(String tenantId) {
        return getIntConfig(tenantId, LOGIN_MAX_FAIL_COUNT, 5);
    }

    public int getLockMinutes(String tenantId) {
        return getIntConfig(tenantId, LOGIN_LOCK_MINUTES, 30);
    }

    public boolean isCaptchaRequired(String tenantId, Integer failedLoginCount) {
        if (getBooleanConfig(tenantId, LOGIN_CAPTCHA_ENABLED, false)) {
            return true;
        }
        int threshold = getIntConfig(tenantId, LOGIN_CAPTCHA_AFTER_FAIL_COUNT, 3);
        int currentFailedCount = failedLoginCount == null ? 0 : failedLoginCount;
        return threshold > 0 && currentFailedCount >= threshold;
    }

    public void validatePassword(String tenantId, String password) {
        int minLength = getIntConfig(tenantId, PASSWORD_MIN_LENGTH, 8);
        if (password == null || password.length() < minLength) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "password length is too short");
        }
        if (getBooleanConfig(tenantId, PASSWORD_REQUIRE_LETTER, true) && !containsLetter(password)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "password must contain letter");
        }
        if (getBooleanConfig(tenantId, PASSWORD_REQUIRE_NUMBER, true) && !containsNumber(password)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "password must contain number");
        }
    }

    public LocalDateTime calculatePasswordExpireTime(String tenantId) {
        int expireDays = getIntConfig(tenantId, PASSWORD_EXPIRE_DAYS, 90);
        if (expireDays <= 0) {
            return null;
        }
        return LocalDateTime.now().plusDays(expireDays);
    }

    private int getIntConfig(String tenantId, String configKey, int defaultValue) {
        ConfigRespDTO config = configService.getEffectiveConfig(parseTenantId(tenantId), configKey);
        if (config == null || config.getConfigValue() == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(config.getConfigValue());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private boolean getBooleanConfig(String tenantId, String configKey, boolean defaultValue) {
        ConfigRespDTO config = configService.getEffectiveConfig(parseTenantId(tenantId), configKey);
        if (config == null || config.getConfigValue() == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(config.getConfigValue());
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

    private boolean containsLetter(String password) {
        return password.chars().anyMatch(Character::isLetter);
    }

    private boolean containsNumber(String password) {
        return password.chars().anyMatch(Character::isDigit);
    }
}
