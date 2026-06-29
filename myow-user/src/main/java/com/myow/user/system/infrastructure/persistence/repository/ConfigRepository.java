package com.myow.user.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.mybatis.util.MyPageUtil;
import com.myow.user.system.application.dto.PageConfigReqDTO;
import com.myow.user.system.infrastructure.persistence.mapper.ConfigMapper;
import com.myow.user.system.infrastructure.persistence.po.ConfigDO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ConfigRepository extends ServiceImpl<ConfigMapper, ConfigDO> {

    public static final long GLOBAL_TENANT_ID = 0L;

    public Page<ConfigDO> selectPage(PageConfigReqDTO reqDTO) {
        Page<ConfigDO> page = MyPageUtil.convert2PageQuery(reqDTO, ConfigDO.class);
        LambdaQueryWrapper<ConfigDO> queryWrapper = Wrappers.lambdaQuery();
        if (reqDTO.getTenantId() != null) {
            queryWrapper.eq(ConfigDO::getTenantId, reqDTO.getTenantId());
        }
        if (StringUtils.hasText(reqDTO.getConfigKey())) {
            queryWrapper.like(ConfigDO::getConfigKey, reqDTO.getConfigKey());
        }
        if (StringUtils.hasText(reqDTO.getGroupCode())) {
            queryWrapper.eq(ConfigDO::getGroupCode, reqDTO.getGroupCode());
        }
        queryWrapper.eq(ConfigDO::getDeletedFlag, false);
        return this.page(page, queryWrapper);
    }

    public ConfigDO getByTenantAndKey(Long tenantId, String configKey) {
        Long effectiveTenantId = tenantId == null ? GLOBAL_TENANT_ID : tenantId;
        return this.getOne(Wrappers.<ConfigDO>lambdaQuery()
                .eq(ConfigDO::getTenantId, effectiveTenantId)
                .eq(ConfigDO::getConfigKey, configKey)
                .eq(ConfigDO::getDeletedFlag, false));
    }

    public ConfigDO getEffectiveConfig(Long tenantId, String configKey) {
        if (tenantId != null && tenantId > GLOBAL_TENANT_ID) {
            ConfigDO tenantConfig = getByTenantAndKey(tenantId, configKey);
            if (tenantConfig != null) {
                return tenantConfig;
            }
        }
        return getByTenantAndKey(GLOBAL_TENANT_ID, configKey);
    }
}
