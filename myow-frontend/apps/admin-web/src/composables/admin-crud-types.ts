import type { AdminRecord } from '@/services/admin-data-service';

// 列表表格的列定义
export interface TableColumn {
  key: string;
  label: string;
  code?: boolean;
  aliases?: string[];
}

// 表单下拉选项
export interface FormOption {
  label: string;
  value: string | number;
}

// 表单字段定义
export interface FormField {
  key: string;
  label: string;
  type?: 'text' | 'number' | 'textarea' | 'select' | 'datetime';
  readonly?: boolean;
  hideOnCreate?: boolean;
  json?: boolean;
  aliases?: string[];
  options?: readonly FormOption[];
}

// 行操作按钮定义
export interface RowAction {
  label: string;
  endpoint: string;
  permission?: string;
  confirm?: string;
  success?: string;
  idKey?: string;
  payloadKey?: string;
  resultMode?: 'toast' | 'drawer' | 'open-url' | 'download';
  urlKey?: string;
  variablesPrompt?: boolean;
  refresh?: boolean;
}

// 通用记录类型别名
export type { AdminRecord };

// 列与字段构造辅助函数
export function textColumn(key: string, label: string, code = false, aliases: string[] = []) {
  return { key, label, code, aliases } satisfies TableColumn;
}

export function textField(
  key: string,
  label: string,
  type: 'text' | 'number' | 'textarea' | 'select' | 'datetime' = 'text',
  options?: readonly FormOption[],
  extra: Record<string, unknown> = {}
) {
  return { key, label, type, options, ...extra } satisfies FormField;
}

export function aliasField(
  key: string,
  label: string,
  aliases: string[],
  type: 'text' | 'number' | 'textarea' | 'select' | 'datetime' = 'text',
  options?: readonly FormOption[],
  extra: Record<string, unknown> = {}
) {
  return textField(key, label, type, options, { ...extra, aliases });
}

export function idField(key: string, label = 'ID') {
  return textField(key, label, 'number', undefined, { hideOnCreate: true, readonly: true });
}
