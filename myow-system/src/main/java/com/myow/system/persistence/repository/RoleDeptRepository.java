package com.myow.system.persistence.repository;

import com.myow.system.persistence.po.RoleDeptDO;
import com.myow.system.persistence.mapper.RoleDeptMapper;
import com.myow.system.persistence.service.RoleDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色和部门关联表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class RoleDeptRepository extends ServiceImpl<RoleDeptMapper, RoleDeptDO> implements RoleDeptService {

}
