import { computed } from 'vue';
import { useAuthStore } from '@/stores/user-session';

export function usePermission() {
  const authStore = useAuthStore();
  const permissionSet = computed(() => new Set(authStore.permissionList));

  function hasPermission(permission?: string) {
    if (!permission) {
      return true;
    }
    if (authStore.bootstrapPayload?.adminFlag || authStore.user?.adminFlag) {
      return true;
    }
    return permissionSet.value.has(permission);
  }

  return { hasPermission };
}
