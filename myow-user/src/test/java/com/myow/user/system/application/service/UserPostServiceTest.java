package com.myow.user.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.user.system.BaseServiceTest;
import com.myow.user.system.application.dto.CreateUserPostReqDTO;
import com.myow.user.system.application.dto.UserPostRespDTO;
import com.myow.user.system.application.dto.UpdateUserPostReqDTO;
import com.myow.user.system.infrastructure.persistence.po.PositionDO;
import com.myow.user.system.infrastructure.persistence.po.UserDO;
import com.myow.user.system.infrastructure.persistence.po.UserPostDO;
import com.myow.user.system.infrastructure.persistence.repository.PositionRepository;
import com.myow.user.system.infrastructure.persistence.repository.UserPostRepository;
import com.myow.user.system.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("UserPostService测试")
class UserPostServiceTest extends BaseServiceTest {

    @Mock
    private UserPostRepository userPostRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private UserPostService userPostService;

    @Test
    @DisplayName("创建用户岗位关联-成功")
    void createUserPost_Success() {
        CreateUserPostReqDTO createReqDTO = new CreateUserPostReqDTO();
        createReqDTO.setUserId(1L);
        createReqDTO.setPositionId(2L);

        UserDO mockUserDO = new UserDO();
        mockUserDO.setUserId(1L);
        PositionDO mockPositionDO = new PositionDO();
        mockPositionDO.setPositionId(2L);
        UserPostDO mockUserPostDO = new UserPostDO();
        mockUserPostDO.setUserId(1L);
        mockUserPostDO.setPositionId(2L);

        when(userRepository.getById(1L)).thenReturn(mockUserDO);
        when(positionRepository.getById(2L)).thenReturn(mockPositionDO);
        when(userPostRepository.save(any(UserPostDO.class))).thenReturn(true);

        boolean result = userPostService.createUserPost(createReqDTO);

        assertThat(result).isTrue();
        verify(userPostRepository, times(1)).save(any(UserPostDO.class));
    }

    @Test
    @DisplayName("创建用户岗位关联-用户ID为空")
    void createUserPost_UserIdNull() {
        CreateUserPostReqDTO createReqDTO = new CreateUserPostReqDTO();
        createReqDTO.setUserId(null);
        createReqDTO.setPositionId(2L);

        assertThatThrownBy(() -> userPostService.createUserPost(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("用户ID不能为空");
    }

    @Test
    @DisplayName("创建用户岗位关联-岗位ID为空")
    void createUserPost_PostIdNull() {
        CreateUserPostReqDTO createReqDTO = new CreateUserPostReqDTO();
        createReqDTO.setUserId(1L);
        createReqDTO.setPositionId(null);

        assertThatThrownBy(() -> userPostService.createUserPost(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("岗位ID不能为空");
    }

    @Test
    @DisplayName("创建用户岗位关联-用户不存在")
    void createUserPost_UserNotExist() {
        CreateUserPostReqDTO createReqDTO = new CreateUserPostReqDTO();
        createReqDTO.setUserId(999L);
        createReqDTO.setPositionId(2L);

        when(userRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> userPostService.createUserPost(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.USER_NOT_EXIST);
    }

    @Test
    @DisplayName("创建用户岗位关联-岗位不存在")
    void createUserPost_PostNotExist() {
        CreateUserPostReqDTO createReqDTO = new CreateUserPostReqDTO();
        createReqDTO.setUserId(1L);
        createReqDTO.setPositionId(999L);

        UserDO mockUserDO = new UserDO();
        mockUserDO.setUserId(1L);
        when(userRepository.getById(1L)).thenReturn(mockUserDO);
        when(positionRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> userPostService.createUserPost(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.POSITION_NOT_EXIST);
    }

    @Test
    @DisplayName("创建用户岗位关联-关联已存在")
    void createUserPost_AlreadyExist() {
        CreateUserPostReqDTO createReqDTO = new CreateUserPostReqDTO();
        createReqDTO.setUserId(1L);
        createReqDTO.setPositionId(2L);

        UserDO mockUserDO = new UserDO();
        mockUserDO.setUserId(1L);
        PositionDO mockPositionDO = new PositionDO();
        mockPositionDO.setPositionId(2L);
        UserPostDO mockUserPostDO = new UserPostDO();
        mockUserPostDO.setUserId(1L);
        mockUserPostDO.setPositionId(2L);

        when(userRepository.getById(1L)).thenReturn(mockUserDO);
        when(positionRepository.getById(2L)).thenReturn(mockPositionDO);
        when(userPostRepository.getByCompositeKey(1L, 2L)).thenReturn(mockUserPostDO);

        assertThatThrownBy(() -> userPostService.createUserPost(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该用户岗位关联已存在");
    }

    @Test
    @DisplayName("更新用户岗位关联-成功")
    void updateUserPost_Success() {
        UpdateUserPostReqDTO updateReqDTO = new UpdateUserPostReqDTO();
        updateReqDTO.setUserId(1L);
        updateReqDTO.setPositionId(2L);
        updateReqDTO.setOriginalUserId(3L);
        updateReqDTO.setOriginalPositionId(4L);

        UserDO mockUserDO = new UserDO();
        mockUserDO.setUserId(1L);
        PositionDO mockPositionDO = new PositionDO();
        mockPositionDO.setPositionId(2L);

        when(userRepository.getById(1L)).thenReturn(mockUserDO);
        when(positionRepository.getById(2L)).thenReturn(mockPositionDO);
        when(userPostRepository.getByCompositeKey(1L, 2L)).thenReturn(null);

        userPostService.updateUserPost(updateReqDTO);

        verify(userPostRepository, times(1)).removeByCompositeKey(3L, 4L);
        verify(userPostRepository, times(1)).save(any(UserPostDO.class));
    }

    @Test
    @DisplayName("删除用户岗位关联-成功")
    void deleteUserPost_Success() {
        UserPostDO mockUserPostDO = new UserPostDO();
        mockUserPostDO.setUserId(1L);
        mockUserPostDO.setPositionId(2L);

        when(userPostRepository.getByCompositeKey(1L, 2L)).thenReturn(mockUserPostDO);
        when(userPostRepository.removeByCompositeKey(1L, 2L)).thenReturn(true);

        userPostService.deleteUserPost(1L, 2L);

        verify(userPostRepository, times(1)).removeByCompositeKey(1L, 2L);
    }

    @Test
    @DisplayName("删除用户岗位关联-关联不存在")
    void deleteUserPost_NotExist() {
        when(userPostRepository.getByCompositeKey(1L, 2L)).thenReturn(null);

        assertThatThrownBy(() -> userPostService.deleteUserPost(1L, 2L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("用户岗位关联不存在");
    }

    @Test
    @DisplayName("获取用户岗位关联-成功")
    void getUserPost_Success() {
        UserPostDO mockUserPostDO = new UserPostDO();
        mockUserPostDO.setUserId(1L);
        mockUserPostDO.setPositionId(2L);

        when(userPostRepository.getByCompositeKey(1L, 2L)).thenReturn(mockUserPostDO);

        UserPostRespDTO userPostRespDTO = userPostService.getUserPost(1L, 2L);

        assertThat(userPostRespDTO).isNotNull();
        assertThat(userPostRespDTO.getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取用户岗位关联-关联不存在")
    void getUserPost_NotExist() {
        when(userPostRepository.getByCompositeKey(1L, 2L)).thenReturn(null);

        assertThatThrownBy(() -> userPostService.getUserPost(1L, 2L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("用户岗位关联不存在");
    }
}
