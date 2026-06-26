package com.myow.user.system.application.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.user.system.application.converter.DeptApplicationConverter;
import com.myow.user.system.application.dto.CreateDeptReqDTO;
import com.myow.user.system.application.dto.UpdateDeptReqDTO;
import com.myow.user.system.application.vo.DepartmentTreeVO;
import com.myow.user.system.application.vo.DepartmentVO;
import com.myow.user.system.infrastructure.persistence.po.DeptDO;
import com.myow.user.system.infrastructure.persistence.po.UserDO;
import com.myow.user.system.infrastructure.persistence.repository.DeptRepository;
import com.myow.user.system.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;
    private final UserRepository userRepository;
    private final DeptCacheService deptCacheService;
    private final DeptApplicationConverter deptApplicationConverter;

    public Long createDept(CreateDeptReqDTO createReqDTO) {
        validateDeptForCreate(createReqDTO);
        DeptDO dept = deptApplicationConverter.convert(createReqDTO);
        deptRepository.save(dept);

        // 清除自身以及下级的id列表缓存
        deptCacheService.clearCache();
        return dept.getDeptId();
    }

    public void updateDept(UpdateDeptReqDTO updateReqDTO) {
        validateDeptForUpdate(updateReqDTO);
        DeptDO dept = deptApplicationConverter.convert(updateReqDTO);
        deptRepository.updateById(dept);

        // 清除自身以及下级的id列表缓存
        deptCacheService.clearCache();
    }

    /**
     * 根据id删除部门
     * <p>
     * 1、需要判断当前部门是否有子部门,有子部门则不允许删除
     * 2、需要判断当前部门是否有员工，有员工则不能删除
     */
    public void deleteDept(Long id) {
        DeptDO existDept = deptRepository.getById(id);
        if (Objects.isNull(existDept)) {
            throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST);
        }

        Long childCount = deptRepository.count(Wrappers.lambdaQuery(DeptDO.class)
                .eq(DeptDO::getParentId, id));
        if (childCount > 0) {
            throw new BusinessException(UserErrorCode.DEPT_EXIST_CHILDREN);
        }

        Long userCount = userRepository.count(Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getDeptId, id));
        if (userCount > 0) {
            throw new BusinessException(UserErrorCode.DEPT_EXIST_USER);
        }

        deptCacheService.clearCache();

        deptRepository.removeById(id);
    }

    private void validateDeptForCreate(CreateDeptReqDTO createReqDTO) {
        Long parentId = Objects.isNull(createReqDTO.getParentId()) ? 0L : createReqDTO.getParentId();
        Long countByName = deptRepository.count(Wrappers.lambdaQuery(DeptDO.class)
                .eq(DeptDO::getDeptName, createReqDTO.getDeptName())
                .eq(DeptDO::getParentId, parentId));
        if (countByName > 0) {
            throw new BusinessException(UserErrorCode.DEPT_NAME_EXIST);
        }
    }

    private void validateDeptForUpdate(UpdateDeptReqDTO updateReqDTO) {
        DeptDO existDept = deptRepository.getById(updateReqDTO.getDeptId());
        if (Objects.isNull(existDept)) {
            throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST);
        }

        if (StrUtil.isNotBlank(updateReqDTO.getDeptName())) {
            Long parentId = Objects.isNull(updateReqDTO.getParentId()) ?
                    Objects.isNull(existDept.getParentId()) ? 0L : existDept.getParentId() :
                    updateReqDTO.getParentId();

            Long countByName = deptRepository.count(Wrappers.lambdaQuery(DeptDO.class)
                    .eq(DeptDO::getDeptName, updateReqDTO.getDeptName())
                    .eq(DeptDO::getParentId, parentId)
                    .ne(DeptDO::getDeptId, updateReqDTO.getDeptId()));
            if (countByName > 0) {
                throw new BusinessException(UserErrorCode.DEPT_NAME_EXIST);
            }
        }

    }

    // ---------------------------- 查询 ----------------------------

    /**
     * 获取部门树形结构
     */
    public List<DepartmentTreeVO> getDeptTree() {
        return deptCacheService.getDepartmentTree();
    }

    /**
     * 自身以及所有下级的部门id列表
     */
    public List<Long> getDeptSelfAndChildren(Long departmentId) {
        return deptCacheService.getDeptSelfAndChildren(departmentId);
    }

    /**
     * 获取所有部门
     */
    public List<DepartmentVO> listAll() {
        return deptCacheService.getDepartmentList();
    }

    /**
     * 获取部门
     */
    public DepartmentVO getDept(Long deptId) {
        DeptDO deptDO = deptRepository.getById(deptId);
        return deptApplicationConverter.convert(deptDO);
    }

    /**
     * 获取部门路径：/公司/研发部/产品组
     */
    public String getDepartmentPath(Long departmentId) {
        return deptCacheService.getDepartmentPathMap().get(departmentId);
    }

    /**
     * 自身以及所有下级的部门id列表
     */
    public List<Long> selfAndChildrenIdList(Long departmentId) {
        return deptCacheService.getDeptSelfAndChildren(departmentId);
    }

}
