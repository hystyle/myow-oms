package com.myow.infrastructure.domain;

/**
 * @author: yss
 * @date: 2026-01-27 22:28
 * @description:
 */
public class TenantContext {

    private static final ThreadLocal<Long> TENANT = new ThreadLocal<>();

    public static void set(Long tenantId) {
        TENANT.set(tenantId);
    }

    public static Long get() {
        return TENANT.get();
    }

    public static void clear() {
        TENANT.remove();
    }

}
