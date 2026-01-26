package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateRoleDeptReqDTO;
import com.myow.system.application.dto.RoleDeptRespDTO;
import com.myow.system.application.dto.UpdateRoleDeptReqDTO;
import com.myow.system.domain.entity.RoleDept;
import com.myow.system.infrastructure.persistence.po.RoleDeptDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface RoleDeptApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return role dept
     */
    RoleDept convert(CreateRoleDeptReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return role dept
     */
    @Mapping(source = "roleId", target = "roleId") // Map new roleId to roleId
    @Mapping(source = "deptId", target = "deptId") // Map new deptId to deptId
    RoleDept convert(UpdateRoleDeptReqDTO bean);

    /**
     * convert
     * @param roleDept role dept
     * @return role dept resp dto
     */
    RoleDeptRespDTO convert(RoleDept roleDept);

    /**
     * convert
     * @param roleDeptDO role dept do
     * @return role dept resp dto
     */
    RoleDeptRespDTO convert(RoleDeptDO roleDeptDO);
}
