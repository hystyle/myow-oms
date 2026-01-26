package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateTenantReqDTO;
import com.myow.system.application.dto.TenantRespDTO;
import com.myow.system.application.dto.UpdateTenantReqDTO;
import com.myow.system.domain.entity.Tenant;
import com.myow.system.infrastructure.persistence.po.TenantDO;
import org.mapstruct.Mapper;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface TenantApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return tenant
     */
    Tenant convert(CreateTenantReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return tenant
     */
    Tenant convert(UpdateTenantReqDTO bean);

    /**
     * convert
     * @param tenant tenant
     * @return tenant resp dto
     */
    TenantRespDTO convert(Tenant tenant);

    /**
     * convert
     * @param tenantDO tenantDO
     * @return tenant
     */
    TenantRespDTO convert(TenantDO tenantDO);
}
