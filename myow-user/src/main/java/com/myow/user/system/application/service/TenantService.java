package com.myow.user.system.application.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.port.TenantUserPort;
import com.myow.common.response.PageResult;
import com.myow.common.response.UserErrorCode;
import com.myow.common.mybatis.util.MyPageUtil;
import com.myow.user.system.application.converter.TenantApplicationConverter;
import com.myow.user.system.application.dto.CreateTenantReqDTO;
import com.myow.user.system.application.dto.PageTenantReqDTO;
import com.myow.user.system.application.dto.TenantRespDTO;
import com.myow.user.system.infrastructure.converter.TenantConverter;
import com.myow.user.system.infrastructure.persistence.po.TenantDO;
import com.myow.user.system.infrastructure.persistence.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final TenantApplicationConverter tenantApplicationConverter;
    private final TenantConverter tenantConverter;
    private final TenantUserPort tenantUserPort;

    @Transactional(rollbackFor = Exception.class)
    @InterceptorIgnore(tenantLine = "true")
    public String createTenant(CreateTenantReqDTO createReqDTO) {
        TenantDO tenantDO = tenantConverter.toDo(createReqDTO);
        tenantDO.setStatus(true);

        String password = tenantUserPort.createTenantAdminUser(
                tenantDO.getTenantCode(),
                createReqDTO.getContactPhone(),
                createReqDTO.getContactName(),
                createReqDTO.getContactPhone());
        tenantRepository.save(tenantDO);

        return password;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateTenantStatus(Long tenantId) {
        TenantDO existTenant = tenantRepository.getById(tenantId);
        if (Objects.isNull(existTenant)) {
            throw new BusinessException(UserErrorCode.TENANT_NOT_EXIST);
        }

        return tenantRepository.updateTenantStatus(existTenant.getTenantId(), !existTenant.getStatus());
    }

    public PageResult<TenantRespDTO> getTenantPage(PageTenantReqDTO pageTenantReqDTO) {
        Page<TenantDO> tenantDOPage = tenantRepository.selectPage(pageTenantReqDTO);
        if (CollUtil.isEmpty(tenantDOPage.getRecords())) {
            return PageResult.empty();
        }

        PageResult<TenantRespDTO> result = MyPageUtil.of(tenantDOPage, tenantApplicationConverter::convert);
        List<TenantRespDTO> tenantList = result.getList();

        // 统计租户下用户数量
        List<String> tenantIdList = tenantList.stream().map(TenantRespDTO::getTenantCode).toList();
        Map<String, Long> countedInTenant = tenantUserPort.countUsersInTenants(tenantIdList);

        tenantList.forEach(tenantRespDTO ->
        {
            tenantRespDTO.setUserCount(countedInTenant.getOrDefault(tenantRespDTO.getTenantCode(), 0L));
        });

        return result;
    }

}
