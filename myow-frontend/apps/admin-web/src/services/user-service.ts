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
const DICT_BASE = '/myow/system/dict';
const DICT_DATA_BASE = '/myow/system/dict-data';

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

export interface DictRecord {
  dictId: ApiId;
  dictCode?: string;
  dictName?: string;
  remark?: string;
  createTime?: string;
  updateTime?: string;
}

export interface DictDataRecord {
  dictDataId: ApiId;
  dictId: ApiId;
  dataValue?: string;
  dataLabel?: string;
  remark?: string;
  sort?: number;
  disabledFlag?: boolean;
  createTime?: string;
  updateTime?: string;
}

export function pageDicts(query: PageQuery & { dictCode?: string; dictName?: string }) {
  return request.post<PageResult<DictRecord>>(`${DICT_BASE}/page`, query);
}

export function createDict(data: Record<string, unknown>) {
  return request.post<ApiId>(`${DICT_BASE}/create`, data);
}

export function updateDict(data: Record<string, unknown>) {
  return request.post<boolean>(`${DICT_BASE}/update`, data);
}

export function deleteDict(dictId: ApiId) {
  return request.post<boolean>(`${DICT_BASE}/delete`, { id: dictId });
}

export function pageDictData(query: PageQuery & { dictId?: ApiId; dataLabel?: string; dataValue?: string; disabledFlag?: boolean }) {
  return request.post<PageResult<DictDataRecord>>(`${DICT_DATA_BASE}/page`, query);
}

export function createDictData(data: Record<string, unknown>) {
  return request.post<ApiId>(`${DICT_DATA_BASE}/create`, data);
}

export function updateDictData(data: Record<string, unknown>) {
  return request.post<boolean>(`${DICT_DATA_BASE}/update`, data);
}

export function deleteDictData(dictDataId: ApiId) {
  return request.post<boolean>(`${DICT_DATA_BASE}/delete`, { id: dictDataId });
}
