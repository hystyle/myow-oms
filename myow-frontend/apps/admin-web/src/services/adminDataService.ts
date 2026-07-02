import { request } from '@myow/shared';
import type { PageResult } from '@myow/api';

export type AdminRecord = Record<string, unknown>;

export function pageAdminRecords(endpoint: string, query: Record<string, unknown>) {
  return request.post<PageResult<AdminRecord>>(endpoint, query);
}

export function postAdminData<T = unknown>(endpoint: string, data?: Record<string, unknown>) {
  return request.post<T>(endpoint, data ?? {});
}

export function detailAdminRecord(endpoint: string, idKey: string, id: unknown) {
  return postAdminData<AdminRecord>(endpoint, { [idKey]: id, id });
}

export function createAdminRecord(endpoint: string, data: AdminRecord) {
  return postAdminData<unknown>(endpoint, data);
}

export function updateAdminRecord(endpoint: string, data: AdminRecord) {
  return postAdminData<unknown>(endpoint, data);
}

export function deleteAdminRecord(endpoint: string, idKey: string, id: unknown) {
  return postAdminData<boolean>(endpoint, { [idKey]: id, id });
}
