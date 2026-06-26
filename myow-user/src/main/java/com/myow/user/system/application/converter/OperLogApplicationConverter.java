package com.myow.user.system.application.converter;

import com.myow.user.system.application.dto.CreateOperLogReqDTO;
import com.myow.user.system.application.dto.OperLogRespDTO;
import com.myow.user.system.application.dto.UpdateOperLogReqDTO;
import com.myow.user.system.domain.entity.OperLog;
import com.myow.user.system.infrastructure.persistence.po.OperLogDO;
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
