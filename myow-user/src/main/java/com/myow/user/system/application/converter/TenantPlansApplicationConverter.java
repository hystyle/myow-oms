package com.myow.user.system.application.converter;

import com.myow.user.system.application.dto.CreateTenantPlansReqDTO;
import com.myow.user.system.application.dto.TenantPlansRespDTO;
import com.myow.user.system.application.dto.UpdateTenantPlansReqDTO;
import com.myow.user.system.domain.entity.TenantPlans;
import com.myow.user.system.infrastructure.persistence.po.TenantPlansDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface TenantPlansApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return tenant plans
     */
    TenantPlans convert(CreateTenantPlansReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return tenant plans
     */
    TenantPlans convert(UpdateTenantPlansReqDTO bean);

    /**
     * convert
     * @param tenantPlans tenant plans
     * @return tenant plans resp dto
     */
    TenantPlansRespDTO convert(TenantPlans tenantPlans);

    /**
     * convert
     * @param tenantPlansDO tenant plans
     * @return tenant plans resp dto
     */
    TenantPlansRespDTO convert(TenantPlansDO tenantPlansDO);
}
