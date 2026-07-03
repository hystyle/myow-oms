import { request } from '@myow/shared';
import type {
  ApiId,
  LogisticsChannel,
  LogisticsChannelCreatePayload,
  LogisticsChannelPageQuery,
  LogisticsChannelUpdatePayload,
  LogisticsProduct,
  LogisticsProductCreatePayload,
  LogisticsProductPageQuery,
  LogisticsProductUpdatePayload,
  OverseasBaseStatus,
  PageResult,
  PhysicalWarehouse,
  PhysicalWarehouseCreatePayload,
  PhysicalWarehousePageQuery,
  PhysicalWarehouseUpdatePayload
} from '@myow/api';

const WAREHOUSE_BASE = '/myow/api/v1/overseas/base/physical-warehouse';
const PRODUCT_BASE = '/myow/api/v1/overseas/base/logistics-product';
const CHANNEL_BASE = '/myow/api/v1/overseas/base/logistics-channel';

export function pagePhysicalWarehouses(query: PhysicalWarehousePageQuery) {
  return request.post<PageResult<PhysicalWarehouse>>(`${WAREHOUSE_BASE}/page`, query);
}

export function getPhysicalWarehouse(warehouseId: ApiId) {
  return request.post<PhysicalWarehouse>(`${WAREHOUSE_BASE}/detail`, { id: warehouseId });
}

export function createPhysicalWarehouse(data: PhysicalWarehouseCreatePayload) {
  return request.post<PhysicalWarehouse>(`${WAREHOUSE_BASE}/create`, data);
}

export function updatePhysicalWarehouse(data: PhysicalWarehouseUpdatePayload) {
  return request.post<PhysicalWarehouse>(`${WAREHOUSE_BASE}/update`, data);
}

export function changePhysicalWarehouseStatus(warehouseId: ApiId, status: OverseasBaseStatus) {
  return request.post<boolean>(`${WAREHOUSE_BASE}/change-status`, { id: warehouseId, status });
}

export function deletePhysicalWarehouse(warehouseId: ApiId) {
  return request.post<boolean>(`${WAREHOUSE_BASE}/delete`, { id: warehouseId });
}

export function pageLogisticsProducts(query: LogisticsProductPageQuery) {
  return request.post<PageResult<LogisticsProduct>>(`${PRODUCT_BASE}/page`, query);
}

export function getLogisticsProduct(productId: ApiId) {
  return request.post<LogisticsProduct>(`${PRODUCT_BASE}/detail`, { id: productId });
}

export function createLogisticsProduct(data: LogisticsProductCreatePayload) {
  return request.post<LogisticsProduct>(`${PRODUCT_BASE}/create`, data);
}

export function updateLogisticsProduct(data: LogisticsProductUpdatePayload) {
  return request.post<LogisticsProduct>(`${PRODUCT_BASE}/update`, data);
}

export function changeLogisticsProductStatus(productId: ApiId, status: OverseasBaseStatus) {
  return request.post<boolean>(`${PRODUCT_BASE}/change-status`, { id: productId, status });
}

export function deleteLogisticsProduct(productId: ApiId) {
  return request.post<boolean>(`${PRODUCT_BASE}/delete`, { id: productId });
}

export function pageLogisticsChannels(query: LogisticsChannelPageQuery) {
  return request.post<PageResult<LogisticsChannel>>(`${CHANNEL_BASE}/page`, query);
}

export function getLogisticsChannel(channelId: ApiId) {
  return request.post<LogisticsChannel>(`${CHANNEL_BASE}/detail`, { id: channelId });
}

export function createLogisticsChannel(data: LogisticsChannelCreatePayload) {
  return request.post<LogisticsChannel>(`${CHANNEL_BASE}/create`, data);
}

export function updateLogisticsChannel(data: LogisticsChannelUpdatePayload) {
  return request.post<LogisticsChannel>(`${CHANNEL_BASE}/update`, data);
}

export function changeLogisticsChannelStatus(channelId: ApiId, status: OverseasBaseStatus) {
  return request.post<boolean>(`${CHANNEL_BASE}/change-status`, { id: channelId, status });
}

export function deleteLogisticsChannel(channelId: ApiId) {
  return request.post<boolean>(`${CHANNEL_BASE}/delete`, { id: channelId });
}
