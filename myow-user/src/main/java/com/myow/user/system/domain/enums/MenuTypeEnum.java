package com.myow.user.system.domain.enums;

import com.myow.common.enums.BaseEnum;
import lombok.Getter;

/**
 * @author: yss
 * @date: 2026-01-28 22:06
 * @description: 菜单类型枚举
 */
@Getter
public enum MenuTypeEnum implements BaseEnum {
    CATALOG("M", "目录"),
    MENU("C", "菜单"),
    POINTS("F", "功能点");

    private final String code;
    private final String desc;

    MenuTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
