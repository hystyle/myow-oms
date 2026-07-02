import type { ApiId } from './common';

export interface MenuItem {
  menuId: ApiId;
  menuName: string;
  parentId?: ApiId;
  sort?: number;
  path?: string;
  component?: string;
  queryParam?: string;
  isFrame?: string;
  isCache?: string;
  menuType?: string;
  visible?: string;
  status?: string;
  apiPerms?: string;
  icon?: string;
  remark?: string;
}
