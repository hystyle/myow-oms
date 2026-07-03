package com.myow.customer.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

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
