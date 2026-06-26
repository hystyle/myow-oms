package com.myow.user.system.application.converter;

import com.myow.user.system.application.dto.CreateTenantReqDTO;
import com.myow.user.system.application.dto.TenantRespDTO;
import com.myow.user.system.application.dto.UpdateTenantReqDTO;
import com.myow.user.system.domain.entity.Tenant;
import com.myow.user.system.infrastructure.persistence.po.TenantDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring"/*, imports = {EnumUtil.class, StatusEnum.class, String.class}*/)
public interface TenantApplicationConverter {

    /**
     * convert
     *
     * @param bean bean
     * @return tenant
     */
    Tenant convert(CreateTenantReqDTO bean);

    /**
     * convert
     *
     * @param bean bean
     * @return tenant
     */
    Tenant convert(UpdateTenantReqDTO bean);

    /**
     * convert
     *
     * @param tenant tenant
     * @return tenant resp dto
     */
    TenantRespDTO convert(Tenant tenant);

    /**
     * convert
     *
     * @param tenantDO tenantDO
     * @return tenant
     */
//    @Mapping(source = "status", target = "statusCode")
//    @Mapping(source = "status", target = "statusName", expression = "java(EnumUtil.getDesc(StatusEnum.class, String.valueOf(tenantDO.getStatus())))")
    TenantRespDTO convert(TenantDO tenantDO);
}
