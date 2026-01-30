package com.myow.system.application.service;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.myow.common.constant.StringConst;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: yss
 * @date: 2026-01-28 22:52
 * @description: 密码处理服务
 */
@Service
public class SecurityPasswordService {

    /**
     * 生成加盐密码
     * 格式为：[password]_[uid大写]_[uid小写]
     */
    public static String generateSaltPassword(String password, String employeeUid) {
        return password + StringConst.UNDERLINE +
                employeeUid.toUpperCase() +
                StringConst.UNDERLINE +
                employeeUid.toLowerCase();
    }

    /**
     * 随机生成密码 3位大写字母，2位数字，2位小写字母 + 1位特殊符号
     */
    public static String randomPassword() {
        return RandomStringUtils.randomAlphabetic(3).toUpperCase()
                + RandomStringUtils.randomNumeric(2)
                + RandomStringUtils.randomAlphabetic(2).toLowerCase()
                + (ThreadLocalRandom.current().nextBoolean() ? "#" : "@");
    }

    /**
     * 获取 加密后 的密码
     */
    public static String getEncryptPwd(String password) {
        return SaSecureUtil.sha256(password);
    }

    /**
     * 校验密码是否匹配
     */
    public static Boolean matchesPwd(String password, String encodedPassword) {
        return SaSecureUtil.sha256(password).equals(encodedPassword);
    }


}
