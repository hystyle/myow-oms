package com.myow.customer.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.LocalDate;

public final class CustomerModels {

    private CustomerModels() {
    }

    @Schema(description = "Primary id command")
    public record IdCommand(@Schema(description = "Primary id") Long id) {
    }

    @Schema(description = "Customer page query")
    public record CustomerPageQuery(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Keyword matched against customer code, customer name, license no, or tax no.") String keyword,
            @Schema(description = "Customer status: PENDING, ACTIVE, SUSPENDED, TERMINATED.") String status,
            @Schema(description = "Sales owner user id.") Long salesOwnerId,
            @Schema(description = "Pool status: PRIVATE or PUBLIC.") String poolStatus,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Create customer command")
    public record CustomerCreateCommand(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Customer code, unique in tenant.") String customerCode,
            @Schema(description = "Customer name.") String customerName,
            @Schema(description = "Customer type: COMPANY or INDIVIDUAL.") String customerType,
            @Schema(description = "Customer level, such as BRONZE, SILVER, GOLD, VIP.") String customerLevel,
            @Schema(description = "Business license number.") String bizLicenseNo,
            @Schema(description = "Tax number.") String taxNo,
            @Schema(description = "Settlement type: PREPAID, CREDIT, MONTHLY.") String settlementType,
            @Schema(description = "Default currency, such as USD, CNY.") String defaultCurrency,
            @Schema(description = "Sales owner user id.") Long salesOwnerId,
            @Schema(description = "Owner department id.") Long ownerDeptId,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Update customer command")
    public record CustomerUpdateCommand(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Customer name.") String customerName,
            @Schema(description = "Customer type: COMPANY or INDIVIDUAL.") String customerType,
            @Schema(description = "Customer level, such as BRONZE, SILVER, GOLD, VIP.") String customerLevel,
            @Schema(description = "Business license number.") String bizLicenseNo,
            @Schema(description = "Tax number.") String taxNo,
            @Schema(description = "Settlement type: PREPAID, CREDIT, MONTHLY.") String settlementType,
            @Schema(description = "Default currency, such as USD, CNY.") String defaultCurrency,
            @Schema(description = "Sales owner user id.") Long salesOwnerId,
            @Schema(description = "Owner department id.") Long ownerDeptId,
            @Schema(description = "Pool status: PRIVATE or PUBLIC.") String poolStatus,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Change customer status command")
    public record CustomerStatusCommand(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Customer status: PENDING, ACTIVE, SUSPENDED, TERMINATED.") String status) {
    }

    @Schema(description = "Customer role page query")
    public record RolePageQuery(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Role type: CUSTOMER, SUPPLIER, OVERSEAS_AGENT, CARRIER, WAREHOUSE_PROVIDER, CUSTOMS_BROKER.") String roleType,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Customer role option query")
    public record RoleOptionQuery(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Required role type: CUSTOMER, SUPPLIER, OVERSEAS_AGENT, CARRIER, WAREHOUSE_PROVIDER, CUSTOMS_BROKER.") String roleType,
            @Schema(description = "Keyword matched against customer code or customer name.") String keyword,
            @Schema(description = "Maximum rows returned", defaultValue = "50") Long limit) {
    }

    @Schema(description = "Customer role validation command")
    public record RoleValidateCommand(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Required role type: CUSTOMER, SUPPLIER, OVERSEAS_AGENT, CARRIER, WAREHOUSE_PROVIDER, CUSTOMS_BROKER.") String roleType) {
    }

    @Schema(description = "Create customer role command")
    public record RoleCreateCommand(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Role type: CUSTOMER, SUPPLIER, OVERSEAS_AGENT, CARRIER, WAREHOUSE_PROVIDER, CUSTOMS_BROKER.") String roleType,
            @Schema(description = "Role status: ACTIVE or DISABLED.") String roleStatus,
            @Schema(description = "Optional external or finance code for this role.") String roleCode,
            @Schema(description = "Whether finance can offset receivable and payable for this customer.") Boolean offsetEnabled,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Update customer role command")
    public record RoleUpdateCommand(
            @Schema(description = "Customer role id.") Long customerRoleId,
            @Schema(description = "Role status: ACTIVE or DISABLED.") String roleStatus,
            @Schema(description = "Optional external or finance code for this role.") String roleCode,
            @Schema(description = "Whether finance can offset receivable and payable for this customer.") Boolean offsetEnabled,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Customer relation page query")
    public record RelationPageQuery(
            @Schema(description = "Customer id. Relations where this customer is parent or child will be returned.") Long customerId,
            @Schema(description = "Relation type: PARENT_CHILD, BILLING_TITLE, SETTLEMENT_SUBJECT.") String relationType,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Create customer relation command")
    public record RelationCreateCommand(
            @Schema(description = "Parent or primary customer id.") Long parentCustomerId,
            @Schema(description = "Child or related customer id.") Long childCustomerId,
            @Schema(description = "Relation type: PARENT_CHILD, BILLING_TITLE, SETTLEMENT_SUBJECT.") String relationType,
            @Schema(description = "Whether the child customer settles independently.") Boolean settlementIndependent,
            @Schema(description = "Status: 1 enabled, 0 disabled.") Integer status,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Update customer relation command")
    public record RelationUpdateCommand(
            @Schema(description = "Relation id.") Long relationId,
            @Schema(description = "Whether the child customer settles independently.") Boolean settlementIndependent,
            @Schema(description = "Status: 1 enabled, 0 disabled.") Integer status,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Customer attachment page query")
    public record AttachmentPageQuery(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Attachment type: CONTRACT_COPY, LICENSE, TAX_FILE, KYC_FILE, OTHER.") String attachmentType,
            @Schema(description = "Audit status: PENDING, APPROVED, REJECTED.") String auditStatus,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Create customer attachment index command")
    public record AttachmentCreateCommand(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Attachment type: CONTRACT_COPY, LICENSE, TAX_FILE, KYC_FILE, OTHER.") String attachmentType,
            @Schema(description = "File id from system file center.") Long fileId,
            @Schema(description = "File name.") String fileName,
            @Schema(description = "Expire date.") LocalDate expireDate,
            @Schema(description = "Audit status: PENDING, APPROVED, REJECTED.") String auditStatus,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Update customer attachment index command")
    public record AttachmentUpdateCommand(
            @Schema(description = "Attachment id.") Long attachmentId,
            @Schema(description = "Attachment type: CONTRACT_COPY, LICENSE, TAX_FILE, KYC_FILE, OTHER.") String attachmentType,
            @Schema(description = "File id from system file center.") Long fileId,
            @Schema(description = "File name.") String fileName,
            @Schema(description = "Expire date.") LocalDate expireDate,
            @Schema(description = "Audit status: PENDING, APPROVED, REJECTED.") String auditStatus,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Customer KYC page query")
    public record KycPageQuery(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "KYC type: COMPANY_LICENSE, PERSONAL_ID, TAX, COMPLIANCE.") String kycType,
            @Schema(description = "Audit status: PENDING, APPROVED, REJECTED.") String auditStatus,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Create customer KYC command")
    public record KycCreateCommand(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "KYC type: COMPANY_LICENSE, PERSONAL_ID, TAX, COMPLIANCE.") String kycType,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Update customer KYC command")
    public record KycUpdateCommand(
            @Schema(description = "KYC id.") Long kycId,
            @Schema(description = "KYC type: COMPANY_LICENSE, PERSONAL_ID, TAX, COMPLIANCE.") String kycType,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Audit customer KYC command")
    public record KycAuditCommand(
            @Schema(description = "KYC id.") Long kycId,
            @Schema(description = "Audit status: APPROVED or REJECTED.") String auditStatus,
            @Schema(description = "Auditor user id.") Long auditBy,
            @Schema(description = "Reject reason.") String rejectReason) {
    }

    @Schema(description = "Blacklist page query")
    public record BlacklistPageQuery(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Keyword matched against target value or reason.") String keyword,
            @Schema(description = "Target type: CUSTOMER_ID, TAX_NO, LICENSE_NO, PHONE, EMAIL.") String targetType,
            @Schema(description = "Status: ACTIVE or DISABLED.") String status,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Create blacklist command")
    public record BlacklistCreateCommand(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Target type: CUSTOMER_ID, TAX_NO, LICENSE_NO, PHONE, EMAIL.") String targetType,
            @Schema(description = "Target value.") String targetValue,
            @Schema(description = "Risk level: LOW, MEDIUM, HIGH, CRITICAL.") String riskLevel,
            @Schema(description = "Blacklist reason.") String reason,
            @Schema(description = "Source customer id.") Long sourceCustomerId,
            @Schema(description = "Status: ACTIVE or DISABLED.") String status,
            @Schema(description = "Effective time.") LocalDateTime effectiveTime,
            @Schema(description = "Expire time.") LocalDateTime expireTime) {
    }

    @Schema(description = "Update blacklist command")
    public record BlacklistUpdateCommand(
            @Schema(description = "Blacklist id.") Long blacklistId,
            @Schema(description = "Target type: CUSTOMER_ID, TAX_NO, LICENSE_NO, PHONE, EMAIL.") String targetType,
            @Schema(description = "Target value.") String targetValue,
            @Schema(description = "Risk level: LOW, MEDIUM, HIGH, CRITICAL.") String riskLevel,
            @Schema(description = "Blacklist reason.") String reason,
            @Schema(description = "Source customer id.") Long sourceCustomerId,
            @Schema(description = "Status: ACTIVE or DISABLED.") String status,
            @Schema(description = "Effective time.") LocalDateTime effectiveTime,
            @Schema(description = "Expire time.") LocalDateTime expireTime) {
    }

    @Schema(description = "Blacklist check command")
    public record BlacklistCheckCommand(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Tax number.") String taxNo,
            @Schema(description = "Business license number.") String bizLicenseNo,
            @Schema(description = "Phone.") String phone,
            @Schema(description = "Email.") String email) {
    }

    @Schema(description = "Contact page query")
    public record ContactPageQuery(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Keyword matched against contact name, phone, or email.") String keyword,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Create customer contact command")
    public record ContactCreateCommand(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Contact name.") String contactName,
            @Schema(description = "Contact role: BUSINESS, FINANCE, TECH, WAREHOUSE, LEGAL, MANAGER.") String contactRole,
            @Schema(description = "Position.") String position,
            @Schema(description = "Phone.") String phone,
            @Schema(description = "Email.") String email,
            @Schema(description = "Social account.") String socialAccount,
            @Schema(description = "Whether this is the primary contact.") Boolean primary) {
    }

    @Schema(description = "Update customer contact command")
    public record ContactUpdateCommand(
            @Schema(description = "Contact id.") Long contactId,
            @Schema(description = "Contact name.") String contactName,
            @Schema(description = "Contact role: BUSINESS, FINANCE, TECH, WAREHOUSE, LEGAL, MANAGER.") String contactRole,
            @Schema(description = "Position.") String position,
            @Schema(description = "Phone.") String phone,
            @Schema(description = "Email.") String email,
            @Schema(description = "Social account.") String socialAccount,
            @Schema(description = "Whether this is the primary contact.") Boolean primary,
            @Schema(description = "Status: 1 enabled, 0 disabled.") Integer status) {
    }

    @Schema(description = "Address page query")
    public record AddressPageQuery(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Address type.") String addressType,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Create customer address command")
    public record AddressCreateCommand(
            @Schema(description = "Customer id.") Long customerId,
            @Schema(description = "Address type: REGISTERED, SHIP_FROM, RETURN_TO, BILLING, WAREHOUSE_CONTACT, OTHER.") String addressType,
            @Schema(description = "Contact name.") String contactName,
            @Schema(description = "Phone.") String phone,
            @Schema(description = "Country name.") String country,
            @Schema(description = "Country code.") String countryCode,
            @Schema(description = "Province or state.") String province,
            @Schema(description = "City.") String city,
            @Schema(description = "District.") String district,
            @Schema(description = "Street address.") String street,
            @Schema(description = "Zip code.") String zipCode,
            @Schema(description = "Whether this is default for address type.") Boolean defaultAddress) {
    }

    @Schema(description = "Update customer address command")
    public record AddressUpdateCommand(
            @Schema(description = "Address id.") Long addressId,
            @Schema(description = "Address type: REGISTERED, SHIP_FROM, RETURN_TO, BILLING, WAREHOUSE_CONTACT, OTHER.") String addressType,
            @Schema(description = "Contact name.") String contactName,
            @Schema(description = "Phone.") String phone,
            @Schema(description = "Country name.") String country,
            @Schema(description = "Country code.") String countryCode,
            @Schema(description = "Province or state.") String province,
            @Schema(description = "City.") String city,
            @Schema(description = "District.") String district,
            @Schema(description = "Street address.") String street,
            @Schema(description = "Zip code.") String zipCode,
            @Schema(description = "Whether this is default for address type.") Boolean defaultAddress,
            @Schema(description = "Status: 1 enabled, 0 disabled.") Integer status) {
    }
}
