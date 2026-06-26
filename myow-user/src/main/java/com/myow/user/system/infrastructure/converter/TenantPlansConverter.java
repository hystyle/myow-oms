package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.domain.entity.TenantPlans;
import com.myow.user.system.infrastructure.persistence.po.TenantPlansDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface TenantPlansConverter {

    /**
     * to do
     * @param tenantPlans tenant plans
     * @return tenant plans do
     */
    TenantPlansDO toDo(TenantPlans tenantPlans);

    /**
     * to entity
     * @param tenantPlansDO tenant plans do
     * @return tenant plans
     */
    TenantPlans toEntity(TenantPlansDO tenantPlansDO);
}
