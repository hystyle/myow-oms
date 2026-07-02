import type { ApiId } from './common';

export interface SystemRecord {
  id: ApiId;
  type: string;
  code?: string;
  name?: string;
  status?: number | string | boolean;
  attributes?: Record<string, unknown>;
  createTime?: string;
  updateTime?: string;
}

export interface SystemPageQuery {
  keyword?: string;
  status?: number | string;
  pageNum?: number;
  pageSize?: number;
}

export interface SystemIdPayload {
  id: ApiId;
}
