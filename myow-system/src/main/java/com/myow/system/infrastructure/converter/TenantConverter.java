package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.Tenant;
import com.myow.system.infrastructure.persistence.po.TenantDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface TenantConverter {

    /**
     * to do
     * @param tenant tenant
     * @return tenant do
     */
    TenantDO toDo(Tenant tenant);

    /**
     * to entity
     * @param tenantDO tenant do
     * @return tenant
     */
    Tenant toEntity(TenantDO tenantDO);
}
