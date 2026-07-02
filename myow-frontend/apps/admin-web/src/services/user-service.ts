import { request } from '@myow/shared';
import type {
  ApiId,
  DeptTreeNode,
  PageQuery,
  PageResult,
  UserCreatePayload,
  UserProfile,
  UserStatusPayload,
  UserUpdatePayload
} from '@myow/api';

const USER_BASE = '/myow/system/user';

export function pageUsers(query: PageQuery) {
  return request.post<PageResult<UserProfile>>(`${USER_BASE}/page`, query);
}

export function getUser(userId: ApiId) {
  return request.post<UserProfile>(`${USER_BASE}/get`, { userId });
}

export function createUser(data: UserCreatePayload) {
  return request.post<string>(`${USER_BASE}/create`, data);
}

export function updateUser(data: UserUpdatePayload) {
  return request.post<boolean>(`${USER_BASE}/update`, data);
}

export function deleteUser(userId: ApiId) {
  return request.post<boolean>(`${USER_BASE}/delete`, { userId });
}

export function updateUserStatus(userId: ApiId, status: boolean) {
  return request.post<boolean>(`${USER_BASE}/status`, { userId, status } satisfies UserStatusPayload);
}

export function resetUserPassword(userId: ApiId) {
  return request.post<string>(`${USER_BASE}/reset-password`, { userId });
}

export function unlockUser(userId: ApiId) {
  return request.post<boolean>(`${USER_BASE}/unlock`, { userId });
}

export function forceUserChangePassword(userId: ApiId) {
  return request.post<boolean>(`${USER_BASE}/force-change-password`, { userId });
}

export function listDeptTree() {
  return request.post<DeptTreeNode[]>('/myow/system/dept/tree');
}
