package com.myow.system.application.service;

import cn.hutool.core.collection.CollUtil;
import com.myow.system.application.converter.DeptApplicationConverter;
import com.myow.system.application.vo.DepartmentTreeVO;
import com.myow.system.domain.consts.SystemCacheConst;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import com.myow.system.infrastructure.persistence.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: yss
 * @date: 2026-01-27 22:56
 * @description: 部门 缓存服务
 */
@Service
@RequiredArgsConstructor
public class DeptCacheService {

    private final DeptRepository deptRepository;
    private final DeptApplicationConverter deptApplicationConverter;

    /**
     * 缓存某个部门的下级id列表
     */
    @Cacheable(SystemCacheConst.Department.DEPARTMENT_SELF_CHILDREN)
    public List<Long> getDeptSelfAndChildren(Long deptId) {
        List<DeptDO> doList = deptRepository.list();
        return this.selfAndChildrenIdList(deptId, doList);
    }

    /**
     * 通过部门id,获取当前以及下属部门
     */
    public List<Long> selfAndChildrenIdList(Long departmentId, List<DeptDO> doList) {
        List<Long> selfAndChildrenIdList = new ArrayList<>(doList.size());
        if (CollUtil.isEmpty(doList)) {
            return selfAndChildrenIdList;
        }
        selfAndChildrenIdList.add(departmentId);
        List<DepartmentTreeVO> children = this.getChildren(departmentId, doList);
        if (CollUtil.isEmpty(children)) {
            return selfAndChildrenIdList;
        }
        List<Long> childrenIdList = children.stream().map(DepartmentTreeVO::getDepartmentId).toList();
        selfAndChildrenIdList.addAll(childrenIdList);
        for (Long childId : childrenIdList) {
            this.selfAndChildrenRecursion(selfAndChildrenIdList, childId, doList);
        }
        return selfAndChildrenIdList;
    }


    /**
     * 获取子元素
     */
    private List<DepartmentTreeVO> getChildren(Long departmentId, List<DeptDO> voList) {
        List<DeptDO> childrenEntityList = voList.stream().filter(e -> departmentId.equals(e.getParentId())).collect(Collectors.toList());
        if (CollUtil.isEmpty(childrenEntityList)) {
            return Collections.emptyList();
        }
        return deptApplicationConverter.convert(childrenEntityList);
    }

    /**
     * 递归查询
     */
    public void selfAndChildrenRecursion(List<Long> selfAndChildrenIdList, Long departmentId, List<DeptDO> doList) {
        List<DepartmentTreeVO> children = this.getChildren(departmentId, doList);
        if (CollUtil.isEmpty(children)) {
            return;
        }
        List<Long> childrenIdList = children.stream().map(DepartmentTreeVO::getDepartmentId).toList();
        selfAndChildrenIdList.addAll(childrenIdList);
        for (Long childId : childrenIdList) {
            this.selfAndChildrenRecursion(selfAndChildrenIdList, childId, doList);
        }
    }
}
