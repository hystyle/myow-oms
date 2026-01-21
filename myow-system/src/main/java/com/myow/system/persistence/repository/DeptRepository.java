package com.myow.system.persistence.repository;

import com.myow.system.persistence.po.DeptDO;
import com.myow.system.persistence.mapper.DeptMapper;
import com.myow.system.persistence.service.DeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class DeptRepository extends ServiceImpl<DeptMapper, DeptDO> implements DeptService {

}
