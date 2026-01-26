package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateDeptReqDTO;
import com.myow.system.application.dto.DeptRespDTO;
import com.myow.system.application.dto.UpdateDeptReqDTO;
import com.myow.system.domain.entity.Dept;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface DeptApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return dept
     */
    Dept convert(CreateDeptReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return dept
     */
    Dept convert(UpdateDeptReqDTO bean);

    /**
     * convert
     * @param dept dept
     * @return dept resp dto
     */
    DeptRespDTO convert(Dept dept);


    /**
     * convert
     * @param deptDO deptDO
     * @return dept resp dto
     */
    DeptRespDTO convert(DeptDO deptDO);

}
