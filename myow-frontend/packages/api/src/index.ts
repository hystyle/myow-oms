export interface LoginPayload { loginName: string; password: string; loginClient: string; captchaUuid?: string; captchaCode?: string; }
export interface MenuItem { menuId: number; menuName: string; parentId?: number; sort?: number; path?: string; component?: string; queryParam?: string; isFrame?: string; isCache?: string; menuType?: string; visible?: string; status?: string; apiPerms?: string; icon?: string; remark?: string; }
export interface UserProfile { userId: number; tenantId?: string; deptId?: number; deptName?: string; positionId?: number; positionName?: string; roleIdList?: number[]; roleNameList?: string[]; userName?: string; nickName?: string; userType?: string; email?: string; phone?: string; gender?: string; avatar?: number; status?: string; createTime?: string; remark?: string; adminFlag?: boolean; failedLoginCount?: number; lockedUntil?: string; passwordUpdateTime?: string; passwordExpireTime?: string; mustChangePassword?: boolean; forceChangePassword?: boolean; lastLoginTime?: string; lastLoginIp?: string; }
export interface LoginResult { token: string; userId: number; tenantId?: string; userCode?: string; loginName?: string; nickName?: string; adminFlag?: boolean; mustChangePassword?: boolean; forceChangePassword?: boolean; menuList?: MenuItem[]; }
export interface BootstrapPayload { user: UserProfile; menuList: MenuItem[]; permissionList: string[]; roleIdList: number[]; roleNameList: string[]; adminFlag?: boolean; forceChangePassword?: boolean; mustChangePassword?: boolean; tenantModeEnabled?: boolean; tenantEnabled?: boolean; dataScope?: string; systemConfig?: Record<string, unknown>; }
export interface PageQuery { keyword?: string; status?: string | number; pageNum?: number; pageSize?: number; deptId?: number; }
export interface PageResult<T> { pageNum?: number; pageSize?: number; total?: number; pages?: number; list?: T[]; emptyFlag?: boolean; }
export interface SystemRecord { id: number; type: string; code?: string; name?: string; status?: number; attributes?: Record<string, unknown>; createTime?: string; updateTime?: string; }

// 用户管理请求载荷
export interface UserCreatePayload {
  loginName: string;
  nickName: string;
  deptId?: number | string;
  positionId?: number | string;
  roleIdList?: number[];
  gender?: string;
  phone?: string;
  email?: string;
  remark?: string;
}

export interface UserUpdatePayload extends UserCreatePayload {
  userId: number | string;
}

export interface UserStatusPayload {
  userId: number;
  status: boolean;
}

// 部门管理请求载荷
export interface DeptCreatePayload {
  parentId?: number | string;
  deptName: string;
  sort?: number;
  managerId?: number | string;
}

export interface DeptUpdatePayload extends DeptCreatePayload {
  deptId: number | string;
}
