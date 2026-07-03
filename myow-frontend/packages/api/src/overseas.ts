import type { ApiId, PageQuery } from './common';

export type OverseasBaseStatus = 'DRAFT' | 'ENABLED' | 'DISABLED' | 'ARCHIVED';

export interface PhysicalWarehouse {
  warehouseId: ApiId;
  tenantId?: ApiId;
  warehouseCode: string;
  warehouseName: string;
  serviceProviderCustomerId: ApiId;
  cooperationType?: string;
  wmsSystemId?: ApiId;
  externalWarehouseCode?: string;
  countryCode: string;
  state?: string;
  city?: string;
  postalCode?: string;
  addressLine1?: string;
  addressLine2?: string;
  contactName?: string;
  contactPhone?: string;
  contactEmail?: string;
  timezone: string;
  status?: OverseasBaseStatus;
  remark?: string;
  createTime?: string;
  updateTime?: string;
}

export interface PhysicalWarehousePageQuery extends PageQuery {
  tenantId?: ApiId;
  countryCode?: string;
  status?: OverseasBaseStatus | '';
  serviceProviderCustomerId?: ApiId;
}

export interface PhysicalWarehouseCreatePayload {
  tenantId?: ApiId;
  warehouseCode: string;
  warehouseName: string;
  serviceProviderCustomerId: ApiId;
  cooperationType?: string;
  wmsSystemId?: ApiId;
  externalWarehouseCode?: string;
  countryCode: string;
  state?: string;
  city?: string;
  postalCode?: string;
  addressLine1?: string;
  addressLine2?: string;
  contactName?: string;
  contactPhone?: string;
  contactEmail?: string;
  timezone: string;
  remark?: string;
}

export interface PhysicalWarehouseUpdatePayload extends Omit<PhysicalWarehouseCreatePayload, 'tenantId' | 'warehouseCode'> {
  warehouseId: ApiId;
}

export interface LogisticsProduct {
  productId: ApiId;
  tenantId?: ApiId;
  productCode: string;
  productName: string;
  carrierCustomerId: ApiId;
  productType: string;
  defaultChannelId?: ApiId;
  defaultDecisionStrategy?: string;
  status?: OverseasBaseStatus;
  remark?: string;
  createTime?: string;
  updateTime?: string;
}

export interface LogisticsProductPageQuery extends PageQuery {
  tenantId?: ApiId;
  carrierCustomerId?: ApiId;
  productType?: string;
  status?: OverseasBaseStatus | '';
}

export interface LogisticsProductCreatePayload {
  tenantId?: ApiId;
  productCode: string;
  productName: string;
  carrierCustomerId: ApiId;
  productType: string;
  defaultChannelId?: ApiId;
  defaultDecisionStrategy?: string;
  remark?: string;
}

export interface LogisticsProductUpdatePayload extends Omit<LogisticsProductCreatePayload, 'tenantId' | 'productCode'> {
  productId: ApiId;
}

export interface LogisticsChannel {
  channelId: ApiId;
  tenantId?: ApiId;
  channelCode: string;
  channelName: string;
  carrierCustomerId: ApiId;
  channelType?: string;
  labelSource: string;
  tmsSystemId?: ApiId;
  labelFormat?: string;
  status?: OverseasBaseStatus;
  remark?: string;
  createTime?: string;
  updateTime?: string;
}

export interface LogisticsChannelPageQuery extends PageQuery {
  tenantId?: ApiId;
  carrierCustomerId?: ApiId;
  labelSource?: string;
  status?: OverseasBaseStatus | '';
}

export interface LogisticsChannelCreatePayload {
  tenantId?: ApiId;
  channelCode: string;
  channelName: string;
  carrierCustomerId: ApiId;
  channelType?: string;
  labelSource: string;
  tmsSystemId?: ApiId;
  labelFormat?: string;
  remark?: string;
}

export interface LogisticsChannelUpdatePayload extends Omit<LogisticsChannelCreatePayload, 'tenantId' | 'channelCode'> {
  channelId: ApiId;
}
