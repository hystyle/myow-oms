package com.myow.system.domain.enums;

import com.myow.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yss
 * @date: 2026-02-03 21:57
 * @description: 系统内置用户枚举
 */
@Getter
@AllArgsConstructor
public enum SystemUserEnum implements BaseEnum {

    SYSTEM_USER("system", "系统用户"),
    ;

    private final String code;
    private final String desc;

}
