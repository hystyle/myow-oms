package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageRoleReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.RoleMapper;
import com.myow.system.infrastructure.persistence.po.RoleDO;
import org.springframework.stereotype.Repository;

/**
 * @author gemini
 */
@Repository
public class RoleRepository extends ServiceImpl<RoleMapper, RoleDO> {

    public Page<RoleDO> selectPage(PageRoleReqDTO reqDTO) {
        Page<RoleDO> page = MyPageUtil.convert2PageQuery(reqDTO, RoleDO.class);
        // Assuming there's a QueryWrapper or similar to filter the page.
        // For simplicity, using emptyWrapper for now.
        return this.page(page, Wrappers.emptyWrapper());
    }
}