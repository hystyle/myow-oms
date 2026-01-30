package com.myow.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.system.BaseServiceTest;
import com.myow.system.application.dto.CreateUserReqDTO;
import com.myow.system.infrastructure.persistence.repository.DeptRepository;
import com.myow.system.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserService测试")
class UserServiceTest extends BaseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeptRepository deptRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("创建用户-成功")
    void createUser_Success() {
    }

    @Test
    @DisplayName("创建用户-用户名为空")
    void createUser_UserNameBlank() {
        CreateUserReqDTO createReqDTO = new CreateUserReqDTO();

        assertThatThrownBy(() -> userService.createUser(createReqDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("参数错误");
    }

    @Test
    @DisplayName("创建用户-密码为空")
    void createUser_PasswordBlank() {
        CreateUserReqDTO createReqDTO = new CreateUserReqDTO();

        assertThatThrownBy(() -> userService.createUser(createReqDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("参数错误");
    }

}