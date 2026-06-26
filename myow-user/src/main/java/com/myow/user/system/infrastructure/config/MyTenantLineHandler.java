package com.myow.user.system.infrastructure.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.myow.common.constant.TenantConst;
import com.myow.common.context.TenantContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.stereotype.Service;

/**
 * @author: yss
 * @date: 2026-01-30 22:38
 * @description: 多租户处理
 */
@Service("tenantLineHandler")
public class MyTenantLineHandler implements TenantLineHandler {

    @Override
    public Expression getTenantId() {
        Long tenantId = TenantContext.get();
        if (tenantId == null) {
            throw new IllegalStateException("TenantId is null");
        }
        return new LongValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return "tenant_id";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return TenantConst.IGNORE_TENANT_TABLES.contains(tableName);
    }
}
