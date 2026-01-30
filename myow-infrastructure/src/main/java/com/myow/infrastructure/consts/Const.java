package com.myow.infrastructure.consts;

import java.util.Set;

/**
 * @author: yss
 * @date: 2026-01-29 22:31
 * @description: 静态常量
 */
public class Const {

    /**
     * 忽略多租户的表
     * <>
     * 租户表本身
     * 字典表
     * 配置表
     * 平台级公共表
     * </>
     */
    public static final Set<String> IGNORE_TENANT_TABLES = Set.of(
            "sys_tenant",
            "sys_tenant_plans",
            "sys_dict",
            "sys_dict_data"
    );
}
