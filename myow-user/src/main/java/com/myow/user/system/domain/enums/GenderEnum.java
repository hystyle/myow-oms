package com.myow.user.system.domain.enums;

import com.myow.common.enums.BaseEnum;
import lombok.Getter;

/**
 * @author: yss
 * @date: 2026-01-28 22:41
 * @description: 性别枚举
 */
@Getter
public enum GenderEnum implements BaseEnum {
    /**
     * 0 未知
     */
    UNKNOWN(0, "未知"),

    /**
     * 男 1 奇数为阳
     */
    MAN(1, "男"),

    /**
     * 女 2 偶数为阴
     */
    WOMAN(2, "女");

    private final Integer code;
    private final String desc;

    GenderEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
