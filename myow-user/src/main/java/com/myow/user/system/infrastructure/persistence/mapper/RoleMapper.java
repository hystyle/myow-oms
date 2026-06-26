package com.myow.user.system.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myow.user.system.application.vo.RoleUserVO;
import com.myow.user.system.infrastructure.persistence.po.RoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
public interface RoleMapper extends BaseMapper<RoleDO> {

    List<RoleUserVO> getRoleByUserIdList(@Param("userIdList") List<Long> userIdList);

    List<RoleDO> getRoleByUserId(Long userId);
}
