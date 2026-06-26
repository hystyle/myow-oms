package com.myow.user.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.mybatis.util.MyPageUtil;
import com.myow.user.system.application.dto.PageRoleReqDTO;
import com.myow.user.system.application.vo.RoleUserVO;
import com.myow.user.system.infrastructure.persistence.mapper.RoleMapper;
import com.myow.user.system.infrastructure.persistence.po.RoleDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yss
 */
@Repository
public class RoleRepository extends ServiceImpl<RoleMapper, RoleDO> {

    public Page<RoleDO> selectPage(PageRoleReqDTO reqDTO) {
        Page<RoleDO> page = MyPageUtil.convert2PageQuery(reqDTO, RoleDO.class);
        // Assuming there's a QueryWrapper or similar to filter the page.
        // For simplicity, using emptyWrapper for now.
        return this.page(page, Wrappers.emptyWrapper());
    }

    public List<RoleUserVO> getRoleByUserIdList(List<Long> userIdList) {
        return baseMapper.getRoleByUserIdList(userIdList);
    }

    public List<RoleDO> getRoleByUserId(Long userId) {
        return baseMapper.getRoleByUserId(userId);
    }
}