package com.myow.system.domain.enums;

import com.myow.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yss
 * @date: 2026-02-03 22:01
 * @description: 登录结果枚举
 */
@Getter
@AllArgsConstructor
public enum LoginResultEnum implements BaseEnum {

    LOGIN_SUCCESS(0, "登录成功"),
    LOGIN_FAIL(1, "登录失败"),
    LOGIN_OUT(2, "退出登录");

    private final Integer code;
    private final String desc;

}
