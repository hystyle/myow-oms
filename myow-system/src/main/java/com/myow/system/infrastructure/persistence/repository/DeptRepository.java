package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageDeptReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.DeptMapper;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import org.springframework.stereotype.Repository;

/**
 * @author yss
 */
@Repository
public class DeptRepository extends ServiceImpl<DeptMapper, DeptDO> {

    public Page<DeptDO> selectPage(PageDeptReqDTO reqDTO) {
        Page<DeptDO> page = MyPageUtil.convert2PageQuery(reqDTO, DeptDO.class);
        // Assuming there's a QueryWrapper or similar to filter the page.
        // For simplicity, using emptyWrapper for now.
        return this.page(page, Wrappers.emptyWrapper());
    }
}