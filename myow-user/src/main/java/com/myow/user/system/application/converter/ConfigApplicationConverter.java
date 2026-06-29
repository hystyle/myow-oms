package com.myow.user.system.application.converter;

import com.myow.user.system.application.dto.ConfigRespDTO;
import com.myow.user.system.application.dto.CreateConfigReqDTO;
import com.myow.user.system.application.dto.UpdateConfigReqDTO;
import com.myow.user.system.domain.entity.Config;
import com.myow.user.system.infrastructure.persistence.po.ConfigDO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfigApplicationConverter {
    Config convert(CreateConfigReqDTO bean);
    Config convert(UpdateConfigReqDTO bean);
    ConfigRespDTO convert(Config bean);
    ConfigRespDTO convert(ConfigDO bean);
}
