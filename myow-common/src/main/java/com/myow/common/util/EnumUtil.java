package com.myow.common.util;

import com.myow.common.enums.BaseEnum;

/**
 * @author: yss
 * @date: 2026-01-26 20:13
 * @description: 枚举工具类
 */
public final class EnumUtil {

    private EnumUtil() {}

    public static <T extends BaseEnum> T fromCode(Class<T> enumClass, String code) {
        return BaseEnum.fromCode(enumClass, code);
    }

    public static <T extends BaseEnum> T fromCodeThrow(Class<T> enumClass, String code) {
        T e = fromCode(enumClass, code);
        if (e == null) {
            throw new IllegalArgumentException("无效的枚举code: " + code + " for " + enumClass.getSimpleName());
        }
        return e;
    }

    public static <T extends BaseEnum> String getDesc(T e) {
        return e == null ? null : e.getDesc();
    }

    public static <T extends BaseEnum> String getCode(T e) {
        return e == null ? null : e.getCode();
    }
}
