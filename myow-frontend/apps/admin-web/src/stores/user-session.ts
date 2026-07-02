import { computed, ref } from 'vue';
import { defineStore } from 'pinia';
import { bootstrap, login, logout } from '@/services/profile-service';
import type { BootstrapPayload, LoginPayload } from '@myow/api';
import { getToken, removeToken, setToken } from '@myow/shared';

export const useAuthStore = defineStore('admin-auth', () => {
  const token = ref(getToken());
  const loading = ref(false);
  const bootstrapped = ref(false);
  const bootstrapPayload = ref<BootstrapPayload | null>(null);

  const user = computed(() => bootstrapPayload.value?.user);
  const menuList = computed(() => bootstrapPayload.value?.menuList ?? []);
  const permissionList = computed(() => bootstrapPayload.value?.permissionList ?? []);
  const forceChangePassword = computed(() => Boolean(bootstrapPayload.value?.forceChangePassword));

  async function doLogin(payload: LoginPayload) {
    loading.value = true;
    try {
      const result = await login(payload);
      token.value = result.token;
      setToken(result.token);
    } finally {
      loading.value = false;
    }
  }

  async function doBootstrap() {
    bootstrapPayload.value = await bootstrap();
    bootstrapped.value = true;
  }

  async function doLogout() {
    try {
      await logout();
    } finally {
      token.value = '';
      bootstrapped.value = false;
      bootstrapPayload.value = null;
      removeToken();
    }
  }

  return {
    token,
    loading,
    bootstrapped,
    bootstrapPayload,
    user,
    menuList,
    permissionList,
    forceChangePassword,
    login: doLogin,
    bootstrap: doBootstrap,
    logout: doLogout
  };
});
