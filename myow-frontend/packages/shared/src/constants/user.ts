// 用户相关字典

export const USER_GENDER = {
  UNKNOWN: { value: '0', label: '未知' },
  MALE: { value: '1', label: '男' },
  FEMALE: { value: '2', label: '女' }
} as const;

export const USER_GENDER_OPTIONS = [
  { label: '未知', value: '0' },
  { label: '男', value: '1' },
  { label: '女', value: '2' }
] as const;
