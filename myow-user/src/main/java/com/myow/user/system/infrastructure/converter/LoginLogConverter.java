package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.domain.entity.LoginLog;
import com.myow.user.system.infrastructure.persistence.po.LoginLogDO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginLogConverter {
    LoginLogDO toDo(LoginLog loginLog);
    LoginLog toEntity(LoginLogDO loginLogDO);
}
