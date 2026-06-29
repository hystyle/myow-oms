package com.myow.user.system.application.converter;

import com.myow.user.system.application.dto.CreateLoginLogReqDTO;
import com.myow.user.system.application.dto.LoginLogRespDTO;
import com.myow.user.system.domain.entity.LoginLog;
import com.myow.user.system.infrastructure.persistence.po.LoginLogDO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginLogApplicationConverter {
    LoginLog convert(CreateLoginLogReqDTO bean);
    LoginLogRespDTO convert(LoginLog bean);
    LoginLogRespDTO convert(LoginLogDO bean);
}
