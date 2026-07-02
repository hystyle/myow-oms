export function hasPermission(permissionList: string[] | undefined, permission: string) {
  return Boolean(permissionList?.includes(permission));
}
