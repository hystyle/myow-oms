package com.myow.system.infrastructure.converter;

import com.myow.system.application.dto.CreateTenantReqDTO;
import com.myow.system.domain.entity.Tenant;
import com.myow.system.infrastructure.persistence.po.TenantDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface TenantConverter {

    /**
     * to do
     *
     * @param tenant tenant
     * @return tenant do
     */
    TenantDO toDo(Tenant tenant);

    /**
     * to do
     *
     * @param createReqDTO create req dto
     * @return tenant do
     */
    TenantDO toDo(CreateTenantReqDTO createReqDTO);

    /**
     * to entity
     *
     * @param tenantDO tenant do
     * @return tenant
     */
    Tenant toEntity(TenantDO tenantDO);
}
