package com.myow.system.application.service;

import cn.hutool.core.collection.CollUtil;
import com.myow.system.application.vo.DepartmentTreeVO;
import com.myow.system.application.vo.DepartmentVO;
import com.myow.system.domain.consts.SystemCacheConst;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import com.myow.system.infrastructure.persistence.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: yss
 * @date: 2026-01-27 22:56
 * @description: 部门 缓存服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeptCacheService {

    private final DeptRepository deptRepository;

    /**
     * 缓存部门树结构
     */
    @Cacheable(SystemCacheConst.Department.DEPARTMENT_TREE)
    public List<DepartmentTreeVO> getDepartmentTree() {
        List<DeptDO> deptList = deptRepository.list();
        return this.buildTree(deptList);
    }

    /**
     * 部门列表
     */
    @Cacheable(SystemCacheConst.Department.DEPARTMENT_LIST)
    public List<DepartmentVO> getDepartmentList() {
        return deptRepository.list().stream()
                .map(this::toDepartmentVO)
                .toList();
    }

    /**
     * 清除自身以及下级的id列表缓存
     */
    @CacheEvict(value = {
            SystemCacheConst.Department.DEPARTMENT_LIST,
            SystemCacheConst.Department.DEPARTMENT_SELF_CHILDREN,
            SystemCacheConst.Department.DEPARTMENT_TREE,
            SystemCacheConst.Department.DEPARTMENT_PATH,
    }, allEntries = true)
    public void clearCache() {
        log.info("clear {}", SystemCacheConst.Department.DEPARTMENT_LIST);
    }

    /**
     * 部门的路径名称
     */
    @Cacheable(SystemCacheConst.Department.DEPARTMENT_PATH)
    public Map<Long, String> getDepartmentPathMap() {
        List<DeptDO> deptList = deptRepository.list();
        Map<Long, DeptDO> departmentMap = deptList.stream()
                .collect(Collectors.toMap(DeptDO::getDeptId, Function.identity(), (oldVal, newVal) -> oldVal));

        Map<Long, String> pathNameMap = new HashMap<>();
        for (DeptDO dept : deptList) {
            String pathName = this.buildDepartmentPath(dept, departmentMap);
            pathNameMap.put(dept.getDeptId(), pathName);
        }

        return pathNameMap;
    }

    /**
     * 获取并缓存某个部门的下级id列表
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
        if (departmentId == null || CollUtil.isEmpty(doList)) {
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

    // ---------------------- 构造树的一些方法 ------------------------------

    /**
     * 构建父级部门路径
     */
    private String buildDepartmentPath(DeptDO dept, Map<Long, DeptDO> departmentMap) {
        if (dept.getParentId() == null || Objects.equals(dept.getParentId(), NumberUtils.LONG_ZERO)) {
            return dept.getDeptName();
        }
        // 父节点
        DeptDO parentDepartment = departmentMap.get(dept.getParentId());
        if (parentDepartment == null) {
            return dept.getDeptName();
        }
        String pathName = buildDepartmentPath(parentDepartment, departmentMap);
        return pathName + "/" + dept.getDeptName();
    }

    /**
     * 构建部门树结构
     */
    public List<DepartmentTreeVO> buildTree(List<DeptDO> doList) {
        if (CollUtil.isEmpty(doList)) {
            return Collections.emptyList();
        }
        List<DepartmentTreeVO> rootList = doList.stream()
                .filter(e -> e.getParentId() == null || Objects.equals(e.getParentId(), NumberUtils.LONG_ZERO))
                .map(this::toTreeVO)
                .toList();
        if (CollUtil.isEmpty(rootList)) {
            return Collections.emptyList();
        }
        this.recursiveBuildTree(rootList, doList);
        return rootList;
    }

    /**
     * 构建所有根节点的下级树形结构
     * 返回值为层序遍历结果
     * [由于departmentDao中listAll给出数据根据Sort降序 所以同一层中Sort值较大的优先遍历]
     */
    private List<Long> recursiveBuildTree(List<DepartmentTreeVO> nodeList, List<DeptDO> allDepartmentList) {
        int nodeSize = nodeList.size();
        List<Long> childIdList = new ArrayList<>();
        for (int i = 0; i < nodeSize; i++) {
            int preIndex = i - 1;
            int nextIndex = i + 1;
            DepartmentTreeVO node = nodeList.get(i);
            if (preIndex > -1) {
                node.setPreId(nodeList.get(preIndex).getDepartmentId());
            }
            if (nextIndex < nodeSize) {
                node.setNextId(nodeList.get(nextIndex).getDepartmentId());
            }

            List<DepartmentTreeVO> children = getChildren(node.getDepartmentId(), allDepartmentList);

            List<Long> tempChildIdList = new ArrayList<>();
            if (CollUtil.isNotEmpty(children)) {
                node.setChildren(children);
                tempChildIdList = this.recursiveBuildTree(children, allDepartmentList);
            }

            if (CollUtil.isEmpty(node.getSelfAndAllChildrenIdList())) {
                node.setSelfAndAllChildrenIdList(new ArrayList<>());
            }
            node.getSelfAndAllChildrenIdList().add(node.getDepartmentId());

            if (CollUtil.isNotEmpty(tempChildIdList)) {
                node.getSelfAndAllChildrenIdList().addAll(tempChildIdList);
                childIdList.addAll(tempChildIdList);
            }

        }

        // 保证本层遍历顺序
        for (int i = nodeSize - 1; i >= 0; i--) {
            childIdList.add(0, nodeList.get(i).getDepartmentId());
        }

        return childIdList;
    }


    /**
     * 获取子元素
     */
    private List<DepartmentTreeVO> getChildren(Long departmentId, List<DeptDO> doList) {
        List<DeptDO> childrenEntityList = doList.stream()
                .filter(e -> departmentId.equals(e.getParentId()))
                .toList();
        if (CollUtil.isEmpty(childrenEntityList)) {
            return Collections.emptyList();
        }
        return childrenEntityList.stream().map(this::toTreeVO).toList();
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

    private DepartmentVO toDepartmentVO(DeptDO deptDO) {
        DepartmentVO vo = new DepartmentVO();
        vo.setDeptId(deptDO.getDeptId());
        vo.setDeptName(deptDO.getDeptName());
        return vo;
    }

    private DepartmentTreeVO toTreeVO(DeptDO deptDO) {
        DepartmentTreeVO vo = new DepartmentTreeVO();
        vo.setDepartmentId(deptDO.getDeptId());
        vo.setDepartmentName(deptDO.getDeptName());
        vo.setManagerId(deptDO.getManagerId());
        vo.setParentId(deptDO.getParentId());
        vo.setSort(deptDO.getSort());
        vo.setUpdateTime(deptDO.getUpdateTime());
        vo.setCreateTime(deptDO.getCreateTime());
        return vo;
    }
}
