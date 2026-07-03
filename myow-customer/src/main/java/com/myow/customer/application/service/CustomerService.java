package com.myow.customer.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.customer.application.dto.CustomerModels.AddressCreateCommand;
import com.myow.customer.application.dto.CustomerModels.AddressPageQuery;
import com.myow.customer.application.dto.CustomerModels.AddressUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.ContactCreateCommand;
import com.myow.customer.application.dto.CustomerModels.ContactPageQuery;
import com.myow.customer.application.dto.CustomerModels.ContactUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.CustomerCreateCommand;
import com.myow.customer.application.dto.CustomerModels.CustomerPageQuery;
import com.myow.customer.application.dto.CustomerModels.CustomerStatusCommand;
import com.myow.customer.application.dto.CustomerModels.CustomerUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.IdCommand;
import com.myow.customer.application.vo.CustomerAddressVO;
import com.myow.customer.application.vo.CustomerContactVO;
import com.myow.customer.application.vo.CustomerVO;
import com.myow.customer.infrastructure.persistence.po.CustomerAddressDO;
import com.myow.customer.infrastructure.persistence.po.CustomerContactDO;
import com.myow.customer.infrastructure.persistence.po.CustomerDO;
import com.myow.customer.infrastructure.persistence.repository.CustomerAddressRepository;
import com.myow.customer.infrastructure.persistence.repository.CustomerContactRepository;
import com.myow.customer.infrastructure.persistence.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {

    private static final long DEFAULT_TENANT_ID = 1L;

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository contactRepository;
    private final CustomerAddressRepository addressRepository;

    public CustomerService(CustomerRepository customerRepository,
                           CustomerContactRepository contactRepository,
                           CustomerAddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerVO create(CustomerCreateCommand command) {
        validateText(command.customerCode(), "customerCode is required");
        validateText(command.customerName(), "customerName is required");
        Long tenantId = tenantId(command.tenantId());
        if (customerRepository.existsByCode(tenantId, command.customerCode(), null)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer code already exists");
        }
        LocalDateTime now = LocalDateTime.now();
        CustomerDO data = new CustomerDO()
                .setTenantId(tenantId)
                .setCustomerCode(command.customerCode())
                .setCustomerName(command.customerName())
                .setCustomerType(defaultString(command.customerType(), "COMPANY"))
                .setCustomerLevel(defaultString(command.customerLevel(), "BRONZE"))
                .setBizLicenseNo(command.bizLicenseNo())
                .setTaxNo(command.taxNo())
                .setSettlementType(defaultString(command.settlementType(), "PREPAID"))
                .setDefaultCurrency(defaultString(command.defaultCurrency(), "USD"))
                .setStatus("PENDING")
                .setSalesOwnerId(command.salesOwnerId())
                .setOwnerDeptId(command.ownerDeptId())
                .setPoolStatus("PRIVATE")
                .setRegisterTime(now)
                .setRemark(command.remark())
                .setCreateTime(now)
                .setDeletedFlag(false);
        customerRepository.save(data);
        return toCustomerVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerVO update(CustomerUpdateCommand command) {
        validateId(command.customerId());
        CustomerDO data = mustGetCustomer(command.customerId());
        data.setCustomerName(defaultString(command.customerName(), data.getCustomerName()))
                .setCustomerType(defaultString(command.customerType(), data.getCustomerType()))
                .setCustomerLevel(defaultString(command.customerLevel(), data.getCustomerLevel()))
                .setBizLicenseNo(command.bizLicenseNo())
                .setTaxNo(command.taxNo())
                .setSettlementType(defaultString(command.settlementType(), data.getSettlementType()))
                .setDefaultCurrency(defaultString(command.defaultCurrency(), data.getDefaultCurrency()))
                .setSalesOwnerId(command.salesOwnerId())
                .setOwnerDeptId(command.ownerDeptId())
                .setPoolStatus(defaultString(command.poolStatus(), data.getPoolStatus()))
                .setRemark(command.remark())
                .setUpdateTime(LocalDateTime.now());
        customerRepository.updateById(data);
        return toCustomerVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean changeStatus(CustomerStatusCommand command) {
        validateId(command.customerId());
        validateText(command.status(), "status is required");
        CustomerDO data = mustGetCustomer(command.customerId());
        data.setStatus(command.status()).setUpdateTime(LocalDateTime.now());
        if ("ACTIVE".equals(command.status()) && data.getAuditTime() == null) {
            data.setAuditTime(LocalDateTime.now());
        }
        return customerRepository.updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean delete(IdCommand command) {
        validateId(command.id());
        CustomerDO data = mustGetCustomer(command.id());
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return customerRepository.updateById(data);
    }

    public CustomerVO detail(IdCommand command) {
        validateId(command.id());
        return toCustomerVO(mustGetCustomer(command.id()));
    }

    public PageResult<CustomerVO> page(CustomerPageQuery query) {
        Page<CustomerDO> page = customerRepository.selectPage(
                tenantId(query.tenantId()),
                query.keyword(),
                query.status(),
                query.salesOwnerId(),
                query.poolStatus(),
                pageNum(query.pageNum()),
                pageSize(query.pageSize()));
        return pageResult(page, page.getRecords().stream().map(this::toCustomerVO).toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerContactVO createContact(ContactCreateCommand command) {
        validateId(command.customerId());
        validateText(command.contactName(), "contactName is required");
        CustomerDO customer = mustGetCustomer(command.customerId());
        if (Boolean.TRUE.equals(command.primary())) {
            contactRepository.clearPrimary(customer.getTenantId(), customer.getCustomerId());
        }
        CustomerContactDO data = new CustomerContactDO()
                .setTenantId(customer.getTenantId())
                .setCustomerId(customer.getCustomerId())
                .setContactName(command.contactName())
                .setContactRole(command.contactRole())
                .setPosition(command.position())
                .setPhone(command.phone())
                .setEmail(command.email())
                .setSocialAccount(command.socialAccount())
                .setPrimary(Boolean.TRUE.equals(command.primary()))
                .setStatus(1)
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        contactRepository.save(data);
        return toContactVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerContactVO updateContact(ContactUpdateCommand command) {
        validateId(command.contactId());
        validateText(command.contactName(), "contactName is required");
        CustomerContactDO data = mustGetContact(command.contactId());
        if (Boolean.TRUE.equals(command.primary())) {
            contactRepository.clearPrimary(data.getTenantId(), data.getCustomerId());
        }
        data.setContactName(command.contactName())
                .setContactRole(command.contactRole())
                .setPosition(command.position())
                .setPhone(command.phone())
                .setEmail(command.email())
                .setSocialAccount(command.socialAccount())
                .setPrimary(Boolean.TRUE.equals(command.primary()))
                .setStatus(command.status() == null ? data.getStatus() : command.status())
                .setUpdateTime(LocalDateTime.now());
        contactRepository.updateById(data);
        return toContactVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteContact(IdCommand command) {
        validateId(command.id());
        CustomerContactDO data = mustGetContact(command.id());
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return contactRepository.updateById(data);
    }

    public PageResult<CustomerContactVO> pageContacts(ContactPageQuery query) {
        validateId(query.customerId());
        Page<CustomerContactDO> page = contactRepository.selectPage(query.customerId(), query.keyword(), pageNum(query.pageNum()), pageSize(query.pageSize()));
        return pageResult(page, page.getRecords().stream().map(this::toContactVO).toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerAddressVO createAddress(AddressCreateCommand command) {
        validateId(command.customerId());
        validateText(command.addressType(), "addressType is required");
        CustomerDO customer = mustGetCustomer(command.customerId());
        if (Boolean.TRUE.equals(command.defaultAddress())) {
            addressRepository.clearDefault(customer.getTenantId(), customer.getCustomerId(), command.addressType());
        }
        CustomerAddressDO data = new CustomerAddressDO()
                .setTenantId(customer.getTenantId())
                .setCustomerId(customer.getCustomerId())
                .setAddressType(command.addressType())
                .setContactName(command.contactName())
                .setPhone(command.phone())
                .setCountry(command.country())
                .setCountryCode(command.countryCode())
                .setProvince(command.province())
                .setCity(command.city())
                .setDistrict(command.district())
                .setStreet(command.street())
                .setZipCode(command.zipCode())
                .setDefaultAddress(Boolean.TRUE.equals(command.defaultAddress()))
                .setStatus(1)
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        addressRepository.save(data);
        return toAddressVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerAddressVO updateAddress(AddressUpdateCommand command) {
        validateId(command.addressId());
        validateText(command.addressType(), "addressType is required");
        CustomerAddressDO data = mustGetAddress(command.addressId());
        if (Boolean.TRUE.equals(command.defaultAddress())) {
            addressRepository.clearDefault(data.getTenantId(), data.getCustomerId(), command.addressType());
        }
        data.setAddressType(command.addressType())
                .setContactName(command.contactName())
                .setPhone(command.phone())
                .setCountry(command.country())
                .setCountryCode(command.countryCode())
                .setProvince(command.province())
                .setCity(command.city())
                .setDistrict(command.district())
                .setStreet(command.street())
                .setZipCode(command.zipCode())
                .setDefaultAddress(Boolean.TRUE.equals(command.defaultAddress()))
                .setStatus(command.status() == null ? data.getStatus() : command.status())
                .setUpdateTime(LocalDateTime.now());
        addressRepository.updateById(data);
        return toAddressVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAddress(IdCommand command) {
        validateId(command.id());
        CustomerAddressDO data = mustGetAddress(command.id());
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return addressRepository.updateById(data);
    }

    public PageResult<CustomerAddressVO> pageAddresses(AddressPageQuery query) {
        validateId(query.customerId());
        Page<CustomerAddressDO> page = addressRepository.selectPage(query.customerId(), query.addressType(), pageNum(query.pageNum()), pageSize(query.pageSize()));
        return pageResult(page, page.getRecords().stream().map(this::toAddressVO).toList());
    }

    private CustomerDO mustGetCustomer(Long id) {
        CustomerDO data = customerRepository.getById(id);
        if (data == null || Boolean.TRUE.equals(data.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer not found");
        }
        return data;
    }

    private CustomerContactDO mustGetContact(Long id) {
        CustomerContactDO data = contactRepository.getById(id);
        if (data == null || Boolean.TRUE.equals(data.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "contact not found");
        }
        return data;
    }

    private CustomerAddressDO mustGetAddress(Long id) {
        CustomerAddressDO data = addressRepository.getById(id);
        if (data == null || Boolean.TRUE.equals(data.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "address not found");
        }
        return data;
    }

    private CustomerVO toCustomerVO(CustomerDO data) {
        return new CustomerVO(data.getCustomerId(), data.getTenantId(), data.getCustomerCode(),
                data.getCustomerName(), data.getCustomerType(), data.getCustomerLevel(), data.getBizLicenseNo(),
                data.getTaxNo(), data.getSettlementType(), data.getDefaultCurrency(), data.getStatus(),
                data.getSalesOwnerId(), data.getOwnerDeptId(), data.getPoolStatus(), data.getRegisterTime(),
                data.getAuditTime(), data.getRemark(), data.getCreateTime(), data.getUpdateTime());
    }

    private CustomerContactVO toContactVO(CustomerContactDO data) {
        return new CustomerContactVO(data.getContactId(), data.getTenantId(), data.getCustomerId(), data.getContactName(),
                data.getContactRole(), data.getPosition(), data.getPhone(), data.getEmail(), data.getSocialAccount(),
                data.getPrimary(), data.getStatus(), data.getCreateTime(), data.getUpdateTime());
    }

    private CustomerAddressVO toAddressVO(CustomerAddressDO data) {
        return new CustomerAddressVO(data.getAddressId(), data.getTenantId(), data.getCustomerId(), data.getAddressType(),
                data.getContactName(), data.getPhone(), data.getCountry(), data.getCountryCode(), data.getProvince(),
                data.getCity(), data.getDistrict(), data.getStreet(), data.getZipCode(), data.getDefaultAddress(),
                data.getStatus(), data.getCreateTime(), data.getUpdateTime());
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

    private Long tenantId(Long tenantId) {
        return tenantId == null ? DEFAULT_TENANT_ID : tenantId;
    }

    private long pageNum(Long pageNum) {
        return pageNum == null || pageNum <= 0 ? 1L : pageNum;
    }

    private long pageSize(Long pageSize) {
        if (pageSize == null || pageSize <= 0) {
            return 20L;
        }
        return Math.min(pageSize, 200L);
    }

    private String defaultString(String value, String defaultValue) {
        return StringUtils.hasText(value) ? value : defaultValue;
    }
}
