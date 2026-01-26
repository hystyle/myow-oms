package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageRoleDeptReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.RoleDeptMapper;
import com.myow.system.infrastructure.persistence.po.RoleDeptDO;
import org.springframework.stereotype.Repository;

/**
 * @author yss
 */
@Repository
public class RoleDeptRepository extends ServiceImpl<RoleDeptMapper, RoleDeptDO> {

    // For join tables with composite keys, getById, removeById, updateById might not work directly.
    // They will need to be implemented using query wrappers in the service or repository.
    // The selectPage method will also need appropriate query conditions.

    public Page<RoleDeptDO> selectPage(PageRoleDeptReqDTO reqDTO) {
        Page<RoleDeptDO> page = MyPageUtil.convert2PageQuery(reqDTO, RoleDeptDO.class);
        LambdaQueryWrapper<RoleDeptDO> queryWrapper = Wrappers.lambdaQuery();

        if (reqDTO.getRoleId() != null) {
            queryWrapper.eq(RoleDeptDO::getRoleId, reqDTO.getRoleId());
        }
        if (reqDTO.getDeptId() != null) {
            queryWrapper.eq(RoleDeptDO::getDeptId, reqDTO.getDeptId());
        }

        return this.page(page, queryWrapper);
    }

    public RoleDeptDO getByCompositeKey(Long roleId, Long deptId) {
        return this.getOne(Wrappers.<RoleDeptDO>lambdaQuery()
                .eq(RoleDeptDO::getRoleId, roleId)
                .eq(RoleDeptDO::getDeptId, deptId));
    }

    public boolean removeByCompositeKey(Long roleId, Long deptId) {
        return this.remove(Wrappers.<RoleDeptDO>lambdaQuery()
                .eq(RoleDeptDO::getRoleId, roleId)
                .eq(RoleDeptDO::getDeptId, deptId));
    }
}