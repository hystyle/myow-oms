package com.myow.user.infrastructure.gateway;

import com.myow.common.port.TenantUserPort;
import com.myow.common.security.PasswordService;
import com.myow.user.infrastructure.persistence.po.TenantUserDO;
import com.myow.user.infrastructure.persistence.repository.TenantUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserTenantUserPort implements TenantUserPort {

    private final TenantUserRepository tenantUserRepository;

    @Override
    public String createTenantAdminUser(String tenantCode, String loginName, String nickName, String phone) {
        TenantUserDO user = new TenantUserDO();
        user.setTenantId(tenantCode);
        user.setLoginName(loginName);
        user.setNickName(nickName);
        user.setPhone(phone);

        String userCode = "0001";
        user.setUserCode(userCode);

        String rawPassword = PasswordService.randomPassword();
        String saltedPassword = PasswordService.generateSaltPassword(rawPassword, userCode);
        user.setPassword(PasswordService.getEncryptPwd(saltedPassword));

        tenantUserRepository.save(user);
        return saltedPassword;
    }

    @Override
    public Map<String, Long> countUsersInTenants(List<String> tenantCodes) {
        return tenantUserRepository.countInTenant(tenantCodes);
    }
}
