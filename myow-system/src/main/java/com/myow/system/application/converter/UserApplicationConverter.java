package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateUserReqDTO;
import com.myow.system.application.dto.UserRespDTO;
import com.myow.system.application.dto.UpdateUserReqDTO;
import com.myow.system.domain.entity.User;
import com.myow.system.infrastructure.persistence.po.UserDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface UserApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return user
     */
    User convert(CreateUserReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return user
     */
    User convert(UpdateUserReqDTO bean);
    
    /**
     * convert
     * @param userDO user do
     * @return UserRespDTO
     */
    UserRespDTO convert(UserDO userDO);

    /**
     * convert
     * @param user user
     * @return user do
     */
    UserDO toDo(User user);

    /**
     * convert
     * @param userDO user do
     * @return UserRespDTO
     */
    UserRespDTO toDTO(UserDO userDO);
}
