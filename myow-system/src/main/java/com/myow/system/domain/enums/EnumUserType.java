package com.myow.system.domain.enums;

import com.myow.common.enums.BaseEnum;
import lombok.Getter;

/**
 * @author: yss
 * @date: 2026-01-26 20:17
 * @description: 用户类型枚举
 */
@Getter
public enum EnumUserType implements BaseEnum {

    SYS_USER("sys_user", "管理端用户"),
    CLIENT_USER("client_user", "客户端用户");


    private final String code;
    private final String desc;

    EnumUserType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
