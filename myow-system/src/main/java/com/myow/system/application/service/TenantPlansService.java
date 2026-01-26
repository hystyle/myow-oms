package com.myow.system.application.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.TenantPlansApplicationConverter;
import com.myow.system.application.dto.CreateTenantPlansReqDTO;
import com.myow.system.application.dto.PageTenantPlansReqDTO;
import com.myow.system.application.dto.TenantPlansRespDTO;
import com.myow.system.application.dto.UpdateTenantPlansReqDTO;
import com.myow.system.domain.entity.TenantPlans;
import com.myow.system.infrastructure.converter.TenantPlansConverter;
import com.myow.system.infrastructure.persistence.po.TenantDO;
import com.myow.system.infrastructure.persistence.po.TenantPlansDO;
import com.myow.system.infrastructure.persistence.repository.TenantPlansRepository;
import com.myow.system.infrastructure.persistence.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TenantPlansService {

    private final TenantPlansRepository tenantPlansRepository;
    private final TenantPlansApplicationConverter tenantPlansApplicationConverter;
    private final TenantPlansConverter tenantPlansConverter;
    private final TenantRepository tenantRepository;

    public Long createTenantPlans(CreateTenantPlansReqDTO createReqDTO) {
        validateTenantPlansForCreate(createReqDTO);
        TenantPlans tenantPlans = tenantPlansApplicationConverter.convert(createReqDTO);
        tenantPlansRepository.save(tenantPlansConverter.toDo(tenantPlans));
        return tenantPlans.getPlansId();
    }

    public void updateTenantPlans(UpdateTenantPlansReqDTO updateReqDTO) {
        validateTenantPlansForUpdate(updateReqDTO);
        TenantPlans tenantPlans = tenantPlansApplicationConverter.convert(updateReqDTO);
        tenantPlansRepository.updateById(tenantPlansConverter.toDo(tenantPlans));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTenantPlans(Long id) {
        TenantPlansDO existPlans = tenantPlansRepository.getById(id);
        if (Objects.isNull(existPlans)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "租户套餐不存在");
        }
        
        Long tenantCount = tenantRepository.count(Wrappers.lambdaQuery(TenantDO.class)
            .eq(TenantDO::getPlansId, id));
        if (tenantCount > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "该套餐已被租户使用，无法删除");
        }
        
        tenantPlansRepository.removeById(id);
    }

    public TenantPlansRespDTO getTenantPlans(Long id) {
        TenantPlansDO tenantPlansDO = tenantPlansRepository.getById(id);
        if (Objects.isNull(tenantPlansDO)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "租户套餐不存在");
        }
        return tenantPlansApplicationConverter.convert(tenantPlansConverter.toEntity(tenantPlansDO));
    }

    public PageResult<TenantPlansRespDTO> getTenantPlansPage(PageTenantPlansReqDTO pageTenantPlansReqDTO) {
        Page<TenantPlansDO> tenantPlansDOPage = tenantPlansRepository.selectPage(pageTenantPlansReqDTO);
        if (Objects.isNull(tenantPlansDOPage) || tenantPlansDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(tenantPlansDOPage, tenantPlansApplicationConverter::convert);
    }

    private void validateTenantPlansForCreate(CreateTenantPlansReqDTO createReqDTO) {
        if (StrUtil.isBlank(createReqDTO.getPlansName())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "套餐名称不能为空");
        }
        
        if (StrUtil.isBlank(createReqDTO.getPlansCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "套餐代码不能为空");
        }
        
        if (!createReqDTO.getPlansCode().matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "套餐代码格式不正确，只能包含字母、数字和下划线，且必须以字母开头");
        }
        
        if (Objects.nonNull(createReqDTO.getStatus()) && 
            !Objects.equals(createReqDTO.getStatus(), "0") && 
            !Objects.equals(createReqDTO.getStatus(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        Long countByName = tenantPlansRepository.count(Wrappers.lambdaQuery(TenantPlansDO.class)
            .eq(TenantPlansDO::getPlansName, createReqDTO.getPlansName()));
        if (countByName > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "套餐名称已存在");
        }
        
        Long countByCode = tenantPlansRepository.count(Wrappers.lambdaQuery(TenantPlansDO.class)
            .eq(TenantPlansDO::getPlansCode, createReqDTO.getPlansCode()));
        if (countByCode > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "套餐代码已存在");
        }
    }

    private void validateTenantPlansForUpdate(UpdateTenantPlansReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getPlansId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "套餐ID不能为空");
        }
        
        TenantPlansDO existPlans = tenantPlansRepository.getById(updateReqDTO.getPlansId());
        if (Objects.isNull(existPlans)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "租户套餐不存在");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getPlansCode())) {
            if (!updateReqDTO.getPlansCode().matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "套餐代码格式不正确，只能包含字母、数字和下划线，且必须以字母开头");
            }
        }
        
        if (Objects.nonNull(updateReqDTO.getStatus()) && 
            !Objects.equals(updateReqDTO.getStatus(), "0") && 
            !Objects.equals(updateReqDTO.getStatus(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getPlansName()) && 
            !Objects.equals(existPlans.getPlansName(), updateReqDTO.getPlansName())) {
            Long countByName = tenantPlansRepository.count(Wrappers.lambdaQuery(TenantPlansDO.class)
                .eq(TenantPlansDO::getPlansName, updateReqDTO.getPlansName())
                .ne(TenantPlansDO::getPlansId, updateReqDTO.getPlansId()));
            if (countByName > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "套餐名称已存在");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getPlansCode()) && 
            !Objects.equals(existPlans.getPlansCode(), updateReqDTO.getPlansCode())) {
            Long countByCode = tenantPlansRepository.count(Wrappers.lambdaQuery(TenantPlansDO.class)
                .eq(TenantPlansDO::getPlansCode, updateReqDTO.getPlansCode())
                .ne(TenantPlansDO::getPlansId, updateReqDTO.getPlansId()));
            if (countByCode > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "套餐代码已存在");
            }
        }
    }
}
