package com.myow.system.persistence.repository;

import com.myow.system.persistence.po.TenantDO;
import com.myow.system.persistence.mapper.TenantMapper;
import com.myow.system.persistence.service.TenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class TenantRepository extends ServiceImpl<TenantMapper, TenantDO> implements TenantService {

}
