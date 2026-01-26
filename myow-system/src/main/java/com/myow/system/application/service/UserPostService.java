package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.UserPostApplicationConverter;
import com.myow.system.application.dto.CreateUserPostReqDTO;
import com.myow.system.application.dto.PageUserPostReqDTO;
import com.myow.system.application.dto.UpdateUserPostReqDTO;
import com.myow.system.application.dto.UserPostRespDTO;
import com.myow.system.domain.entity.UserPost;
import com.myow.system.infrastructure.converter.UserPostConverter;
import com.myow.system.infrastructure.persistence.po.UserPostDO;
import com.myow.system.infrastructure.persistence.repository.UserPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class UserPostService {

    private final UserPostRepository userPostRepository;
    private final UserPostApplicationConverter userPostApplicationConverter;
    private final UserPostConverter userPostConverter;

    public boolean createUserPost(CreateUserPostReqDTO createReqDTO) {
        UserPost userPost = userPostApplicationConverter.convert(createReqDTO);
        return userPostRepository.save(userPostConverter.toDo(userPost));
    }

    public void updateUserPost(UpdateUserPostReqDTO updateReqDTO) {
        userPostRepository.removeByCompositeKey(updateReqDTO.getOriginalUserId(), updateReqDTO.getOriginalPostId());
        UserPost newUserPost = new UserPost();
        newUserPost.setUserId(updateReqDTO.getUserId());
        newUserPost.setPostId(updateReqDTO.getPostId());
        userPostRepository.save(userPostConverter.toDo(newUserPost));
    }

    public void deleteUserPost(Long userId, Long postId) {
        // Delete from DB using composite key
        userPostRepository.removeByCompositeKey(userId, postId);
    }

    public UserPostRespDTO getUserPost(Long userId, Long postId) {
        UserPostDO userPostDO = userPostRepository.getByCompositeKey(userId, postId);
        return userPostApplicationConverter.convert(userPostConverter.toEntity(userPostDO));
    }

    public PageResult<UserPostRespDTO> getUserPostPage(PageUserPostReqDTO pageUserPostReqDTO) {
        Page<UserPostDO> page = userPostRepository.selectPage(pageUserPostReqDTO);
        return MyPageUtil.of(page, userPostApplicationConverter::convert);
    }
}
