package com.myow.system.application.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.UserPostApplicationConverter;
import com.myow.system.application.dto.CreateUserPostReqDTO;
import com.myow.system.application.dto.PageUserPostReqDTO;
import com.myow.system.application.dto.UpdateUserPostReqDTO;
import com.myow.system.application.dto.UserPostRespDTO;
import com.myow.system.domain.entity.UserPost;
import com.myow.system.infrastructure.converter.UserPostConverter;
import com.myow.system.infrastructure.persistence.po.PositionDO;
import com.myow.system.infrastructure.persistence.po.UserDO;
import com.myow.system.infrastructure.persistence.po.UserPostDO;
import com.myow.system.infrastructure.persistence.repository.PositionRepository;
import com.myow.system.infrastructure.persistence.repository.UserPostRepository;
import com.myow.system.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserPostService {

    private final UserPostRepository userPostRepository;
    private final UserPostApplicationConverter userPostApplicationConverter;
    private final UserPostConverter userPostConverter;
    private final UserRepository userRepository;
    private final PositionRepository positionRepository;

    public boolean createUserPost(CreateUserPostReqDTO createReqDTO) {
        validateUserPostForCreate(createReqDTO);
        UserPost userPost = userPostApplicationConverter.convert(createReqDTO);
        return userPostRepository.save(userPostConverter.toDo(userPost));
    }

    public void updateUserPost(UpdateUserPostReqDTO updateReqDTO) {
        validateUserPostForUpdate(updateReqDTO);
        userPostRepository.removeByCompositeKey(updateReqDTO.getOriginalUserId(), updateReqDTO.getOriginalPostId());
        UserPost newUserPost = new UserPost();
        newUserPost.setUserId(updateReqDTO.getUserId());
        newUserPost.setPostId(updateReqDTO.getPostId());
        userPostRepository.save(userPostConverter.toDo(newUserPost));
    }

    public void deleteUserPost(Long userId, Long postId) {
        UserPostDO existUserPost = userPostRepository.getByCompositeKey(userId, postId);
        if (Objects.isNull(existUserPost)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户岗位关联不存在");
        }
        userPostRepository.removeByCompositeKey(userId, postId);
    }

    public UserPostRespDTO getUserPost(Long userId, Long postId) {
        UserPostDO userPostDO = userPostRepository.getByCompositeKey(userId, postId);
        if (Objects.isNull(userPostDO)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户岗位关联不存在");
        }
        return userPostApplicationConverter.convert(userPostConverter.toEntity(userPostDO));
    }

    public PageResult<UserPostRespDTO> getUserPostPage(PageUserPostReqDTO pageUserPostReqDTO) {
        Page<UserPostDO> page = userPostRepository.selectPage(pageUserPostReqDTO);
        return MyPageUtil.of(page, userPostApplicationConverter::convert);
    }

    private void validateUserPostForCreate(CreateUserPostReqDTO createReqDTO) {
        if (Objects.isNull(createReqDTO.getUserId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }
        
        if (Objects.isNull(createReqDTO.getPostId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "岗位ID不能为空");
        }
        
        UserDO user = userRepository.getById(createReqDTO.getUserId());
        if (Objects.isNull(user)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }
        
        PositionDO position = positionRepository.getById(createReqDTO.getPostId());
        if (Objects.isNull(position)) {
            throw new BusinessException(UserErrorCode.POSITION_NOT_EXIST);
        }
        
        UserPostDO existUserPost = userPostRepository.getByCompositeKey(createReqDTO.getUserId(), createReqDTO.getPostId());
        if (Objects.nonNull(existUserPost)) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "该用户岗位关联已存在");
        }
    }

    private void validateUserPostForUpdate(UpdateUserPostReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getUserId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getPostId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "岗位ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getOriginalUserId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "原用户ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getOriginalPostId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "原岗位ID不能为空");
        }
        
        UserDO user = userRepository.getById(updateReqDTO.getUserId());
        if (Objects.isNull(user)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }
        
        PositionDO position = positionRepository.getById(updateReqDTO.getPostId());
        if (Objects.isNull(position)) {
            throw new BusinessException(UserErrorCode.POSITION_NOT_EXIST);
        }
        
        UserPostDO existUserPost = userPostRepository.getByCompositeKey(updateReqDTO.getUserId(), updateReqDTO.getPostId());
        if (Objects.nonNull(existUserPost)) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "该用户岗位关联已存在");
        }
    }
}
