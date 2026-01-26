package com.myow.system.application.converter;

import com.myow.system.application.dto.CreateUserPostReqDTO;
import com.myow.system.application.dto.UserPostRespDTO;
import com.myow.system.application.dto.UpdateUserPostReqDTO;
import com.myow.system.domain.entity.UserPost;
import com.myow.system.infrastructure.persistence.po.UserPostDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author gemini
 */
@Mapper(componentModel = "spring")
public interface UserPostApplicationConverter {

    /**
     * convert
     * @param bean bean
     * @return user post
     */
    UserPost convert(CreateUserPostReqDTO bean);

    /**
     * convert
     * @param bean bean
     * @return user post
     */
    @Mapping(source = "userId", target = "userId") // Map new userId to userId
    @Mapping(source = "postId", target = "postId") // Map new postId to postId
    UserPost convert(UpdateUserPostReqDTO bean);

    /**
     * convert
     * @param userPost user post
     * @return user post resp dto
     */
    UserPostRespDTO convert(UserPost userPost);

    /**
     * convert
     * @param userPostDO user post
     * @return user post resp dto
     */
    UserPostRespDTO convert(UserPostDO userPostDO);
}
