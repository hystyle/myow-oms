package com.myow.system.application.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.UserApplicationConverter;
import com.myow.system.application.dto.CreateUserReqDTO;
import com.myow.system.application.dto.PageUserReqDTO;
import com.myow.system.application.dto.UpdateUserReqDTO;
import com.myow.system.application.dto.UserRespDTO;
import com.myow.system.domain.entity.Dept;
import com.myow.system.domain.entity.User;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import com.myow.system.infrastructure.persistence.po.UserDO;
import com.myow.system.infrastructure.persistence.repository.DeptRepository;
import com.myow.system.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private final UserRepository userRepository;
    private final UserApplicationConverter userApplicationConverter;
    private final DeptRepository deptRepository;

    public Long createUser(CreateUserReqDTO createReqDTO) {
        validateUserForCreate(createReqDTO);
        
        User user = userApplicationConverter.convert(createReqDTO);
        
        userRepository.save(userApplicationConverter.toDo(user));
        return user.getUserId();
    }

    public void updateUser(UpdateUserReqDTO updateReqDTO) {
        User user = userApplicationConverter.convert(updateReqDTO);
        validateUserForUpdate(updateReqDTO);
        userRepository.updateById(userApplicationConverter.toDo(user));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        UserDO existUser = userRepository.getById(id);
        if (Objects.isNull(existUser)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }
        
        userRepository.removeById(id);
    }

    public UserRespDTO getUser(Long id) {
        return userApplicationConverter.toDTO(userRepository.getById(id));
    }

    public PageResult<UserRespDTO> getUserPage(PageUserReqDTO pageUserReqDTO) {
        Page<UserDO> userDOPage = userRepository.selectPage(pageUserReqDTO);
        return MyPageUtil.of(userDOPage, userApplicationConverter::convert);
    }

    private void validateUserForCreate(CreateUserReqDTO createReqDTO) {
        if (StrUtil.isBlank(createReqDTO.getUserName())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户名不能为空");
        }
        
        if (StrUtil.isBlank(createReqDTO.getPassword())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "密码不能为空");
        }
        
        if (createReqDTO.getPassword().length() < 6) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "密码长度不能少于6位");
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getEmail()) && !ReUtil.isMatch(EMAIL_REGEX, createReqDTO.getEmail())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "邮箱格式不正确");
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getPhone()) && !PhoneUtil.isMobile(createReqDTO.getPhone())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "手机号格式不正确");
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getGender()) && 
            !Objects.equals(createReqDTO.getGender(), "0") && 
            !Objects.equals(createReqDTO.getGender(), "1") && 
            !Objects.equals(createReqDTO.getGender(), "2")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "性别参数不正确");
        }
        
        Long countByUserName = userRepository.count(Wrappers.lambdaQuery(UserDO.class)
            .eq(UserDO::getUserName, createReqDTO.getUserName()));
        if (countByUserName > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "用户名已存在");
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getEmail())) {
            Long countByEmail = userRepository.count(Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getEmail, createReqDTO.getEmail()));
            if (countByEmail > 0) {
                throw new BusinessException(UserErrorCode.EMAIL_ALREADY_EXIST);
            }
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getPhone())) {
            Long countByPhone = userRepository.count(Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getPhone, createReqDTO.getPhone()));
            if (countByPhone > 0) {
                throw new BusinessException(UserErrorCode.PHONE_ALREADY_EXIST);
            }
        }
        
        if (Objects.nonNull(createReqDTO.getDeptId())) {
            DeptDO deptDO = deptRepository.getById(createReqDTO.getDeptId());
            if (Objects.isNull(deptDO)) {
                throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST);
            }
        }
    }

    private void validateUserForUpdate(UpdateUserReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getUserId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }
        
        UserDO existUser = userRepository.getById(updateReqDTO.getUserId());
        if (Objects.isNull(existUser)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getEmail()) && !ReUtil.isMatch(EMAIL_REGEX, updateReqDTO.getEmail())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "邮箱格式不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getPhone()) && !PhoneUtil.isMobile(updateReqDTO.getPhone())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "手机号格式不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getGender()) && 
            !Objects.equals(updateReqDTO.getGender(), "0") && 
            !Objects.equals(updateReqDTO.getGender(), "1") && 
            !Objects.equals(updateReqDTO.getGender(), "2")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "性别参数不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getStatus()) && 
            !Objects.equals(updateReqDTO.getStatus(), "0") && 
            !Objects.equals(updateReqDTO.getStatus(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getUserName()) && 
            !Objects.equals(existUser.getUserName(), updateReqDTO.getUserName())) {
            Long countByUserName = userRepository.count(Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserName, updateReqDTO.getUserName())
                .ne(UserDO::getUserId, updateReqDTO.getUserId()));
            if (countByUserName > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "用户名已存在");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getEmail()) && 
            !Objects.equals(existUser.getEmail(), updateReqDTO.getEmail())) {
            Long countByEmail = userRepository.count(Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getEmail, updateReqDTO.getEmail())
                .ne(UserDO::getUserId, updateReqDTO.getUserId()));
            if (countByEmail > 0) {
                throw new BusinessException(UserErrorCode.EMAIL_ALREADY_EXIST);
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getPhone()) && 
            !Objects.equals(existUser.getPhone(), updateReqDTO.getPhone())) {
            Long countByPhone = userRepository.count(Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getPhone, updateReqDTO.getPhone())
                .ne(UserDO::getUserId, updateReqDTO.getUserId()));
            if (countByPhone > 0) {
                throw new BusinessException(UserErrorCode.PHONE_ALREADY_EXIST);
            }
        }
        
        if (Objects.nonNull(updateReqDTO.getDeptId())) {
            DeptDO deptDO = deptRepository.getById(updateReqDTO.getDeptId());
            if (Objects.isNull(deptDO)) {
                throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST);
            }
        }
    }
}
