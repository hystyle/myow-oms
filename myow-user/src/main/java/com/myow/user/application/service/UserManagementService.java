package com.myow.user.application.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.port.DeptInfoPort;
import com.myow.common.port.PositionInfoPort;
import com.myow.common.port.UserLoginCachePort;
import com.myow.common.response.PageResult;
import com.myow.common.response.UserErrorCode;
import com.myow.common.security.PasswordService;
import com.myow.common.support.serialnumber.SerialNumberService;
import com.myow.common.mybatis.util.MyPageUtil;
import com.myow.user.application.dto.CreateUserReqDTO;
import com.myow.user.application.dto.PageUserReqDTO;
import com.myow.user.application.dto.UpdateUserReqDTO;
import com.myow.user.application.vo.UserRespVO;
import com.myow.user.application.vo.UserRoleInfoVO;
import com.myow.user.infrastructure.persistence.po.TenantUserDO;
import com.myow.user.infrastructure.persistence.po.UserRoleDO;
import com.myow.user.infrastructure.persistence.repository.TenantUserRepository;
import com.myow.user.infrastructure.persistence.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final TenantUserRepository tenantUserRepository;
    private final UserRoleRepository userRoleRepository;
    private final SerialNumberService serialNumberService;
    private final UserLoginCachePort userLoginCachePort;
    private final DeptInfoPort deptInfoPort;
    private final PositionInfoPort positionInfoPort;

    @Transactional(rollbackFor = Exception.class)
    public String createUser(CreateUserReqDTO createReqDTO) {
        validateUserForCreate(createReqDTO);

        TenantUserDO user = toCreateDO(createReqDTO);
        String userCode = serialNumberService.generate("USER", LocalDate.now(), user);
        user.setUserCode(userCode);

        String password = PasswordService.generateSaltPassword(PasswordService.randomPassword(), userCode);
        user.setPassword(PasswordService.getEncryptPwd(password));

        tenantUserRepository.save(user);
        saveUserRoles(user.getUserId(), createReqDTO.getRoleIdList());

        return password;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UpdateUserReqDTO updateReqDTO) {
        Long userId = updateReqDTO.getUserId();
        TenantUserDO existUser = tenantUserRepository.getById(userId);
        if (Objects.isNull(existUser)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }

        tenantUserRepository.updateById(toUpdateDO(updateReqDTO));
        userRoleRepository.deleteByUserId(userId);
        saveUserRoles(userId, updateReqDTO.getRoleIdList());
        userLoginCachePort.clearUserLoginCache(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        TenantUserDO existUser = tenantUserRepository.getById(id);
        if (Objects.isNull(existUser)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }

        tenantUserRepository.removeById(id);
        userRoleRepository.deleteByUserId(id);
        userLoginCachePort.clearUserLoginCache(id);
    }

    public UserRespVO getUser(Long id) {
        TenantUserDO user = tenantUserRepository.getById(id);
        if (Objects.isNull(user)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }

        UserRespVO result = toRespVO(user);
        fillRoles(List.of(result));
        return result;
    }

    public PageResult<UserRespVO> getUserPage(PageUserReqDTO pageUserReqDTO) {
        List<Long> departmentIds = pageUserReqDTO.getDeptId() == null
                ? Collections.emptyList()
                : deptInfoPort.listSelfAndChildrenIds(pageUserReqDTO.getDeptId());
        Page<TenantUserDO> userPage = tenantUserRepository.selectPage(pageUserReqDTO, departmentIds);
        if (CollUtil.isEmpty(userPage.getRecords())) {
            return PageResult.empty();
        }

        PageResult<UserRespVO> result = MyPageUtil.of(userPage, this::toRespVO);
        fillRoles(result.getList());
        return result;
    }

    private void validateUserForCreate(CreateUserReqDTO createReqDTO) {
        if (Objects.nonNull(tenantUserRepository.getByLoginName(createReqDTO.getLoginName(), null))) {
            throw new BusinessException(UserErrorCode.USER_LOGIN_NAME_EXIST);
        }
        if (Objects.nonNull(tenantUserRepository.getByPhone(createReqDTO.getPhone()))) {
            throw new BusinessException(UserErrorCode.PHONE_ALREADY_EXIST);
        }
        if (Objects.nonNull(tenantUserRepository.getByEmail(createReqDTO.getEmail()))) {
            throw new BusinessException(UserErrorCode.EMAIL_ALREADY_EXIST);
        }
    }

    private void saveUserRoles(Long userId, List<Long> roleIdList) {
        if (CollUtil.isEmpty(roleIdList)) {
            return;
        }

        userRoleRepository.saveBatch(roleIdList.stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(roleId -> new UserRoleDO(userId, roleId))
                .toList());
    }

    private void fillRoles(List<UserRespVO> users) {
        List<Long> userIds = users.stream().map(UserRespVO::getUserId).toList();
        List<UserRoleInfoVO> roleInfoList = userRoleRepository.listRoleInfoByUserIds(userIds);
        Map<Long, List<Long>> roleIdMap = roleInfoList.stream()
                .collect(Collectors.groupingBy(UserRoleInfoVO::getUserId, Collectors.mapping(UserRoleInfoVO::getRoleId, Collectors.toList())));
        Map<Long, List<String>> roleNameMap = roleInfoList.stream()
                .collect(Collectors.groupingBy(UserRoleInfoVO::getUserId, Collectors.mapping(UserRoleInfoVO::getRoleName, Collectors.toList())));
        Map<Long, String> deptNameMap = deptInfoPort.getDeptNameMap(users.stream()
                .map(UserRespVO::getDeptId)
                .filter(Objects::nonNull)
                .distinct()
                .toList());
        Map<Long, String> positionNameMap = positionInfoPort.getPositionNameMap(users.stream()
                .map(UserRespVO::getPositionId)
                .filter(Objects::nonNull)
                .distinct()
                .toList());

        users.forEach(user -> {
            user.setRoleIdList(roleIdMap.getOrDefault(user.getUserId(), Collections.emptyList()));
            user.setRoleNameList(roleNameMap.getOrDefault(user.getUserId(), Collections.emptyList()));
            user.setDeptName(deptNameMap.get(user.getDeptId()));
            user.setPositionName(positionNameMap.get(user.getPositionId()));
        });
    }

    private TenantUserDO toCreateDO(CreateUserReqDTO createReqDTO) {
        TenantUserDO user = new TenantUserDO();
        user.setLoginName(createReqDTO.getLoginName());
        user.setNickName(createReqDTO.getNickName());
        user.setGender(toText(createReqDTO.getGender()));
        user.setEmail(createReqDTO.getEmail());
        user.setPhone(createReqDTO.getPhone());
        user.setRemark(createReqDTO.getRemark());
        user.setDeptId(createReqDTO.getDeptId());
        user.setPositionId(createReqDTO.getPositionId());
        return user;
    }

    private TenantUserDO toUpdateDO(UpdateUserReqDTO updateReqDTO) {
        TenantUserDO user = new TenantUserDO();
        user.setUserId(updateReqDTO.getUserId());
        user.setNickName(updateReqDTO.getNickName());
        user.setAvatar(toLong(updateReqDTO.getAvatar()));
        user.setDeptId(updateReqDTO.getDeptId());
        user.setGender(toText(updateReqDTO.getGender()));
        user.setEmail(updateReqDTO.getEmail());
        user.setPhone(updateReqDTO.getPhone());
        user.setPositionId(updateReqDTO.getPositionId());
        user.setRemark(updateReqDTO.getRemark());
        return user;
    }

    private UserRespVO toRespVO(TenantUserDO user) {
        UserRespVO result = new UserRespVO();
        result.setUserId(user.getUserId());
        result.setTenantId(user.getTenantId());
        result.setDeptId(user.getDeptId());
        result.setPositionId(user.getPositionId());
        result.setUserName(user.getLoginName());
        result.setNickName(user.getNickName());
        result.setUserType(user.getUserType());
        result.setEmail(user.getEmail());
        result.setPhone(user.getPhone());
        result.setGender(user.getGender());
        result.setAvatar(user.getAvatar());
        result.setStatus(toText(user.getStatus()));
        result.setCreateTime(user.getCreateTime());
        result.setRemark(user.getRemark());
        result.setAdminFlag(user.getAdminFlag());
        return result;
    }

    private String toText(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private Long toLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Long.valueOf(value);
    }
}
