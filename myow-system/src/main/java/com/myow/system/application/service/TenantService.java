package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.TenantApplicationConverter;
import com.myow.system.application.dto.CreateTenantReqDTO;
import com.myow.system.application.dto.PageTenantReqDTO;
import com.myow.system.application.dto.TenantRespDTO;
import com.myow.system.application.dto.UpdateTenantReqDTO;
import com.myow.system.domain.entity.Tenant;
import com.myow.system.infrastructure.converter.TenantConverter;
import com.myow.system.infrastructure.persistence.po.TenantDO;
import com.myow.system.infrastructure.persistence.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final TenantApplicationConverter tenantApplicationConverter;
    private final TenantConverter tenantConverter;

    public Long createTenant(CreateTenantReqDTO createReqDTO) {
        Tenant tenant = tenantApplicationConverter.convert(createReqDTO);
        tenantRepository.save(tenantConverter.toDo(tenant));
        return tenant.getTenantId();
    }

    public void updateTenant(UpdateTenantReqDTO updateReqDTO) {
        Tenant tenant = tenantApplicationConverter.convert(updateReqDTO);
        tenantRepository.updateById(tenantConverter.toDo(tenant));
    }

    public void deleteTenant(Long id) {
        tenantRepository.removeById(id);
    }

    public TenantRespDTO getTenant(Long id) {
        TenantDO tenantDO = tenantRepository.getById(id);
        return tenantApplicationConverter.convert(tenantConverter.toEntity(tenantDO));
    }

    public PageResult<TenantRespDTO> getTenantPage(PageTenantReqDTO pageTenantReqDTO) {
        Page<TenantDO> tenantDOPage = tenantRepository.selectPage(pageTenantReqDTO);
        return MyPageUtil.of(tenantDOPage, tenantApplicationConverter::convert);
    }
}
