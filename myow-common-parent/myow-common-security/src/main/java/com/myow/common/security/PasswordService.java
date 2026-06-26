package com.myow.common.security;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.myow.common.constant.StringConst;

import java.security.SecureRandom;

public final class PasswordService {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIALS = "#@";
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordService() {
    }

    public static String generateSaltPassword(String password, String userCode) {
        return password + StringConst.UNDERLINE
                + userCode.toUpperCase()
                + StringConst.UNDERLINE
                + userCode.toLowerCase();
    }

    public static String randomPassword() {
        return random(UPPER, 3) + random(DIGITS, 2) + random(LOWER, 2) + random(SPECIALS, 1);
    }

    public static String getEncryptPwd(String password) {
        return SaSecureUtil.sha256(password);
    }

    public static Boolean matchesPwd(String password, String encodedPassword) {
        return SaSecureUtil.sha256(password).equals(encodedPassword);
    }

    private static String random(String source, int length) {
        StringBuilder value = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            value.append(source.charAt(RANDOM.nextInt(source.length())));
        }
        return value.toString();
    }
}
