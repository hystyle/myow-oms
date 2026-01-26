package com.myow.system.application.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.DeptApplicationConverter;
import com.myow.system.application.dto.CreateDeptReqDTO;
import com.myow.system.application.dto.DeptRespDTO;
import com.myow.system.application.dto.PageDeptReqDTO;
import com.myow.system.application.dto.UpdateDeptReqDTO;
import com.myow.system.domain.entity.Dept;
import com.myow.system.infrastructure.converter.DeptConverter;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import com.myow.system.infrastructure.persistence.po.UserDO;
import com.myow.system.infrastructure.persistence.repository.DeptRepository;
import com.myow.system.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;
    private final DeptApplicationConverter deptApplicationConverter;
    private final DeptConverter deptConverter;
    private final UserRepository userRepository;

    public Long createDept(CreateDeptReqDTO createReqDTO) {
        validateDeptForCreate(createReqDTO);
        Dept dept = deptApplicationConverter.convert(createReqDTO);
        deptRepository.save(deptConverter.toDo(dept));
        return dept.getDeptId();
    }

    public void updateDept(UpdateDeptReqDTO updateReqDTO) {
        validateDeptForUpdate(updateReqDTO);
        Dept dept = deptApplicationConverter.convert(updateReqDTO);
        deptRepository.updateById(deptConverter.toDo(dept));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(Long id) {
        DeptDO existDept = deptRepository.getById(id);
        if (Objects.isNull(existDept)) {
            throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST);
        }
        
        Long childCount = deptRepository.count(Wrappers.lambdaQuery(DeptDO.class)
            .eq(DeptDO::getParentId, id));
        if (childCount > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "该部门存在子部门，无法删除");
        }
        
        Long userCount = userRepository.count(Wrappers.lambdaQuery(UserDO.class)
            .eq(UserDO::getDeptId, id));
        if (userCount > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "该部门存在用户，无法删除");
        }
        
        deptRepository.removeById(id);
    }

    public DeptRespDTO getDept(Long id) {
        DeptDO deptDO = deptRepository.getById(id);
        if (Objects.isNull(deptDO)) {
            throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST);
        }
        return deptApplicationConverter.convert(deptConverter.toEntity(deptDO));
    }

    public PageResult<DeptRespDTO> getDeptPage(PageDeptReqDTO pageDeptReqDTO) {
        Page<DeptDO> deptDOPage = deptRepository.selectPage(pageDeptReqDTO);
        return MyPageUtil.of(deptDOPage, deptApplicationConverter::convert);
    }

    private void validateDeptForCreate(CreateDeptReqDTO createReqDTO) {
        if (StrUtil.isBlank(createReqDTO.getDeptName())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "部门名称不能为空");
        }
        
        if (Objects.nonNull(createReqDTO.getParentId())) {
            DeptDO parentDept = deptRepository.getById(createReqDTO.getParentId());
            if (Objects.isNull(parentDept)) {
                throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST, "父部门不存在");
            }
        }
        
        if (Objects.nonNull(createReqDTO.getManagerId())) {
            UserDO manager = userRepository.getById(createReqDTO.getManagerId());
            if (Objects.isNull(manager)) {
                throw new BusinessException(UserErrorCode.USER_NOT_EXIST, "负责人用户不存在");
            }
        }
        
        Long parentId = Objects.isNull(createReqDTO.getParentId()) ? 0L : createReqDTO.getParentId();
        Long countByName = deptRepository.count(Wrappers.lambdaQuery(DeptDO.class)
            .eq(DeptDO::getDeptName, createReqDTO.getDeptName())
            .eq(DeptDO::getParentId, parentId));
        if (countByName > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "同一父部门下部门名称已存在");
        }
    }

    private void validateDeptForUpdate(UpdateDeptReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getDeptId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "部门ID不能为空");
        }
        
        DeptDO existDept = deptRepository.getById(updateReqDTO.getDeptId());
        if (Objects.isNull(existDept)) {
            throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST);
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getDeptName())) {
            if (Objects.nonNull(updateReqDTO.getParentId())) {
                if (Objects.equals(updateReqDTO.getParentId(), updateReqDTO.getDeptId())) {
                    throw new BusinessException(ResultCode.PARAM_ERROR, "不能将部门的父部门设置为自己");
                }
                
                DeptDO parentDept = deptRepository.getById(updateReqDTO.getParentId());
                if (Objects.isNull(parentDept)) {
                    throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST, "父部门不存在");
                }
                
                if (isChildDept(updateReqDTO.getDeptId(), updateReqDTO.getParentId())) {
                    throw new BusinessException(ResultCode.PARAM_ERROR, "不能将部门的父部门设置为其子部门");
                }
            }
            
            Long parentId = Objects.isNull(updateReqDTO.getParentId()) ? 
                Objects.isNull(existDept.getParentId()) ? 0L : existDept.getParentId() : 
                updateReqDTO.getParentId();
            
            Long countByName = deptRepository.count(Wrappers.lambdaQuery(DeptDO.class)
                .eq(DeptDO::getDeptName, updateReqDTO.getDeptName())
                .eq(DeptDO::getParentId, parentId)
                .ne(DeptDO::getDeptId, updateReqDTO.getDeptId()));
            if (countByName > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "同一父部门下部门名称已存在");
            }
        }
        
        if (Objects.nonNull(updateReqDTO.getManagerId())) {
            UserDO manager = userRepository.getById(updateReqDTO.getManagerId());
            if (Objects.isNull(manager)) {
                throw new BusinessException(UserErrorCode.USER_NOT_EXIST, "负责人用户不存在");
            }
        }
    }

    private boolean isChildDept(Long deptId, Long parentId) {
        List<DeptDO> childDepts = deptRepository.list(Wrappers.lambdaQuery(DeptDO.class)
            .eq(DeptDO::getParentId, deptId));
        
        if (childDepts.isEmpty()) {
            return false;
        }
        
        for (DeptDO child : childDepts) {
            if (Objects.equals(child.getDeptId(), parentId)) {
                return true;
            }
            if (isChildDept(child.getDeptId(), parentId)) {
                return true;
            }
        }
        
        return false;
    }
}
