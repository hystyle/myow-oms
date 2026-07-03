package com.myow.overseas.application.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.customer.application.dto.CustomerModels.RoleValidateCommand;
import com.myow.customer.application.service.CustomerService;
import com.myow.overseas.application.base.dto.OverseasBaseModels.IdCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsChannelCreateCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsChannelPageQuery;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsChannelUpdateCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsProductCreateCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsProductPageQuery;
import com.myow.overseas.application.base.dto.OverseasBaseModels.LogisticsProductUpdateCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.StatusCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.WarehouseCreateCommand;
import com.myow.overseas.application.base.dto.OverseasBaseModels.WarehousePageQuery;
import com.myow.overseas.application.base.dto.OverseasBaseModels.WarehouseUpdateCommand;
import com.myow.overseas.application.base.vo.LogisticsChannelVO;
import com.myow.overseas.application.base.vo.LogisticsProductVO;
import com.myow.overseas.application.base.vo.WarehouseVO;
import com.myow.overseas.infrastructure.persistence.po.LogisticsChannelDO;
import com.myow.overseas.infrastructure.persistence.po.LogisticsProductDO;
import com.myow.overseas.infrastructure.persistence.po.WarehouseDO;
import com.myow.overseas.infrastructure.persistence.repository.LogisticsChannelRepository;
import com.myow.overseas.infrastructure.persistence.repository.LogisticsProductRepository;
import com.myow.overseas.infrastructure.persistence.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OverseasBaseDataService {

    private static final long DEFAULT_TENANT_ID = 1L;

    private final WarehouseRepository warehouseRepository;
    private final LogisticsProductRepository productRepository;
    private final LogisticsChannelRepository channelRepository;
    private final CustomerService customerService;

    public OverseasBaseDataService(WarehouseRepository warehouseRepository,
                                   LogisticsProductRepository productRepository,
                                   LogisticsChannelRepository channelRepository,
                                   CustomerService customerService) {
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.channelRepository = channelRepository;
        this.customerService = customerService;
    }

    @Transactional(rollbackFor = Exception.class)
    public WarehouseVO createWarehouse(WarehouseCreateCommand command) {
        validateText(command.warehouseCode(), "warehouseCode is required");
        validateText(command.warehouseName(), "warehouseName is required");
        validateId(command.serviceProviderCustomerId());
        validateText(command.countryCode(), "countryCode is required");
        validateText(command.timezone(), "timezone is required");
        Long tenantId = tenantId(command.tenantId());
        if (warehouseRepository.existsByCode(tenantId, command.warehouseCode(), null)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "warehouse code already exists");
        }
        validateCustomerRole(command.serviceProviderCustomerId(), "WAREHOUSE_PROVIDER");
        WarehouseDO data = new WarehouseDO()
                .setTenantId(tenantId)
                .setWarehouseCode(command.warehouseCode())
                .setWarehouseName(command.warehouseName())
                .setServiceProviderCustomerId(command.serviceProviderCustomerId())
                .setCooperationType(command.cooperationType())
                .setWmsSystemId(command.wmsSystemId())
                .setExternalWarehouseCode(command.externalWarehouseCode())
                .setCountryCode(command.countryCode())
                .setState(command.state())
                .setCity(command.city())
                .setPostalCode(command.postalCode())
                .setAddressLine1(command.addressLine1())
                .setAddressLine2(command.addressLine2())
                .setContactName(command.contactName())
                .setContactPhone(command.contactPhone())
                .setContactEmail(command.contactEmail())
                .setTimezone(command.timezone())
                .setStatus("DRAFT")
                .setRemark(command.remark())
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        warehouseRepository.save(data);
        return toWarehouseVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public WarehouseVO updateWarehouse(WarehouseUpdateCommand command) {
        validateId(command.warehouseId());
        WarehouseDO data = mustGetWarehouse(command.warehouseId());
        validateId(command.serviceProviderCustomerId());
        validateText(command.warehouseName(), "warehouseName is required");
        validateText(command.countryCode(), "countryCode is required");
        validateText(command.timezone(), "timezone is required");
        validateCustomerRole(command.serviceProviderCustomerId(), "WAREHOUSE_PROVIDER");
        data.setWarehouseName(command.warehouseName())
                .setServiceProviderCustomerId(command.serviceProviderCustomerId())
                .setCooperationType(command.cooperationType())
                .setWmsSystemId(command.wmsSystemId())
                .setExternalWarehouseCode(command.externalWarehouseCode())
                .setCountryCode(command.countryCode())
                .setState(command.state())
                .setCity(command.city())
                .setPostalCode(command.postalCode())
                .setAddressLine1(command.addressLine1())
                .setAddressLine2(command.addressLine2())
                .setContactName(command.contactName())
                .setContactPhone(command.contactPhone())
                .setContactEmail(command.contactEmail())
                .setTimezone(command.timezone())
                .setRemark(command.remark())
                .setUpdateTime(LocalDateTime.now());
        warehouseRepository.updateById(data);
        return toWarehouseVO(data);
    }

    public WarehouseVO warehouseDetail(IdCommand command) {
        validateId(command.id());
        return toWarehouseVO(mustGetWarehouse(command.id()));
    }

    public PageResult<WarehouseVO> pageWarehouses(WarehousePageQuery query) {
        Page<WarehouseDO> page = warehouseRepository.selectPage(tenantId(query.tenantId()), query.keyword(),
                query.countryCode(), query.status(), query.serviceProviderCustomerId(), pageNum(query.pageNum()), pageSize(query.pageSize()));
        return pageResult(page, page.getRecords().stream().map(this::toWarehouseVO).toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean changeWarehouseStatus(StatusCommand command) {
        validateId(command.id());
        validateText(command.status(), "status is required");
        WarehouseDO data = mustGetWarehouse(command.id());
        if ("ENABLED".equals(command.status())) {
            validateCustomerRole(data.getServiceProviderCustomerId(), "WAREHOUSE_PROVIDER");
        }
        data.setStatus(command.status()).setUpdateTime(LocalDateTime.now());
        return warehouseRepository.updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWarehouse(IdCommand command) {
        validateId(command.id());
        WarehouseDO data = mustGetWarehouse(command.id());
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return warehouseRepository.updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public LogisticsProductVO createProduct(LogisticsProductCreateCommand command) {
        validateText(command.productCode(), "productCode is required");
        validateText(command.productName(), "productName is required");
        validateId(command.carrierCustomerId());
        validateText(command.productType(), "productType is required");
        Long tenantId = tenantId(command.tenantId());
        if (productRepository.existsByCode(tenantId, command.productCode(), null)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "logistics product code already exists");
        }
        validateCustomerRole(command.carrierCustomerId(), "CARRIER");
        LogisticsProductDO data = new LogisticsProductDO()
                .setTenantId(tenantId)
                .setProductCode(command.productCode())
                .setProductName(command.productName())
                .setCarrierCustomerId(command.carrierCustomerId())
                .setProductType(command.productType())
                .setDefaultChannelId(command.defaultChannelId())
                .setDefaultDecisionStrategy(command.defaultDecisionStrategy())
                .setStatus("DRAFT")
                .setRemark(command.remark())
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        productRepository.save(data);
        return toProductVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public LogisticsProductVO updateProduct(LogisticsProductUpdateCommand command) {
        validateId(command.productId());
        LogisticsProductDO data = mustGetProduct(command.productId());
        validateText(command.productName(), "productName is required");
        validateId(command.carrierCustomerId());
        validateText(command.productType(), "productType is required");
        validateCustomerRole(command.carrierCustomerId(), "CARRIER");
        data.setProductName(command.productName())
                .setCarrierCustomerId(command.carrierCustomerId())
                .setProductType(command.productType())
                .setDefaultChannelId(command.defaultChannelId())
                .setDefaultDecisionStrategy(command.defaultDecisionStrategy())
                .setRemark(command.remark())
                .setUpdateTime(LocalDateTime.now());
        productRepository.updateById(data);
        return toProductVO(data);
    }

    public LogisticsProductVO productDetail(IdCommand command) {
        validateId(command.id());
        return toProductVO(mustGetProduct(command.id()));
    }

    public PageResult<LogisticsProductVO> pageProducts(LogisticsProductPageQuery query) {
        Page<LogisticsProductDO> page = productRepository.selectPage(tenantId(query.tenantId()), query.keyword(),
                query.productType(), query.status(), query.carrierCustomerId(), pageNum(query.pageNum()), pageSize(query.pageSize()));
        return pageResult(page, page.getRecords().stream().map(this::toProductVO).toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean changeProductStatus(StatusCommand command) {
        validateId(command.id());
        validateText(command.status(), "status is required");
        LogisticsProductDO data = mustGetProduct(command.id());
        if ("ENABLED".equals(command.status())) {
            validateCustomerRole(data.getCarrierCustomerId(), "CARRIER");
        }
        data.setStatus(command.status()).setUpdateTime(LocalDateTime.now());
        return productRepository.updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(IdCommand command) {
        validateId(command.id());
        LogisticsProductDO data = mustGetProduct(command.id());
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return productRepository.updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public LogisticsChannelVO createChannel(LogisticsChannelCreateCommand command) {
        validateText(command.channelCode(), "channelCode is required");
        validateText(command.channelName(), "channelName is required");
        validateId(command.carrierCustomerId());
        validateText(command.labelSource(), "labelSource is required");
        Long tenantId = tenantId(command.tenantId());
        if (channelRepository.existsByCode(tenantId, command.channelCode(), null)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "logistics channel code already exists");
        }
        validateCustomerRole(command.carrierCustomerId(), "CARRIER");
        LogisticsChannelDO data = new LogisticsChannelDO()
                .setTenantId(tenantId)
                .setChannelCode(command.channelCode())
                .setChannelName(command.channelName())
                .setCarrierCustomerId(command.carrierCustomerId())
                .setChannelType(command.channelType())
                .setLabelSource(command.labelSource())
                .setTmsSystemId(command.tmsSystemId())
                .setLabelFormat(StringUtils.hasText(command.labelFormat()) ? command.labelFormat() : "PDF")
                .setStatus("DRAFT")
                .setRemark(command.remark())
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        channelRepository.save(data);
        return toChannelVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public LogisticsChannelVO updateChannel(LogisticsChannelUpdateCommand command) {
        validateId(command.channelId());
        LogisticsChannelDO data = mustGetChannel(command.channelId());
        validateText(command.channelName(), "channelName is required");
        validateId(command.carrierCustomerId());
        validateText(command.labelSource(), "labelSource is required");
        validateCustomerRole(command.carrierCustomerId(), "CARRIER");
        data.setChannelName(command.channelName())
                .setCarrierCustomerId(command.carrierCustomerId())
                .setChannelType(command.channelType())
                .setLabelSource(command.labelSource())
                .setTmsSystemId(command.tmsSystemId())
                .setLabelFormat(StringUtils.hasText(command.labelFormat()) ? command.labelFormat() : "PDF")
                .setRemark(command.remark())
                .setUpdateTime(LocalDateTime.now());
        channelRepository.updateById(data);
        return toChannelVO(data);
    }

    public LogisticsChannelVO channelDetail(IdCommand command) {
        validateId(command.id());
        return toChannelVO(mustGetChannel(command.id()));
    }

    public PageResult<LogisticsChannelVO> pageChannels(LogisticsChannelPageQuery query) {
        Page<LogisticsChannelDO> page = channelRepository.selectPage(tenantId(query.tenantId()), query.keyword(),
                query.labelSource(), query.status(), query.carrierCustomerId(), pageNum(query.pageNum()), pageSize(query.pageSize()));
        return pageResult(page, page.getRecords().stream().map(this::toChannelVO).toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean changeChannelStatus(StatusCommand command) {
        validateId(command.id());
        validateText(command.status(), "status is required");
        LogisticsChannelDO data = mustGetChannel(command.id());
        if ("ENABLED".equals(command.status())) {
            validateCustomerRole(data.getCarrierCustomerId(), "CARRIER");
        }
        data.setStatus(command.status()).setUpdateTime(LocalDateTime.now());
        return channelRepository.updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteChannel(IdCommand command) {
        validateId(command.id());
        LogisticsChannelDO data = mustGetChannel(command.id());
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return channelRepository.updateById(data);
    }

    private void validateCustomerRole(Long customerId, String roleType) {
        customerService.validateRole(new RoleValidateCommand(customerId, roleType));
    }

    private WarehouseDO mustGetWarehouse(Long id) {
        WarehouseDO data = warehouseRepository.getById(id);
        if (data == null || Boolean.TRUE.equals(data.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "physical warehouse not found");
        }
        return data;
    }

    private LogisticsProductDO mustGetProduct(Long id) {
        LogisticsProductDO data = productRepository.getById(id);
        if (data == null || Boolean.TRUE.equals(data.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "logistics product not found");
        }
        return data;
    }

    private LogisticsChannelDO mustGetChannel(Long id) {
        LogisticsChannelDO data = channelRepository.getById(id);
        if (data == null || Boolean.TRUE.equals(data.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "logistics channel not found");
        }
        return data;
    }

    private WarehouseVO toWarehouseVO(WarehouseDO data) {
        return new WarehouseVO(data.getWarehouseId(), data.getTenantId(), data.getWarehouseCode(), data.getWarehouseName(),
                data.getServiceProviderCustomerId(), data.getCooperationType(), data.getWmsSystemId(),
                data.getExternalWarehouseCode(), data.getCountryCode(), data.getState(), data.getCity(),
                data.getPostalCode(), data.getAddressLine1(), data.getAddressLine2(), data.getContactName(),
                data.getContactPhone(), data.getContactEmail(), data.getTimezone(), data.getStatus(),
                data.getRemark(), data.getCreateTime(), data.getUpdateTime());
    }

    private LogisticsProductVO toProductVO(LogisticsProductDO data) {
        return new LogisticsProductVO(data.getProductId(), data.getTenantId(), data.getProductCode(), data.getProductName(),
                data.getCarrierCustomerId(), data.getProductType(), data.getDefaultChannelId(),
                data.getDefaultDecisionStrategy(), data.getStatus(), data.getRemark(), data.getCreateTime(), data.getUpdateTime());
    }

    private LogisticsChannelVO toChannelVO(LogisticsChannelDO data) {
        return new LogisticsChannelVO(data.getChannelId(), data.getTenantId(), data.getChannelCode(), data.getChannelName(),
                data.getCarrierCustomerId(), data.getChannelType(), data.getLabelSource(), data.getTmsSystemId(),
                data.getLabelFormat(), data.getStatus(), data.getRemark(), data.getCreateTime(), data.getUpdateTime());
    }

    private <T> PageResult<T> pageResult(Page<?> page, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setList(records);
        result.setEmptyFlag(records.isEmpty());
        return result;
    }

    private Long tenantId(Long tenantId) {
        return tenantId == null ? DEFAULT_TENANT_ID : tenantId;
    }

    private long pageNum(Long pageNum) {
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    private long pageSize(Long pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 20;
        }
        return Math.min(pageSize, 200);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "id is required");
        }
    }

    private void validateText(String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, message);
        }
    }
}
