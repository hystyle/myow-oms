package com.myow.system.application.service;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.TenantApplicationConverter;
import com.myow.system.application.dto.CreateTenantReqDTO;
import com.myow.system.application.dto.PageTenantReqDTO;
import com.myow.system.application.dto.TenantRespDTO;
import com.myow.system.application.dto.UpdateTenantReqDTO;
import com.myow.system.domain.entity.Tenant;
import com.myow.system.infrastructure.converter.TenantConverter;
import com.myow.system.infrastructure.persistence.po.TenantDO;
import com.myow.system.infrastructure.persistence.po.TenantPlansDO;
import com.myow.system.infrastructure.persistence.po.UserDO;
import com.myow.system.infrastructure.persistence.repository.TenantPlansRepository;
import com.myow.system.infrastructure.persistence.repository.TenantRepository;
import com.myow.system.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final TenantApplicationConverter tenantApplicationConverter;
    private final TenantConverter tenantConverter;
    private final TenantPlansRepository tenantPlansRepository;
    private final UserRepository userRepository;

    public Long createTenant(CreateTenantReqDTO createReqDTO) {
        validateTenantForCreate(createReqDTO);
        Tenant tenant = tenantApplicationConverter.convert(createReqDTO);
        tenantRepository.save(tenantConverter.toDo(tenant));
        return tenant.getTenantId();
    }

    public void updateTenant(UpdateTenantReqDTO updateReqDTO) {
        validateTenantForUpdate(updateReqDTO);
        Tenant tenant = tenantApplicationConverter.convert(updateReqDTO);
        tenantRepository.updateById(tenantConverter.toDo(tenant));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTenant(Long id) {
        TenantDO existTenant = tenantRepository.getById(id);
        if (Objects.isNull(existTenant)) {
            throw new BusinessException(UserErrorCode.TENANT_NOT_EXIST);
        }
        
        Long userCount = userRepository.count(Wrappers.lambdaQuery(UserDO.class)
            .eq(UserDO::getTenantId, existTenant.getTenantCode()));
        if (userCount > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "该租户存在用户，无法删除");
        }
        
        tenantRepository.removeById(id);
    }

    public TenantRespDTO getTenant(Long id) {
        TenantDO tenantDO = tenantRepository.getById(id);
        if (Objects.isNull(tenantDO)) {
            throw new BusinessException(UserErrorCode.TENANT_NOT_EXIST);
        }
        return tenantApplicationConverter.convert(tenantConverter.toEntity(tenantDO));
    }

    public PageResult<TenantRespDTO> getTenantPage(PageTenantReqDTO pageTenantReqDTO) {
        Page<TenantDO> tenantDOPage = tenantRepository.selectPage(pageTenantReqDTO);
        return MyPageUtil.of(tenantDOPage, tenantApplicationConverter::convert);
    }

    private void validateTenantForCreate(CreateTenantReqDTO createReqDTO) {
        if (StrUtil.isBlank(createReqDTO.getTenantCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "租户编码不能为空");
        }
        
        if (!createReqDTO.getTenantCode().matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "租户编码格式不正确，只能包含字母、数字和下划线，且必须以字母开头");
        }
        
        if (StrUtil.isBlank(createReqDTO.getName())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "租户名称不能为空");
        }
        
        if (Objects.nonNull(createReqDTO.getStatus()) && 
            !Objects.equals(createReqDTO.getStatus(), "0") && 
            !Objects.equals(createReqDTO.getStatus(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        if (StrUtil.isNotBlank(createReqDTO.getContactPhone()) && !PhoneUtil.isMobile(createReqDTO.getContactPhone())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "联系电话格式不正确");
        }
        
        if (Objects.nonNull(createReqDTO.getExpireTime()) && createReqDTO.getExpireTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "服务到期时间不能早于当前时间");
        }
        
        if (Objects.nonNull(createReqDTO.getAccountCount()) && createReqDTO.getAccountCount() < -1) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户数量限制不能小于-1");
        }
        
        if (Objects.nonNull(createReqDTO.getPlansId())) {
            TenantPlansDO plans = tenantPlansRepository.getById(createReqDTO.getPlansId());
            if (Objects.isNull(plans)) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "租户套餐不存在");
            }
        }
        
        Long countByCode = tenantRepository.count(Wrappers.lambdaQuery(TenantDO.class)
            .eq(TenantDO::getTenantCode, createReqDTO.getTenantCode()));
        if (countByCode > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "租户编码已存在");
        }
        
        Long countByName = tenantRepository.count(Wrappers.lambdaQuery(TenantDO.class)
            .eq(TenantDO::getName, createReqDTO.getName()));
        if (countByName > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "租户名称已存在");
        }
    }

    private void validateTenantForUpdate(UpdateTenantReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getTenantId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "租户ID不能为空");
        }
        
        TenantDO existTenant = tenantRepository.getById(updateReqDTO.getTenantId());
        if (Objects.isNull(existTenant)) {
            throw new BusinessException(UserErrorCode.TENANT_NOT_EXIST);
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getTenantCode())) {
            if (!updateReqDTO.getTenantCode().matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "租户编码格式不正确，只能包含字母、数字和下划线，且必须以字母开头");
            }
        }
        
        if (Objects.nonNull(updateReqDTO.getStatus()) && 
            !Objects.equals(updateReqDTO.getStatus(), "0") && 
            !Objects.equals(updateReqDTO.getStatus(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getContactPhone()) && !PhoneUtil.isMobile(updateReqDTO.getContactPhone())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "联系电话格式不正确");
        }
        
        if (Objects.nonNull(updateReqDTO.getExpireTime()) && updateReqDTO.getExpireTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "服务到期时间不能早于当前时间");
        }
        
        if (Objects.nonNull(updateReqDTO.getAccountCount()) && updateReqDTO.getAccountCount() < -1) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户数量限制不能小于-1");
        }
        
        if (Objects.nonNull(updateReqDTO.getPlansId())) {
            TenantPlansDO plans = tenantPlansRepository.getById(updateReqDTO.getPlansId());
            if (Objects.isNull(plans)) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "租户套餐不存在");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getTenantCode()) && 
            !Objects.equals(existTenant.getTenantCode(), updateReqDTO.getTenantCode())) {
            Long countByCode = tenantRepository.count(Wrappers.lambdaQuery(TenantDO.class)
                .eq(TenantDO::getTenantCode, updateReqDTO.getTenantCode())
                .ne(TenantDO::getTenantId, updateReqDTO.getTenantId()));
            if (countByCode > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "租户编码已存在");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getName()) && 
            !Objects.equals(existTenant.getName(), updateReqDTO.getName())) {
            Long countByName = tenantRepository.count(Wrappers.lambdaQuery(TenantDO.class)
                .eq(TenantDO::getName, updateReqDTO.getName())
                .ne(TenantDO::getTenantId, updateReqDTO.getTenantId()));
            if (countByName > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "租户名称已存在");
            }
        }
    }
}
