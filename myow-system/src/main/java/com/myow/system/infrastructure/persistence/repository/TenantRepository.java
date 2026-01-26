package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.po.TenantDO;
import com.myow.system.infrastructure.persistence.mapper.TenantMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class TenantRepository extends ServiceImpl<TenantMapper, TenantDO> {

}
