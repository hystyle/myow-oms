package com.myow.user.system.application.converter;

import com.myow.user.system.application.dto.CreateDeptReqDTO;
import com.myow.user.system.application.dto.DeptRespDTO;
import com.myow.user.system.application.dto.UpdateDeptReqDTO;
import com.myow.user.system.application.vo.DepartmentTreeVO;
import com.myow.user.system.application.vo.DepartmentVO;
import com.myow.user.system.domain.entity.Dept;
import com.myow.user.system.infrastructure.persistence.po.DeptDO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface DeptApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return deptDO
     */
    DeptDO convert(CreateDeptReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return DeptDO
     */
    DeptDO convert(UpdateDeptReqDTO bean);

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
    DepartmentVO convert(DeptDO deptDO);

    /**
     * convert
     * @param childrenEntityList childrenEntityList
     * @return dept resp dto
     */
    List<DepartmentTreeVO> convert(List<DeptDO> childrenEntityList);
}
