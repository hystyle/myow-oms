package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.TenantPlansApplicationConverter;
import com.myow.system.application.dto.CreateTenantPlansReqDTO;
import com.myow.system.application.dto.PageTenantPlansReqDTO;
import com.myow.system.application.dto.TenantPlansRespDTO;
import com.myow.system.application.dto.UpdateTenantPlansReqDTO;
import com.myow.system.domain.entity.TenantPlans;
import com.myow.system.infrastructure.converter.TenantPlansConverter;
import com.myow.system.infrastructure.persistence.po.TenantPlansDO;
import com.myow.system.infrastructure.persistence.repository.TenantPlansRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class TenantPlansService {

    private final TenantPlansRepository tenantPlansRepository;
    private final TenantPlansApplicationConverter tenantPlansApplicationConverter;
    private final TenantPlansConverter tenantPlansConverter;

    public Long createTenantPlans(CreateTenantPlansReqDTO createReqDTO) {
        TenantPlans tenantPlans = tenantPlansApplicationConverter.convert(createReqDTO);
        tenantPlansRepository.save(tenantPlansConverter.toDo(tenantPlans));
        return tenantPlans.getPlansId();
    }

    public void updateTenantPlans(UpdateTenantPlansReqDTO updateReqDTO) {
        TenantPlans tenantPlans = tenantPlansApplicationConverter.convert(updateReqDTO);
        tenantPlansRepository.updateById(tenantPlansConverter.toDo(tenantPlans));
    }

    public void deleteTenantPlans(Long id) {
        tenantPlansRepository.removeById(id);
    }

    public TenantPlansRespDTO getTenantPlans(Long id) {
        TenantPlansDO tenantPlansDO = tenantPlansRepository.getById(id);
        return tenantPlansApplicationConverter.convert(tenantPlansConverter.toEntity(tenantPlansDO));
    }

    public PageResult<TenantPlansRespDTO> getTenantPlansPage(PageTenantPlansReqDTO pageTenantPlansReqDTO) {
        Page<TenantPlansDO> tenantPlansDOPage = tenantPlansRepository.selectPage(pageTenantPlansReqDTO);
        if (Objects.isNull(tenantPlansDOPage) || tenantPlansDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(tenantPlansDOPage, tenantPlansApplicationConverter::convert);
    }
}
