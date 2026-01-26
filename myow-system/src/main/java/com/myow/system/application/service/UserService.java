package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.UserApplicationConverter;
import com.myow.system.application.dto.CreateUserReqDTO;
import com.myow.system.application.dto.PageUserReqDTO;
import com.myow.system.application.dto.UpdateUserReqDTO;
import com.myow.system.application.dto.UserRespDTO;
import com.myow.system.domain.entity.User;
import com.myow.system.infrastructure.persistence.po.UserDO;
import com.myow.system.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserApplicationConverter userApplicationConverter;


    public Long createUser(CreateUserReqDTO createReqDTO) {
        User user = userApplicationConverter.convert(createReqDTO);
        userRepository.save(userApplicationConverter.toDo(user));
        return user.getUserId();
    }


    public void updateUser(UpdateUserReqDTO updateReqDTO) {
        User user = userApplicationConverter.convert(updateReqDTO);
        userRepository.updateById(userApplicationConverter.toDo(user));
    }


    public void deleteUser(Long id) {
        userRepository.removeById(id);
    }


    public UserRespDTO getUser(Long id) {
        return userApplicationConverter.toDTO(userRepository.getById(id));
    }


    public PageResult<UserRespDTO> getUserPage(PageUserReqDTO pageUserReqDTO) {
        Page<UserDO> userDOPage = userRepository.selectPage(pageUserReqDTO);
        return MyPageUtil.of(userDOPage, userApplicationConverter::convert);
    }
}
