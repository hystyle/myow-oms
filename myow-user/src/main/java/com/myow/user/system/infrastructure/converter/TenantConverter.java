package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.application.dto.CreateTenantReqDTO;
import com.myow.user.system.domain.entity.Tenant;
import com.myow.user.system.infrastructure.persistence.po.TenantDO;
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
