package com.myow.system.infrastructure.converter;

import com.myow.system.domain.entity.UserPost;
import com.myow.system.infrastructure.persistence.po.UserPostDO;
import org.mapstruct.Mapper;

/**
 * @author yss
 */
@Mapper(componentModel = "spring")
public interface UserPostConverter {

    /**
     * to do
     * @param userPost user post
     * @return user post do
     */
    UserPostDO toDo(UserPost userPost);

    /**
     * to entity
     * @param userPostDO user post do
     * @return user post
     */
    UserPost toEntity(UserPostDO userPostDO);
}
