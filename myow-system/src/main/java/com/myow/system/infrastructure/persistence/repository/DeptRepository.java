package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.mapper.DeptMapper;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class DeptRepository extends ServiceImpl<DeptMapper, DeptDO>{

}
