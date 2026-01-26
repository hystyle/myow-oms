package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageUserReqDTO;
import com.myow.system.infrastructure.persistence.mapper.UserMapper;
import com.myow.system.infrastructure.persistence.po.UserDO;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Repository
public class UserRepository extends ServiceImpl<UserMapper, UserDO> {

    public Page<UserDO> selectPage(PageUserReqDTO reqDTO) {
        Page<UserDO> page = MyPageUtil.convert2PageQuery(reqDTO, UserDO.class);
        return this.page(page, Wrappers.emptyWrapper());
    }

}
