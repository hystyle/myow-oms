import type { ApiId } from './common';

export interface UserProfile {
  userId: ApiId;
  tenantId?: ApiId;
  deptId?: ApiId;
  deptName?: string;
  positionId?: ApiId;
  positionName?: string;
  roleIdList?: ApiId[];
  roleNameList?: string[];
  userName?: string;
  nickName?: string;
  userType?: string;
  email?: string;
  phone?: string;
  gender?: string;
  avatar?: ApiId;
  status?: string;
  createTime?: string;
  remark?: string;
  adminFlag?: boolean;
  failedLoginCount?: number;
  lockedUntil?: string;
  passwordUpdateTime?: string;
  passwordExpireTime?: string;
  mustChangePassword?: boolean;
  forceChangePassword?: boolean;
  lastLoginTime?: string;
  lastLoginIp?: string;
}

export interface UserCreatePayload {
  loginName: string;
  nickName: string;
  deptId?: ApiId;
  positionId?: ApiId;
  roleIdList?: ApiId[];
  gender?: string;
  phone?: string;
  email?: string;
  remark?: string;
}

export interface UserUpdatePayload extends UserCreatePayload {
  userId: ApiId;
}

export interface UserStatusPayload {
  userId: ApiId;
  status: boolean;
}

export interface DeptTreeNode {
  deptId?: ApiId;
  parentId?: ApiId;
  deptName?: string;
  name?: string;
  status?: string | boolean;
  children?: DeptTreeNode[];
}

export interface DeptCreatePayload {
  parentId?: ApiId;
  deptName: string;
  sort?: number;
  managerId?: ApiId;
}

export interface DeptUpdatePayload extends DeptCreatePayload {
  deptId: ApiId;
}
