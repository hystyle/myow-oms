package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateOperLogReqDTO;
import com.myow.system.application.dto.OperLogRespDTO;
import com.myow.system.application.dto.UpdateOperLogReqDTO;
import com.myow.system.domain.entity.OperLog;
import com.myow.system.infrastructure.persistence.po.OperLogDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface OperLogApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return oper log
     */
    OperLog convert(CreateOperLogReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return oper log
     */
    OperLog convert(UpdateOperLogReqDTO bean);

    /**
     * convert
     * @param operLog oper log
     * @return oper log resp dto
     */
    OperLogRespDTO convert(OperLog operLog);

    /**
     * convert
     * @param operLogDO oper log
     * @return OperLogRespDTO
     */
    OperLogRespDTO convert(OperLogDO operLogDO);
}
