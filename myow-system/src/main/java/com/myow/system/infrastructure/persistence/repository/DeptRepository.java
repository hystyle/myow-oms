package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.mapper.DeptMapper;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import org.springframework.stereotype.Repository;

/**
 * @author yss
 */
@Repository
public class DeptRepository extends ServiceImpl<DeptMapper, DeptDO> {
}