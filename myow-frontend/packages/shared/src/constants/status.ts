// 通用状态字典

// 通用正常/停用（字符串值，兼容旧版后端）
export const NORMAL_DISABLE_STATUS = {
  NORMAL: { value: '0', label: '正常' },
  DISABLED: { value: '1', label: '停用' }
} as const;

export const NORMAL_DISABLE_OPTIONS = [
  { label: '正常', value: '0' },
  { label: '停用', value: '1' }
] as const;

// 系统启用/停用（数值型，兼容新版 REST API）
export const SYSTEM_STATUS = {
  ENABLED: { value: 1, label: '启用' },
  DISABLED: { value: 0, label: '停用' }
} as const;

export const SYSTEM_STATUS_OPTIONS = [
  { label: '启用', value: 1 },
  { label: '停用', value: 0 }
] as const;
