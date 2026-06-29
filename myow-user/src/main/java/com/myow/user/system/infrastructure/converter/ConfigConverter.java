package com.myow.user.system.infrastructure.converter;

import com.myow.user.system.domain.entity.Config;
import com.myow.user.system.infrastructure.persistence.po.ConfigDO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfigConverter {
    ConfigDO toDo(Config config);
    Config toEntity(ConfigDO configDO);
}
