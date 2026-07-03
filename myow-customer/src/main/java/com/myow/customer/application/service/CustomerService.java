package com.myow.customer.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.customer.application.dto.CustomerModels.AddressCreateCommand;
import com.myow.customer.application.dto.CustomerModels.AddressPageQuery;
import com.myow.customer.application.dto.CustomerModels.AddressUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.AttachmentCreateCommand;
import com.myow.customer.application.dto.CustomerModels.AttachmentPageQuery;
import com.myow.customer.application.dto.CustomerModels.AttachmentUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.BlacklistCheckCommand;
import com.myow.customer.application.dto.CustomerModels.BlacklistCreateCommand;
import com.myow.customer.application.dto.CustomerModels.BlacklistPageQuery;
import com.myow.customer.application.dto.CustomerModels.BlacklistUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.ContactCreateCommand;
import com.myow.customer.application.dto.CustomerModels.ContactPageQuery;
import com.myow.customer.application.dto.CustomerModels.ContactUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.CustomerCreateCommand;
import com.myow.customer.application.dto.CustomerModels.CustomerPageQuery;
import com.myow.customer.application.dto.CustomerModels.CustomerStatusCommand;
import com.myow.customer.application.dto.CustomerModels.CustomerUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.IdCommand;
import com.myow.customer.application.dto.CustomerModels.KycAuditCommand;
import com.myow.customer.application.dto.CustomerModels.KycCreateCommand;
import com.myow.customer.application.dto.CustomerModels.KycPageQuery;
import com.myow.customer.application.dto.CustomerModels.KycUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.RoleCreateCommand;
import com.myow.customer.application.dto.CustomerModels.RoleOptionQuery;
import com.myow.customer.application.dto.CustomerModels.RolePageQuery;
import com.myow.customer.application.dto.CustomerModels.RoleUpdateCommand;
import com.myow.customer.application.dto.CustomerModels.RoleValidateCommand;
import com.myow.customer.application.dto.CustomerModels.RelationCreateCommand;
import com.myow.customer.application.dto.CustomerModels.RelationPageQuery;
import com.myow.customer.application.dto.CustomerModels.RelationUpdateCommand;
import com.myow.customer.application.vo.CustomerAddressVO;
import com.myow.customer.application.vo.CustomerAttachmentVO;
import com.myow.customer.application.vo.CustomerBlacklistVO;
import com.myow.customer.application.vo.CustomerContactVO;
import com.myow.customer.application.vo.CustomerKycVO;
import com.myow.customer.application.vo.CustomerOptionVO;
import com.myow.customer.application.vo.CustomerRelationVO;
import com.myow.customer.application.vo.CustomerRoleVO;
import com.myow.customer.application.vo.CustomerVO;
import com.myow.customer.infrastructure.persistence.po.CustomerAddressDO;
import com.myow.customer.infrastructure.persistence.po.CustomerAttachmentDO;
import com.myow.customer.infrastructure.persistence.po.CustomerBlacklistDO;
import com.myow.customer.infrastructure.persistence.po.CustomerContactDO;
import com.myow.customer.infrastructure.persistence.po.CustomerDO;
import com.myow.customer.infrastructure.persistence.po.CustomerKycDO;
import com.myow.customer.infrastructure.persistence.po.CustomerRelationDO;
import com.myow.customer.infrastructure.persistence.po.CustomerRoleDO;
import com.myow.customer.infrastructure.persistence.repository.CustomerAddressRepository;
import com.myow.customer.infrastructure.persistence.repository.CustomerAttachmentRepository;
import com.myow.customer.infrastructure.persistence.repository.CustomerBlacklistRepository;
import com.myow.customer.infrastructure.persistence.repository.CustomerContactRepository;
import com.myow.customer.infrastructure.persistence.repository.CustomerKycRepository;
import com.myow.customer.infrastructure.persistence.repository.CustomerRepository;
import com.myow.customer.infrastructure.persistence.repository.CustomerRelationRepository;
import com.myow.customer.infrastructure.persistence.repository.CustomerRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerService {

    private static final long DEFAULT_TENANT_ID = 1L;

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository contactRepository;
    private final CustomerAddressRepository addressRepository;
    private final CustomerRoleRepository roleRepository;
    private final CustomerBlacklistRepository blacklistRepository;
    private final CustomerRelationRepository relationRepository;
    private final CustomerAttachmentRepository attachmentRepository;
    private final CustomerKycRepository kycRepository;

    public CustomerService(CustomerRepository customerRepository,
                           CustomerContactRepository contactRepository,
                           CustomerAddressRepository addressRepository,
                           CustomerRoleRepository roleRepository,
                           CustomerBlacklistRepository blacklistRepository,
                           CustomerRelationRepository relationRepository,
                           CustomerAttachmentRepository attachmentRepository,
                           CustomerKycRepository kycRepository) {
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
        this.roleRepository = roleRepository;
        this.blacklistRepository = blacklistRepository;
        this.relationRepository = relationRepository;
        this.attachmentRepository = attachmentRepository;
        this.kycRepository = kycRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerVO create(CustomerCreateCommand command) {
        validateText(command.customerCode(), "customerCode is required");
        validateText(command.customerName(), "customerName is required");
        Long tenantId = tenantId(command.tenantId());
        if (customerRepository.existsByCode(tenantId, command.customerCode(), null)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer code already exists");
        }
        assertNoBlacklistHit(new BlacklistCheckCommand(tenantId, null, command.taxNo(), command.bizLicenseNo(), null, null));
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
        assertNoBlacklistHit(new BlacklistCheckCommand(data.getTenantId(), data.getCustomerId(), command.taxNo(), command.bizLicenseNo(), null, null));
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
    public CustomerRoleVO createRole(RoleCreateCommand command) {
        validateId(command.customerId());
        validateText(command.roleType(), "roleType is required");
        CustomerDO customer = mustGetCustomer(command.customerId());
        if (roleRepository.existsByRoleType(customer.getTenantId(), customer.getCustomerId(), command.roleType(), null)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer role already exists");
        }
        CustomerRoleDO data = new CustomerRoleDO()
                .setTenantId(customer.getTenantId())
                .setCustomerId(customer.getCustomerId())
                .setRoleType(command.roleType())
                .setRoleStatus(defaultString(command.roleStatus(), "ACTIVE"))
                .setRoleCode(command.roleCode())
                .setOffsetEnabled(Boolean.TRUE.equals(command.offsetEnabled()))
                .setRemark(command.remark())
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        roleRepository.save(data);
        return toRoleVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerRoleVO updateRole(RoleUpdateCommand command) {
        validateId(command.customerRoleId());
        CustomerRoleDO data = mustGetRole(command.customerRoleId());
        data.setRoleStatus(defaultString(command.roleStatus(), data.getRoleStatus()))
                .setRoleCode(command.roleCode())
                .setOffsetEnabled(Boolean.TRUE.equals(command.offsetEnabled()))
                .setRemark(command.remark())
                .setUpdateTime(LocalDateTime.now());
        roleRepository.updateById(data);
        return toRoleVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(IdCommand command) {
        validateId(command.id());
        CustomerRoleDO data = mustGetRole(command.id());
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return roleRepository.updateById(data);
    }

    public PageResult<CustomerRoleVO> pageRoles(RolePageQuery query) {
        validateId(query.customerId());
        Page<CustomerRoleDO> page = roleRepository.selectPage(query.customerId(), query.roleType(), pageNum(query.pageNum()), pageSize(query.pageSize()));
        return pageResult(page, page.getRecords().stream().map(this::toRoleVO).toList());
    }

    public List<CustomerOptionVO> roleOptions(RoleOptionQuery query) {
        validateText(query.roleType(), "roleType is required");
        long limit = Math.max(1, Math.min(query.limit() == null ? 50 : query.limit(), 200));
        List<CustomerRoleDO> roles = roleRepository.listActiveByRole(tenantId(query.tenantId()), query.roleType(), limit * 2);
        Set<Long> customerIds = new LinkedHashSet<>();
        for (CustomerRoleDO role : roles) {
            customerIds.add(role.getCustomerId());
            if (customerIds.size() >= limit * 2) {
                break;
            }
        }
        if (customerIds.isEmpty()) {
            return List.of();
        }
        return customerRepository.listActiveOptions(tenantId(query.tenantId()), customerIds, query.keyword(), limit).stream()
                .map(this::toCustomerOptionVO)
                .toList();
    }

    public boolean validateRole(RoleValidateCommand command) {
        validateId(command.customerId());
        validateText(command.roleType(), "roleType is required");
        CustomerDO customer = mustGetCustomer(command.customerId());
        if (!"ACTIVE".equals(customer.getStatus())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer is not active");
        }
        if (!roleRepository.hasActiveRole(customer.getTenantId(), customer.getCustomerId(), command.roleType())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer role is not active");
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerRelationVO createRelation(RelationCreateCommand command) {
        validateId(command.parentCustomerId());
        validateId(command.childCustomerId());
        validateText(command.relationType(), "relationType is required");
        if (command.parentCustomerId().equals(command.childCustomerId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer relation cannot point to itself");
        }
        CustomerDO parent = mustGetCustomer(command.parentCustomerId());
        CustomerDO child = mustGetCustomer(command.childCustomerId());
        if (!parent.getTenantId().equals(child.getTenantId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customers must belong to the same tenant");
        }
        if (relationRepository.existsRelation(parent.getTenantId(), parent.getCustomerId(), child.getCustomerId(), command.relationType(), null)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer relation already exists");
        }
        CustomerRelationDO data = new CustomerRelationDO()
                .setTenantId(parent.getTenantId())
                .setParentCustomerId(parent.getCustomerId())
                .setChildCustomerId(child.getCustomerId())
                .setRelationType(command.relationType())
                .setSettlementIndependent(Boolean.TRUE.equals(command.settlementIndependent()))
                .setStatus(command.status() == null ? 1 : command.status())
                .setRemark(command.remark())
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        relationRepository.save(data);
        return toRelationVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerRelationVO updateRelation(RelationUpdateCommand command) {
        validateId(command.relationId());
        CustomerRelationDO data = mustGetRelation(command.relationId());
        data.setSettlementIndependent(Boolean.TRUE.equals(command.settlementIndependent()))
                .setStatus(command.status() == null ? data.getStatus() : command.status())
                .setRemark(command.remark())
                .setUpdateTime(LocalDateTime.now());
        relationRepository.updateById(data);
        return toRelationVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRelation(IdCommand command) {
        validateId(command.id());
        CustomerRelationDO data = mustGetRelation(command.id());
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return relationRepository.updateById(data);
    }

    public PageResult<CustomerRelationVO> pageRelations(RelationPageQuery query) {
        validateId(query.customerId());
        Page<CustomerRelationDO> page = relationRepository.selectPage(query.customerId(), query.relationType(), pageNum(query.pageNum()), pageSize(query.pageSize()));
        return pageResult(page, page.getRecords().stream().map(this::toRelationVO).toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerAttachmentVO createAttachment(AttachmentCreateCommand command) {
        validateId(command.customerId());
        validateText(command.attachmentType(), "attachmentType is required");
        validateId(command.fileId());
        CustomerDO customer = mustGetCustomer(command.customerId());
        CustomerAttachmentDO data = new CustomerAttachmentDO()
                .setTenantId(customer.getTenantId())
                .setCustomerId(customer.getCustomerId())
                .setAttachmentType(command.attachmentType())
                .setFileId(command.fileId())
                .setFileName(command.fileName())
                .setExpireDate(command.expireDate())
                .setAuditStatus(defaultString(command.auditStatus(), "PENDING"))
                .setRemark(command.remark())
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        attachmentRepository.save(data);
        return toAttachmentVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerAttachmentVO updateAttachment(AttachmentUpdateCommand command) {
        validateId(command.attachmentId());
        validateText(command.attachmentType(), "attachmentType is required");
        validateId(command.fileId());
        CustomerAttachmentDO data = mustGetAttachment(command.attachmentId());
        data.setAttachmentType(command.attachmentType())
                .setFileId(command.fileId())
                .setFileName(command.fileName())
                .setExpireDate(command.expireDate())
                .setAuditStatus(defaultString(command.auditStatus(), data.getAuditStatus()))
                .setRemark(command.remark())
                .setUpdateTime(LocalDateTime.now());
        attachmentRepository.updateById(data);
        return toAttachmentVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAttachment(IdCommand command) {
        validateId(command.id());
        CustomerAttachmentDO data = mustGetAttachment(command.id());
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return attachmentRepository.updateById(data);
    }

    public PageResult<CustomerAttachmentVO> pageAttachments(AttachmentPageQuery query) {
        validateId(query.customerId());
        Page<CustomerAttachmentDO> page = attachmentRepository.selectPage(query.customerId(), query.attachmentType(), query.auditStatus(), pageNum(query.pageNum()), pageSize(query.pageSize()));
        return pageResult(page, page.getRecords().stream().map(this::toAttachmentVO).toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerKycVO createKyc(KycCreateCommand command) {
        validateId(command.customerId());
        validateText(command.kycType(), "kycType is required");
        CustomerDO customer = mustGetCustomer(command.customerId());
        CustomerKycDO data = new CustomerKycDO()
                .setTenantId(customer.getTenantId())
                .setCustomerId(customer.getCustomerId())
                .setKycType(command.kycType())
                .setAuditStatus("PENDING")
                .setRemark(command.remark())
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        kycRepository.save(data);
        return toKycVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerKycVO updateKyc(KycUpdateCommand command) {
        validateId(command.kycId());
        validateText(command.kycType(), "kycType is required");
        CustomerKycDO data = mustGetKyc(command.kycId());
        data.setKycType(command.kycType())
                .setRemark(command.remark())
                .setUpdateTime(LocalDateTime.now());
        kycRepository.updateById(data);
        return toKycVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerKycVO auditKyc(KycAuditCommand command) {
        validateId(command.kycId());
        validateText(command.auditStatus(), "auditStatus is required");
        CustomerKycDO data = mustGetKyc(command.kycId());
        data.setAuditStatus(command.auditStatus())
                .setAuditBy(command.auditBy())
                .setAuditTime(LocalDateTime.now())
                .setRejectReason(command.rejectReason())
                .setUpdateTime(LocalDateTime.now());
        kycRepository.updateById(data);
        return toKycVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteKyc(IdCommand command) {
        validateId(command.id());
        CustomerKycDO data = mustGetKyc(command.id());
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return kycRepository.updateById(data);
    }

    public PageResult<CustomerKycVO> pageKycs(KycPageQuery query) {
        validateId(query.customerId());
        Page<CustomerKycDO> page = kycRepository.selectPage(query.customerId(), query.kycType(), query.auditStatus(), pageNum(query.pageNum()), pageSize(query.pageSize()));
        return pageResult(page, page.getRecords().stream().map(this::toKycVO).toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerContactVO createContact(ContactCreateCommand command) {
        validateId(command.customerId());
        validateText(command.contactName(), "contactName is required");
        CustomerDO customer = mustGetCustomer(command.customerId());
        if (Boolean.TRUE.equals(command.primary())) {
            contactRepository.clearPrimary(customer.getTenantId(), customer.getCustomerId());
        }
        assertNoBlacklistHit(new BlacklistCheckCommand(customer.getTenantId(), customer.getCustomerId(), null, null, command.phone(), command.email()));
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
        assertNoBlacklistHit(new BlacklistCheckCommand(data.getTenantId(), data.getCustomerId(), null, null, command.phone(), command.email()));
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

    @Transactional(rollbackFor = Exception.class)
    public CustomerBlacklistVO createBlacklist(BlacklistCreateCommand command) {
        Long tenantId = tenantId(command.tenantId());
        validateText(command.targetType(), "targetType is required");
        validateText(command.targetValue(), "targetValue is required");
        validateText(command.reason(), "reason is required");
        String normalizedValue = normalizeTargetValue(command.targetType(), command.targetValue());
        if (blacklistRepository.existsByTarget(tenantId, command.targetType(), normalizedValue, null)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "blacklist target already exists");
        }
        CustomerBlacklistDO data = new CustomerBlacklistDO()
                .setTenantId(tenantId)
                .setTargetType(command.targetType())
                .setTargetValue(normalizedValue)
                .setRiskLevel(defaultString(command.riskLevel(), "HIGH"))
                .setReason(command.reason())
                .setSourceCustomerId(command.sourceCustomerId())
                .setStatus(defaultString(command.status(), "ACTIVE"))
                .setEffectiveTime(command.effectiveTime())
                .setExpireTime(command.expireTime())
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        blacklistRepository.save(data);
        return toBlacklistVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerBlacklistVO updateBlacklist(BlacklistUpdateCommand command) {
        validateId(command.blacklistId());
        validateText(command.targetType(), "targetType is required");
        validateText(command.targetValue(), "targetValue is required");
        validateText(command.reason(), "reason is required");
        CustomerBlacklistDO data = mustGetBlacklist(command.blacklistId());
        String normalizedValue = normalizeTargetValue(command.targetType(), command.targetValue());
        if (blacklistRepository.existsByTarget(data.getTenantId(), command.targetType(), normalizedValue, data.getBlacklistId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "blacklist target already exists");
        }
        data.setTargetType(command.targetType())
                .setTargetValue(normalizedValue)
                .setRiskLevel(defaultString(command.riskLevel(), data.getRiskLevel()))
                .setReason(command.reason())
                .setSourceCustomerId(command.sourceCustomerId())
                .setStatus(defaultString(command.status(), data.getStatus()))
                .setEffectiveTime(command.effectiveTime())
                .setExpireTime(command.expireTime())
                .setUpdateTime(LocalDateTime.now());
        blacklistRepository.updateById(data);
        return toBlacklistVO(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBlacklist(IdCommand command) {
        validateId(command.id());
        CustomerBlacklistDO data = mustGetBlacklist(command.id());
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return blacklistRepository.updateById(data);
    }

    public PageResult<CustomerBlacklistVO> pageBlacklists(BlacklistPageQuery query) {
        Page<CustomerBlacklistDO> page = blacklistRepository.selectPage(
                tenantId(query.tenantId()),
                query.keyword(),
                query.targetType(),
                query.status(),
                pageNum(query.pageNum()),
                pageSize(query.pageSize()));
        return pageResult(page, page.getRecords().stream().map(this::toBlacklistVO).toList());
    }

    public List<CustomerBlacklistVO> checkBlacklist(BlacklistCheckCommand command) {
        return findBlacklistHits(command).stream().map(this::toBlacklistVO).toList();
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

    private CustomerRoleDO mustGetRole(Long id) {
        CustomerRoleDO data = roleRepository.getById(id);
        if (data == null || Boolean.TRUE.equals(data.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer role not found");
        }
        return data;
    }

    private CustomerRelationDO mustGetRelation(Long id) {
        CustomerRelationDO data = relationRepository.getById(id);
        if (data == null || Boolean.TRUE.equals(data.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer relation not found");
        }
        return data;
    }

    private CustomerBlacklistDO mustGetBlacklist(Long id) {
        CustomerBlacklistDO data = blacklistRepository.getById(id);
        if (data == null || Boolean.TRUE.equals(data.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "blacklist not found");
        }
        return data;
    }

    private CustomerAttachmentDO mustGetAttachment(Long id) {
        CustomerAttachmentDO data = attachmentRepository.getById(id);
        if (data == null || Boolean.TRUE.equals(data.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer attachment not found");
        }
        return data;
    }

    private CustomerKycDO mustGetKyc(Long id) {
        CustomerKycDO data = kycRepository.getById(id);
        if (data == null || Boolean.TRUE.equals(data.getDeletedFlag())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "customer KYC not found");
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

    private CustomerOptionVO toCustomerOptionVO(CustomerDO data) {
        return new CustomerOptionVO(data.getCustomerId(), data.getCustomerCode(), data.getCustomerName(), data.getStatus());
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

    private CustomerRoleVO toRoleVO(CustomerRoleDO data) {
        return new CustomerRoleVO(data.getCustomerRoleId(), data.getTenantId(), data.getCustomerId(), data.getRoleType(),
                data.getRoleStatus(), data.getRoleCode(), data.getOffsetEnabled(), data.getRemark(),
                data.getCreateTime(), data.getUpdateTime());
    }

    private CustomerBlacklistVO toBlacklistVO(CustomerBlacklistDO data) {
        return new CustomerBlacklistVO(data.getBlacklistId(), data.getTenantId(), data.getTargetType(), data.getTargetValue(),
                data.getRiskLevel(), data.getReason(), data.getSourceCustomerId(), data.getStatus(), data.getEffectiveTime(),
                data.getExpireTime(), data.getCreateTime(), data.getUpdateTime());
    }

    private CustomerRelationVO toRelationVO(CustomerRelationDO data) {
        CustomerDO parent = customerRepository.getById(data.getParentCustomerId());
        CustomerDO child = customerRepository.getById(data.getChildCustomerId());
        return new CustomerRelationVO(data.getRelationId(), data.getTenantId(), data.getParentCustomerId(),
                parent == null ? null : parent.getCustomerName(), data.getChildCustomerId(),
                child == null ? null : child.getCustomerName(), data.getRelationType(),
                data.getSettlementIndependent(), data.getStatus(), data.getRemark(),
                data.getCreateTime(), data.getUpdateTime());
    }

    private CustomerAttachmentVO toAttachmentVO(CustomerAttachmentDO data) {
        return new CustomerAttachmentVO(data.getAttachmentId(), data.getTenantId(), data.getCustomerId(),
                data.getAttachmentType(), data.getFileId(), data.getFileName(), data.getExpireDate(),
                data.getAuditStatus(), data.getRemark(), data.getCreateTime(), data.getUpdateTime());
    }

    private CustomerKycVO toKycVO(CustomerKycDO data) {
        return new CustomerKycVO(data.getKycId(), data.getTenantId(), data.getCustomerId(), data.getKycType(),
                data.getAuditStatus(), data.getAuditBy(), data.getAuditTime(), data.getRejectReason(),
                data.getRemark(), data.getCreateTime(), data.getUpdateTime());
    }

    private void assertNoBlacklistHit(BlacklistCheckCommand command) {
        List<CustomerBlacklistDO> hits = findBlacklistHits(command);
        if (!hits.isEmpty()) {
            CustomerBlacklistDO hit = hits.get(0);
            throw new BusinessException(ResultCode.PARAM_ERROR, "blacklist hit: " + hit.getTargetType() + "=" + hit.getTargetValue());
        }
    }

    private List<CustomerBlacklistDO> findBlacklistHits(BlacklistCheckCommand command) {
        Long tenantId = tenantId(command.tenantId());
        List<String> targetTypes = new ArrayList<>();
        List<String> targetValues = new ArrayList<>();
        addBlacklistTarget(targetTypes, targetValues, "CUSTOMER_ID", command.customerId() == null ? null : String.valueOf(command.customerId()));
        addBlacklistTarget(targetTypes, targetValues, "TAX_NO", command.taxNo());
        addBlacklistTarget(targetTypes, targetValues, "LICENSE_NO", command.bizLicenseNo());
        addBlacklistTarget(targetTypes, targetValues, "PHONE", command.phone());
        addBlacklistTarget(targetTypes, targetValues, "EMAIL", command.email());
        return blacklistRepository.listActiveHits(tenantId, targetTypes, targetValues, LocalDateTime.now());
    }

    private void addBlacklistTarget(List<String> types, List<String> values, String type, String value) {
        if (!StringUtils.hasText(value)) {
            return;
        }
        types.add(type);
        values.add(normalizeTargetValue(type, value));
    }

    private String normalizeTargetValue(String targetType, String value) {
        String trimmed = value == null ? "" : value.trim();
        if ("EMAIL".equals(targetType)) {
            return trimmed.toLowerCase();
        }
        if ("PHONE".equals(targetType)) {
            return trimmed.replace(" ", "").replace("-", "");
        }
        return trimmed;
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
