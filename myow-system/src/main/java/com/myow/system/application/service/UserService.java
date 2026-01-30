package com.myow.system.application.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.UserErrorCode;
import com.myow.common.support.serialnumber.SerialNumberService;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.UserApplicationConverter;
import com.myow.system.application.dto.*;
import com.myow.system.application.vo.RoleUserVO;
import com.myow.system.infrastructure.persistence.po.UserDO;
import com.myow.system.infrastructure.persistence.po.UserRoleDO;
import com.myow.system.infrastructure.persistence.repository.UserRepository;
import com.myow.system.infrastructure.persistence.repository.UserRoleRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserApplicationConverter userApplicationConverter;
    private final DeptService deptService;
    private final RoleService roleService;
    private final PositionService positionService;
    private final UserRoleRepository userRoleRepository;
    private final SerialNumberService serialNumberService;
    @Resource
    @Lazy
    private LoginService loginService;

    /**
     * 创建租户管理员账户
     */
    public String createTenantAdminUser(String tenantId, CreateUserDTO createUserDTO) {
        UserDO userDO = userApplicationConverter.toDo(createUserDTO);
        userDO.setTenantId(tenantId);
        String userCode = "0001";
        userDO.setUserCode(userCode);

        // 设置密码
        String generateSaltPassword = SecurityPasswordService.generateSaltPassword(SecurityPasswordService.randomPassword(), userCode);
        userDO.setPassword(SecurityPasswordService.getEncryptPwd(generateSaltPassword));
        // 保存
        userRepository.save(userDO);
        // 返回密码
        return generateSaltPassword;
    }

    /**
     * 创建用户
     */
    @Transactional(rollbackFor = Exception.class)
    public String createUser(CreateUserReqDTO createReqDTO) {
        validateUserForCreate(createReqDTO);

        UserDO userDO = userApplicationConverter.toDo(createReqDTO);

        // 生成用户编号
        String userCode = serialNumberService.generate("USER", LocalDate.now(), userDO);
        userDO.setUserCode(userCode);

        // 设置随机密码
        String generateSaltPassword = SecurityPasswordService.generateSaltPassword(SecurityPasswordService.randomPassword(), userCode);
        userDO.setPassword(SecurityPasswordService.getEncryptPwd(generateSaltPassword));

        // 保存
        userRepository.save(userDO);

        // 保存 角色列表
        if (CollUtil.isNotEmpty(createReqDTO.getRoleIdList())) {
            userRoleRepository.saveBatch(
                    createReqDTO.getRoleIdList().stream().map(roleId -> new UserRoleDO(roleId, userDO.getUserId())).toList());
        }

        // 返回密码
        return generateSaltPassword;
    }

    /**
     * 校验用户创建参数
     */
    private void validateUserForCreate(CreateUserDTO createUserDTO) {
        // 校验登录名是否重复
        UserDO existUser = userRepository.getByLoginName(createUserDTO.getLoginName());
        if (Objects.nonNull(existUser)) {
            throw new BusinessException(UserErrorCode.USER_LOGIN_NAME_EXIST);
        }
        // 校验电话是否存在
        UserDO existUserByPhone = userRepository.getByPhone(createUserDTO.getPhone());
        if (Objects.nonNull(existUserByPhone)) {
            throw new BusinessException(UserErrorCode.PHONE_ALREADY_EXIST);
        }
        // 校验邮箱是否存在
        UserDO existUserByEmail = userRepository.getByEmail(createUserDTO.getEmail());
        if (Objects.nonNull(existUserByEmail)) {
            throw new BusinessException(UserErrorCode.EMAIL_ALREADY_EXIST);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UpdateUserReqDTO updateReqDTO) {
        Long userId = updateReqDTO.getUserId();
        UserDO existUser = userRepository.getById(userId);
        if (Objects.isNull(existUser)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }

        UserDO user = userApplicationConverter.toDo(updateReqDTO);

        userRepository.updateById(user);

        // 保存 角色列表, 若为空，则删除所有角色
        if (CollUtil.isNotEmpty(updateReqDTO.getRoleIdList())) {
            userRoleRepository.saveBatch(
                    updateReqDTO.getRoleIdList().stream().map(roleId -> new UserRoleDO(roleId, user.getUserId())).toList());
        } else {
            userRoleRepository.deleteByUserId(userId);
        }

        // 清空缓存
        loginService.clearLoginEmployeeCache(userId);
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
        UserDO userDO = userRepository.getById(id);
        return userApplicationConverter.toDTO(userDO);
    }

    public PageResult<UserRespDTO> getUserPage(PageUserReqDTO pageUserReqDTO) {
        List<Long> departmentIdList = new ArrayList<>();
        if (pageUserReqDTO.getDeptId() != null) {
            departmentIdList.addAll(deptService.selfAndChildrenIdList(pageUserReqDTO.getDeptId()));
        }

        Page<UserDO> userDOPage = userRepository.selectPage(pageUserReqDTO, departmentIdList);
        if (CollUtil.isEmpty(userDOPage.getRecords())) {
            return PageResult.empty();
        }

        PageResult<UserRespDTO> result = MyPageUtil.of(userDOPage, userApplicationConverter::convert);
        List<UserRespDTO> userList = result.getList();

        // 查询员工角色
        List<Long> userIdList = userList.stream().map(UserRespDTO::getUserId).toList();
        List<RoleUserVO> roleUserVOList = roleService.getRoleByUserIdList(userIdList);
        Map<Long, List<Long>> userRoleIdListMap = roleUserVOList.stream().collect(Collectors.groupingBy(RoleUserVO::getUserId, Collectors.mapping(RoleUserVO::getRoleId, Collectors.toList())));
        Map<Long, List<String>> userRoleNameListMap = roleUserVOList.stream().collect(Collectors.groupingBy(RoleUserVO::getUserId, Collectors.mapping(RoleUserVO::getRoleName, Collectors.toList())));

        // 查询员工职位
        List<Long> positionIdList = userList.stream().map(UserRespDTO::getPositionId).toList();
        List<PositionRespDTO> positionLists = CollUtil.isEmpty(positionIdList) ? Collections.emptyList() : positionService.getPositionListByIds(positionIdList);
        Map<Long, String> positionNameMap = positionLists.stream().collect(Collectors.toMap(PositionRespDTO::getPositionId, PositionRespDTO::getPositionName));

        result.getList().forEach(userRespDTO -> {
            userRespDTO.setRoleIdList(userRoleIdListMap.getOrDefault(userRespDTO.getUserId(), Collections.emptyList()));
            userRespDTO.setRoleNameList(userRoleNameListMap.getOrDefault(userRespDTO.getUserId(), Collections.emptyList()));
            userRespDTO.setDeptName(deptService.getDept(userRespDTO.getDeptId()).getDeptName());
            userRespDTO.setPositionName(positionNameMap.get(userRespDTO.getPositionId()));
        });

        return result;
    }

}
