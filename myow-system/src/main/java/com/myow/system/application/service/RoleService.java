package com.myow.system.application.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.RoleApplicationConverter;
import com.myow.system.application.dto.CreateRoleReqDTO;
import com.myow.system.application.dto.PageRoleReqDTO;
import com.myow.system.application.dto.RoleRespDTO;
import com.myow.system.application.dto.UpdateRoleReqDTO;
import com.myow.system.domain.entity.Role;
import com.myow.system.infrastructure.converter.RoleConverter;
import com.myow.system.infrastructure.persistence.po.RoleDO;
import com.myow.system.infrastructure.persistence.repository.RoleRepository;
import com.myow.system.infrastructure.persistence.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleApplicationConverter roleApplicationConverter;
    private final RoleConverter roleConverter;
    private final UserRoleRepository userRoleRepository;

    public Long createRole(CreateRoleReqDTO createReqDTO) {
        validateRoleForCreate(createReqDTO);
        Role role = roleApplicationConverter.convert(createReqDTO);
        roleRepository.save(roleConverter.toDo(role));
        return role.getRoleId();
    }

    public void updateRole(UpdateRoleReqDTO updateReqDTO) {
        validateRoleForUpdate(updateReqDTO);
        Role role = roleApplicationConverter.convert(updateReqDTO);
        roleRepository.updateById(roleConverter.toDo(role));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        RoleDO existRole = roleRepository.getById(id);
        if (Objects.isNull(existRole)) {
            throw new BusinessException(UserErrorCode.ROLE_NOT_EXIST);
        }
        
        Long userCount = userRoleRepository.count(Wrappers.lambdaQuery(com.myow.system.infrastructure.persistence.po.UserRoleDO.class)
            .eq(com.myow.system.infrastructure.persistence.po.UserRoleDO::getRoleId, id));
        if (userCount > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "该角色已被用户使用，无法删除");
        }
        
        roleRepository.removeById(id);
    }

    public RoleRespDTO getRole(Long id) {
        RoleDO roleDO = roleRepository.getById(id);
        if (Objects.isNull(roleDO)) {
            throw new BusinessException(UserErrorCode.ROLE_NOT_EXIST);
        }
        return roleApplicationConverter.convert(roleConverter.toEntity(roleDO));
    }

    public PageResult<RoleRespDTO> getRolePage(PageRoleReqDTO pageRoleReqDTO) {
        Page<RoleDO> roleDOPage = roleRepository.selectPage(pageRoleReqDTO);
        if (Objects.isNull(roleDOPage) || roleDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(roleDOPage, roleApplicationConverter::convert);
    }

    private void validateRoleForCreate(CreateRoleReqDTO createReqDTO) {
        if (StrUtil.isBlank(createReqDTO.getRoleName())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色名称不能为空");
        }
        
        if (StrUtil.isBlank(createReqDTO.getRoleCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色编码不能为空");
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getStatus()) && 
            !Objects.equals(createReqDTO.getStatus(), "0") && 
            !Objects.equals(createReqDTO.getStatus(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getDataScope())) {
            List<String> validDataScopes = List.of("1", "2", "3", "4", "5", "6");
            if (!validDataScopes.contains(createReqDTO.getDataScope())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "数据范围参数不正确");
            }
        }
        
        Long countByRoleName = roleRepository.count(Wrappers.lambdaQuery(RoleDO.class)
            .eq(RoleDO::getRoleName, createReqDTO.getRoleName()));
        if (countByRoleName > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "角色名称已存在");
        }
        
        Long countByRoleCode = roleRepository.count(Wrappers.lambdaQuery(RoleDO.class)
            .eq(RoleDO::getRoleCode, createReqDTO.getRoleCode()));
        if (countByRoleCode > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "角色编码已存在");
        }
    }

    private void validateRoleForUpdate(UpdateRoleReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getRoleId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }
        
        RoleDO existRole = roleRepository.getById(updateReqDTO.getRoleId());
        if (Objects.isNull(existRole)) {
            throw new BusinessException(UserErrorCode.ROLE_NOT_EXIST);
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getStatus()) && 
            !Objects.equals(updateReqDTO.getStatus(), "0") && 
            !Objects.equals(updateReqDTO.getStatus(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getDataScope())) {
            List<String> validDataScopes = List.of("1", "2", "3", "4", "5", "6");
            if (!validDataScopes.contains(updateReqDTO.getDataScope())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "数据范围参数不正确");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getRoleName()) && 
            !Objects.equals(existRole.getRoleName(), updateReqDTO.getRoleName())) {
            Long countByRoleName = roleRepository.count(Wrappers.lambdaQuery(RoleDO.class)
                .eq(RoleDO::getRoleName, updateReqDTO.getRoleName())
                .ne(RoleDO::getRoleId, updateReqDTO.getRoleId()));
            if (countByRoleName > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "角色名称已存在");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getRoleCode()) && 
            !Objects.equals(existRole.getRoleCode(), updateReqDTO.getRoleCode())) {
            Long countByRoleCode = roleRepository.count(Wrappers.lambdaQuery(RoleDO.class)
                .eq(RoleDO::getRoleCode, updateReqDTO.getRoleCode())
                .ne(RoleDO::getRoleId, updateReqDTO.getRoleId()));
            if (countByRoleCode > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "角色编码已存在");
            }
        }
    }
}
