package com.myow.common.port;

import java.util.List;
import java.util.Map;

public interface TenantUserPort {

    String createTenantAdminUser(String tenantCode, String loginName, String nickName, String phone);

    Map<String, Long> countUsersInTenants(List<String> tenantCodes);
}
