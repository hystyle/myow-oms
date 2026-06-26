package com.myow.user.system.application.service;

import com.myow.common.exception.BusinessException;
import com.myow.common.response.UserErrorCode;
import com.myow.user.system.BaseServiceTest;
import com.myow.user.system.application.dto.CreateTenantPlansReqDTO;
import com.myow.user.system.application.dto.TenantPlansRespDTO;
import com.myow.user.system.application.dto.UpdateTenantPlansReqDTO;
import com.myow.user.system.infrastructure.persistence.po.TenantDO;
import com.myow.user.system.infrastructure.persistence.po.TenantPlansDO;
import com.myow.user.system.infrastructure.persistence.repository.TenantPlansRepository;
import com.myow.user.system.infrastructure.persistence.repository.TenantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("TenantPlansService测试")
class TenantPlansServiceTest extends BaseServiceTest {

    @Mock
    private TenantPlansRepository tenantPlansRepository;

    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private TenantPlansService tenantPlansService;

    @Test
    @DisplayName("创建租户套餐-成功")
    void createTenantPlans_Success() {
        CreateTenantPlansReqDTO createReqDTO = new CreateTenantPlansReqDTO();
        createReqDTO.setPlansName("测试套餐");
        createReqDTO.setPlansCode("test_plans");

        when(tenantPlansRepository.save(any(TenantPlansDO.class))).thenReturn(true);

        Long plansId = tenantPlansService.createTenantPlans(createReqDTO);

        assertThat(plansId).isNotNull();
        verify(tenantPlansRepository, times(1)).save(any(TenantPlansDO.class));
    }

    @Test
    @DisplayName("创建租户套餐-套餐名称为空")
    void createTenantPlans_PlansNameBlank() {
        CreateTenantPlansReqDTO createReqDTO = new CreateTenantPlansReqDTO();
        createReqDTO.setPlansName("");
        createReqDTO.setPlansCode("test_plans");

        assertThatThrownBy(() -> tenantPlansService.createTenantPlans(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("套餐名称不能为空");
    }

    @Test
    @DisplayName("创建租户套餐-套餐代码为空")
    void createTenantPlans_PlansCodeBlank() {
        CreateTenantPlansReqDTO createReqDTO = new CreateTenantPlansReqDTO();
        createReqDTO.setPlansName("测试套餐");
        createReqDTO.setPlansCode("");

        assertThatThrownBy(() -> tenantPlansService.createTenantPlans(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("套餐代码不能为空");
    }

    @Test
    @DisplayName("创建租户套餐-套餐代码格式错误")
    void createTenantPlans_PlansCodeFormatError() {
        CreateTenantPlansReqDTO createReqDTO = new CreateTenantPlansReqDTO();
        createReqDTO.setPlansName("测试套餐");
        createReqDTO.setPlansCode("123_invalid");

        assertThatThrownBy(() -> tenantPlansService.createTenantPlans(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("套餐代码格式不正确");
    }

    @Test
    @DisplayName("创建租户套餐-套餐名称已存在")
    void createTenantPlans_PlansNameAlreadyExist() {
        CreateTenantPlansReqDTO createReqDTO = new CreateTenantPlansReqDTO();
        createReqDTO.setPlansName("existing_plans");
        createReqDTO.setPlansCode("test_plans");

        when(tenantPlansRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> tenantPlansService.createTenantPlans(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("创建租户套餐-套餐代码已存在")
    void createTenantPlans_PlansCodeAlreadyExist() {
        CreateTenantPlansReqDTO createReqDTO = new CreateTenantPlansReqDTO();
        createReqDTO.setPlansName("测试套餐");
        createReqDTO.setPlansCode("existing_code");

        when(tenantPlansRepository.count(any())).thenReturn(0L);
        when(tenantPlansRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> tenantPlansService.createTenantPlans(createReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.ALREADY_EXIST);
    }

    @Test
    @DisplayName("更新租户套餐-成功")
    void updateTenantPlans_Success() {
        UpdateTenantPlansReqDTO updateReqDTO = new UpdateTenantPlansReqDTO();
        updateReqDTO.setPlansId(1L);
        updateReqDTO.setPlansName("更新套餐");

        TenantPlansDO mockPlansDO = new TenantPlansDO();
        mockPlansDO.setPlansId(1L);
        when(tenantPlansRepository.getById(1L)).thenReturn(mockPlansDO);
        when(tenantPlansRepository.updateById(any(TenantPlansDO.class))).thenReturn(true);

        tenantPlansService.updateTenantPlans(updateReqDTO);

        verify(tenantPlansRepository, times(1)).updateById(any(TenantPlansDO.class));
    }

    @Test
    @DisplayName("更新租户套餐-套餐不存在")
    void updateTenantPlans_PlansNotExist() {
        UpdateTenantPlansReqDTO updateReqDTO = new UpdateTenantPlansReqDTO();
        updateReqDTO.setPlansId(999L);

        when(tenantPlansRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> tenantPlansService.updateTenantPlans(updateReqDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("租户套餐不存在");
    }

    @Test
    @DisplayName("删除租户套餐-成功")
    void deleteTenantPlans_Success() {
        TenantPlansDO mockPlansDO = new TenantPlansDO();
        mockPlansDO.setPlansId(1L);
        when(tenantPlansRepository.getById(1L)).thenReturn(mockPlansDO);
        when(tenantRepository.count(any())).thenReturn(0L);
        when(tenantPlansRepository.removeById(1L)).thenReturn(true);

        tenantPlansService.deleteTenantPlans(1L);

        verify(tenantPlansRepository, times(1)).removeById(1L);
    }

    @Test
    @DisplayName("删除租户套餐-套餐不存在")
    void deleteTenantPlans_PlansNotExist() {
        when(tenantPlansRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> tenantPlansService.deleteTenantPlans(999L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("租户套餐不存在");
    }

    @Test
    @DisplayName("删除租户套餐-已被租户使用")
    void deleteTenantPlans_UsedByTenant() {
        TenantPlansDO mockPlansDO = new TenantPlansDO();
        mockPlansDO.setPlansId(1L);
        when(tenantPlansRepository.getById(1L)).thenReturn(mockPlansDO);
        when(tenantRepository.count(any())).thenReturn(1L);

        assertThatThrownBy(() -> tenantPlansService.deleteTenantPlans(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("该套餐已被租户使用，无法删除");
    }

    @Test
    @DisplayName("获取租户套餐-成功")
    void getTenantPlans_Success() {
        TenantPlansDO mockPlansDO = new TenantPlansDO();
        mockPlansDO.setPlansId(1L);
        mockPlansDO.setPlansName("测试套餐");
        when(tenantPlansRepository.getById(1L)).thenReturn(mockPlansDO);

        TenantPlansRespDTO plansRespDTO = tenantPlansService.getTenantPlans(1L);

        assertThat(plansRespDTO).isNotNull();
        assertThat(plansRespDTO.getPlansId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("获取租户套餐-套餐不存在")
    void getTenantPlans_PlansNotExist() {
        when(tenantPlansRepository.getById(999L)).thenReturn(null);

        assertThatThrownBy(() -> tenantPlansService.getTenantPlans(999L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("租户套餐不存在");
    }
}