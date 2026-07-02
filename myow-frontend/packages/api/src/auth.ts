import type { ApiId } from './common';
import type { MenuItem } from './menu';
import type { UserProfile } from './user';

export interface LoginPayload {
  loginName: string;
  password: string;
  loginClient: string;
  captchaUuid?: string;
  captchaCode?: string;
}

export interface LoginResult {
  token: string;
  userId: ApiId;
  tenantId?: ApiId;
  userCode?: string;
  loginName?: string;
  nickName?: string;
  adminFlag?: boolean;
  mustChangePassword?: boolean;
  forceChangePassword?: boolean;
  menuList?: MenuItem[];
}

export interface BootstrapPayload {
  user: UserProfile;
  menuList: MenuItem[];
  permissionList: string[];
  roleIdList: ApiId[];
  roleNameList: string[];
  adminFlag?: boolean;
  forceChangePassword?: boolean;
  mustChangePassword?: boolean;
  tenantModeEnabled?: boolean;
  tenantEnabled?: boolean;
  dataScope?: string;
  systemConfig?: Record<string, unknown>;
}
