package com.myow.overseas.application.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public final class OverseasBaseModels {

    private OverseasBaseModels() {
    }

    @Schema(description = "Primary id command")
    public record IdCommand(@Schema(description = "Primary id") Long id) {
    }

    @Schema(description = "Status change command")
    public record StatusCommand(
            @Schema(description = "Primary id") Long id,
            @Schema(description = "Status: DRAFT, ENABLED, DISABLED, ARCHIVED") String status) {
    }

    @Schema(description = "Physical warehouse page query")
    public record WarehousePageQuery(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Keyword matched against code, name or external warehouse code.") String keyword,
            @Schema(description = "Country code.") String countryCode,
            @Schema(description = "Warehouse status.") String status,
            @Schema(description = "Warehouse provider customer id.") Long serviceProviderCustomerId,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Create physical warehouse command")
    public record WarehouseCreateCommand(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Warehouse code, unique in tenant.") String warehouseCode,
            @Schema(description = "Warehouse name.") String warehouseName,
            @Schema(description = "Customer id with WAREHOUSE_PROVIDER role.") Long serviceProviderCustomerId,
            @Schema(description = "Cooperation type, such as SELF_OPERATED, OUTSOURCED, THIRD_PARTY.") String cooperationType,
            @Schema(description = "WMS integration system id.") Long wmsSystemId,
            @Schema(description = "External warehouse code in WMS.") String externalWarehouseCode,
            @Schema(description = "Country code.") String countryCode,
            @Schema(description = "State or province.") String state,
            @Schema(description = "City.") String city,
            @Schema(description = "Postal code.") String postalCode,
            @Schema(description = "Address line 1.") String addressLine1,
            @Schema(description = "Address line 2.") String addressLine2,
            @Schema(description = "Contact name.") String contactName,
            @Schema(description = "Contact phone.") String contactPhone,
            @Schema(description = "Contact email.") String contactEmail,
            @Schema(description = "Warehouse local timezone, such as America/Los_Angeles.") String timezone,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Update physical warehouse command")
    public record WarehouseUpdateCommand(
            @Schema(description = "Warehouse id.") Long warehouseId,
            @Schema(description = "Warehouse name.") String warehouseName,
            @Schema(description = "Customer id with WAREHOUSE_PROVIDER role.") Long serviceProviderCustomerId,
            @Schema(description = "Cooperation type.") String cooperationType,
            @Schema(description = "WMS integration system id.") Long wmsSystemId,
            @Schema(description = "External warehouse code in WMS.") String externalWarehouseCode,
            @Schema(description = "Country code.") String countryCode,
            @Schema(description = "State or province.") String state,
            @Schema(description = "City.") String city,
            @Schema(description = "Postal code.") String postalCode,
            @Schema(description = "Address line 1.") String addressLine1,
            @Schema(description = "Address line 2.") String addressLine2,
            @Schema(description = "Contact name.") String contactName,
            @Schema(description = "Contact phone.") String contactPhone,
            @Schema(description = "Contact email.") String contactEmail,
            @Schema(description = "Warehouse local timezone.") String timezone,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Logistics product page query")
    public record LogisticsProductPageQuery(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Keyword matched against code or name.") String keyword,
            @Schema(description = "Carrier customer id.") Long carrierCustomerId,
            @Schema(description = "Product type.") String productType,
            @Schema(description = "Product status.") String status,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Create logistics product command")
    public record LogisticsProductCreateCommand(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Product code, unique in tenant.") String productCode,
            @Schema(description = "Product name.") String productName,
            @Schema(description = "Customer id with CARRIER role.") Long carrierCustomerId,
            @Schema(description = "Product type: CUSTOMER_ACCOUNT, SELF_LABEL, WAREHOUSE_LABEL, LTL.") String productType,
            @Schema(description = "Default channel id.") Long defaultChannelId,
            @Schema(description = "Default decision strategy: LOWEST_COST, FASTEST, PRIORITY.") String defaultDecisionStrategy,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Update logistics product command")
    public record LogisticsProductUpdateCommand(
            @Schema(description = "Product id.") Long productId,
            @Schema(description = "Product name.") String productName,
            @Schema(description = "Customer id with CARRIER role.") Long carrierCustomerId,
            @Schema(description = "Product type.") String productType,
            @Schema(description = "Default channel id.") Long defaultChannelId,
            @Schema(description = "Default decision strategy.") String defaultDecisionStrategy,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Logistics channel page query")
    public record LogisticsChannelPageQuery(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Keyword matched against code or name.") String keyword,
            @Schema(description = "Carrier customer id.") Long carrierCustomerId,
            @Schema(description = "Label source.") String labelSource,
            @Schema(description = "Channel status.") String status,
            @Schema(description = "Page number", defaultValue = "1") Long pageNum,
            @Schema(description = "Page size", defaultValue = "20") Long pageSize) {
    }

    @Schema(description = "Create logistics channel command")
    public record LogisticsChannelCreateCommand(
            @Schema(description = "Tenant id. Defaults to 1 when omitted.") Long tenantId,
            @Schema(description = "Channel code, unique in tenant.") String channelCode,
            @Schema(description = "Channel name.") String channelName,
            @Schema(description = "Customer id with CARRIER role.") Long carrierCustomerId,
            @Schema(description = "Channel type.") String channelType,
            @Schema(description = "Label source: IMPORTED_LABEL, WAREHOUSE_LABEL, TMS.") String labelSource,
            @Schema(description = "TMS integration system id.") Long tmsSystemId,
            @Schema(description = "Label format, such as PDF or ZPL.") String labelFormat,
            @Schema(description = "Remark.") String remark) {
    }

    @Schema(description = "Update logistics channel command")
    public record LogisticsChannelUpdateCommand(
            @Schema(description = "Channel id.") Long channelId,
            @Schema(description = "Channel name.") String channelName,
            @Schema(description = "Customer id with CARRIER role.") Long carrierCustomerId,
            @Schema(description = "Channel type.") String channelType,
            @Schema(description = "Label source.") String labelSource,
            @Schema(description = "TMS integration system id.") Long tmsSystemId,
            @Schema(description = "Label format.") String labelFormat,
            @Schema(description = "Remark.") String remark) {
    }
}
