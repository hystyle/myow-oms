package com.myow.system.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.persistence.mapper.TenantPlansMapper;
import com.myow.system.persistence.po.TenantPlansDO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户套餐表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class TenantPlansRepository extends ServiceImpl<TenantPlansMapper, TenantPlansDO> {

}
