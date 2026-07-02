export type ApiId = string;

export interface PageQuery {
  keyword?: string;
  status?: string | number;
  pageNum?: number;
  pageSize?: number;
  deptId?: ApiId;
}

export interface PageResult<T> {
  pageNum?: number;
  pageSize?: number;
  total?: number | string;
  pages?: number | string;
  list?: T[];
  emptyFlag?: boolean;
}
