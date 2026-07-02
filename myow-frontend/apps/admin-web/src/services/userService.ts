import { request } from '@myow/shared';
import type { PageQuery, PageResult, UserProfile } from '@myow/api';

const USER_BASE = '/myow/system/user';

export function pageUsers(query: PageQuery) {
  return request.post<PageResult<UserProfile>>(`${USER_BASE}/page`, query);
}

export function getUser(userId: number) {
  return request.post<UserProfile>(`${USER_BASE}/get`, { userId });
}

export function createUser(data: Record<string, unknown>) {
  return request.post<string>(`${USER_BASE}/create`, data);
}

export function updateUser(data: Record<string, unknown>) {
  return request.post<boolean>(`${USER_BASE}/update`, data);
}

export function deleteUser(userId: number) {
  return request.post<boolean>(`${USER_BASE}/delete`, { userId });
}

export function updateUserStatus(userId: number, status: boolean) {
  return request.post<boolean>(`${USER_BASE}/status`, { userId, status });
}

export function resetUserPassword(userId: number) {
  return request.post<string>(`${USER_BASE}/reset-password`, { userId });
}

export function unlockUser(userId: number) {
  return request.post<boolean>(`${USER_BASE}/unlock`, { userId });
}

export function forceUserChangePassword(userId: number) {
  return request.post<boolean>(`${USER_BASE}/force-change-password`, { userId });
}
