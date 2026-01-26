package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.User;
import com.myow.system.infrastructure.persistence.po.UserDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface UserConverter {
    /**
     * to do
     * @param user user
     * @return user do
     */
    UserDO toDo(User user);

    /**
     * to entity
     * @param userDO user do
     * @return user
     */
    User toEntity(UserDO userDO);
}
