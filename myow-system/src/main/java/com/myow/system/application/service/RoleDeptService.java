package com.myow.system.application.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.RoleDeptApplicationConverter;
import com.myow.system.application.dto.CreateRoleDeptReqDTO;
import com.myow.system.application.dto.PageRoleDeptReqDTO;
import com.myow.system.application.dto.RoleDeptRespDTO;
import com.myow.system.application.dto.UpdateRoleDeptReqDTO;
import com.myow.system.domain.entity.RoleDept;
import com.myow.system.infrastructure.converter.RoleDeptConverter;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import com.myow.system.infrastructure.persistence.po.RoleDO;
import com.myow.system.infrastructure.persistence.po.RoleDeptDO;
import com.myow.system.infrastructure.persistence.repository.DeptRepository;
import com.myow.system.infrastructure.persistence.repository.RoleDeptRepository;
import com.myow.system.infrastructure.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleDeptService {

    private final RoleDeptRepository roleDeptRepository;
    private final RoleDeptApplicationConverter roleDeptApplicationConverter;
    private final RoleDeptConverter roleDeptConverter;
    private final RoleRepository roleRepository;
    private final DeptRepository deptRepository;

    public boolean createRoleDept(CreateRoleDeptReqDTO createReqDTO) {
        validateRoleDeptForCreate(createReqDTO);
        RoleDept roleDept = roleDeptApplicationConverter.convert(createReqDTO);
        return roleDeptRepository.save(roleDeptConverter.toDo(roleDept));
    }

    public void updateRoleDept(UpdateRoleDeptReqDTO updateReqDTO) {
        validateRoleDeptForUpdate(updateReqDTO);
        roleDeptRepository.removeByCompositeKey(updateReqDTO.getOriginalRoleId(), updateReqDTO.getOriginalDeptId());
        RoleDept newRoleDept = new RoleDept();
        newRoleDept.setRoleId(updateReqDTO.getRoleId());
        newRoleDept.setDeptId(updateReqDTO.getDeptId());
        roleDeptRepository.save(roleDeptConverter.toDo(newRoleDept));
    }

    public void deleteRoleDept(Long roleId, Long deptId) {
        RoleDeptDO existRoleDept = roleDeptRepository.getByCompositeKey(roleId, deptId);
        if (Objects.isNull(existRoleDept)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色部门关联不存在");
        }
        roleDeptRepository.removeByCompositeKey(roleId, deptId);
    }

    public RoleDeptRespDTO getRoleDept(Long roleId, Long deptId) {
        RoleDeptDO roleDeptDO = roleDeptRepository.getByCompositeKey(roleId, deptId);
        if (Objects.isNull(roleDeptDO)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色部门关联不存在");
        }
        return roleDeptApplicationConverter.convert(roleDeptConverter.toEntity(roleDeptDO));
    }

    public PageResult<RoleDeptRespDTO> getRoleDeptPage(PageRoleDeptReqDTO pageRoleDeptReqDTO) {
        Page<RoleDeptDO> roleDeptDOPage = roleDeptRepository.selectPage(pageRoleDeptReqDTO);
        if (Objects.isNull(roleDeptDOPage) || roleDeptDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(roleDeptDOPage, roleDeptApplicationConverter::convert);
    }

    private void validateRoleDeptForCreate(CreateRoleDeptReqDTO createReqDTO) {
        if (Objects.isNull(createReqDTO.getRoleId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }
        
        if (Objects.isNull(createReqDTO.getDeptId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "部门ID不能为空");
        }
        
        RoleDO role = roleRepository.getById(createReqDTO.getRoleId());
        if (Objects.isNull(role)) {
            throw new BusinessException(UserErrorCode.ROLE_NOT_EXIST);
        }
        
        DeptDO dept = deptRepository.getById(createReqDTO.getDeptId());
        if (Objects.isNull(dept)) {
            throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST);
        }
        
        RoleDeptDO existRoleDept = roleDeptRepository.getByCompositeKey(createReqDTO.getRoleId(), createReqDTO.getDeptId());
        if (Objects.nonNull(existRoleDept)) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "该角色部门关联已存在");
        }
    }

    private void validateRoleDeptForUpdate(UpdateRoleDeptReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getRoleId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getDeptId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "部门ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getOriginalRoleId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "原角色ID不能为空");
        }
        
        if (Objects.isNull(updateReqDTO.getOriginalDeptId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "原部门ID不能为空");
        }
        
        RoleDO role = roleRepository.getById(updateReqDTO.getRoleId());
        if (Objects.isNull(role)) {
            throw new BusinessException(UserErrorCode.ROLE_NOT_EXIST);
        }
        
        DeptDO dept = deptRepository.getById(updateReqDTO.getDeptId());
        if (Objects.isNull(dept)) {
            throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST);
        }
        
        RoleDeptDO existRoleDept = roleDeptRepository.getByCompositeKey(updateReqDTO.getRoleId(), updateReqDTO.getDeptId());
        if (Objects.nonNull(existRoleDept)) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "该角色部门关联已存在");
        }
    }
}
