package com.myow.user.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.mybatis.util.MyPageUtil;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.user.system.application.converter.ConfigApplicationConverter;
import com.myow.user.system.application.dto.ConfigRespDTO;
import com.myow.user.system.application.dto.CreateConfigReqDTO;
import com.myow.user.system.application.dto.PageConfigReqDTO;
import com.myow.user.system.application.dto.UpdateConfigReqDTO;
import com.myow.user.system.domain.entity.Config;
import com.myow.user.system.infrastructure.converter.ConfigConverter;
import com.myow.user.system.infrastructure.persistence.po.ConfigDO;
import com.myow.user.system.infrastructure.persistence.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final ConfigRepository configRepository;
    private final ConfigApplicationConverter configApplicationConverter;
    private final ConfigConverter configConverter;

    public Long createConfig(CreateConfigReqDTO createReqDTO) {
        validateKey(createReqDTO.getConfigKey());
        Long tenantId = normalizeTenantId(createReqDTO.getTenantId());
        if (configRepository.getByTenantAndKey(tenantId, createReqDTO.getConfigKey()) != null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "config key already exists");
        }
        Config config = configApplicationConverter.convert(createReqDTO);
        ConfigDO configDO = configConverter.toDo(config);
        configDO.setTenantId(tenantId);
        if (configDO.getDeletedFlag() == null) {
            configDO.setDeletedFlag(false);
        }
        configRepository.save(configDO);
        return configDO.getConfigId();
    }

    public void updateConfig(UpdateConfigReqDTO updateReqDTO) {
        if (updateReqDTO.getConfigId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "configId is required");
        }
        ConfigDO exist = configRepository.getById(updateReqDTO.getConfigId());
        if (Objects.isNull(exist) || Boolean.TRUE.equals(exist.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "config does not exist");
        }
        Config config = configApplicationConverter.convert(updateReqDTO);
        ConfigDO configDO = configConverter.toDo(config);
        configDO.setTenantId(normalizeTenantId(updateReqDTO.getTenantId()));
        configRepository.updateById(configDO);
    }

    public void deleteConfig(Long id) {
        ConfigDO exist = configRepository.getById(id);
        if (Objects.isNull(exist) || Boolean.TRUE.equals(exist.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "config does not exist");
        }
        if (Boolean.TRUE.equals(exist.getSystemFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "system config cannot be deleted");
        }
        exist.setDeletedFlag(true);
        configRepository.updateById(exist);
    }

    public ConfigRespDTO getConfig(Long id) {
        ConfigDO configDO = configRepository.getById(id);
        return configApplicationConverter.convert(configDO);
    }

    public ConfigRespDTO getEffectiveConfig(Long tenantId, String configKey) {
        validateKey(configKey);
        return configApplicationConverter.convert(configRepository.getEffectiveConfig(tenantId, configKey));
    }

    public PageResult<ConfigRespDTO> getConfigPage(PageConfigReqDTO pageConfigReqDTO) {
        Page<ConfigDO> configDOPage = configRepository.selectPage(pageConfigReqDTO);
        if (configDOPage == null || configDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(configDOPage, configApplicationConverter::convert);
    }

    private Long normalizeTenantId(Long tenantId) {
        return tenantId == null ? ConfigRepository.GLOBAL_TENANT_ID : tenantId;
    }

    private void validateKey(String configKey) {
        if (!StringUtils.hasText(configKey)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "configKey is required");
        }
    }
}
