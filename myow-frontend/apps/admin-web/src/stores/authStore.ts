import { defineStore } from 'pinia';
import { bootstrap, login, logout } from '@/services/profileService';
import type { BootstrapPayload, LoginPayload } from '@myow/api';
import { getToken, removeToken, setToken } from '@myow/shared';

export const useAuthStore = defineStore('admin-auth', {
  state: () => ({
    token: getToken(),
    loading: false,
    bootstrapped: false,
    bootstrapPayload: null as BootstrapPayload | null
  }),
  getters: {
    user: (state) => state.bootstrapPayload?.user,
    menuList: (state) => state.bootstrapPayload?.menuList ?? [],
    permissionList: (state) => state.bootstrapPayload?.permissionList ?? [],
    forceChangePassword: (state) => Boolean(state.bootstrapPayload?.forceChangePassword)
  },
  actions: {
    async login(payload: LoginPayload) {
      this.loading = true;
      try {
        const result = await login(payload);
        this.token = result.token;
        setToken(result.token);
      } finally {
        this.loading = false;
      }
    },
    async bootstrap() {
      this.bootstrapPayload = await bootstrap();
      this.bootstrapped = true;
    },
    async logout() {
      try {
        await logout();
      } finally {
        this.token = '';
        this.bootstrapped = false;
        this.bootstrapPayload = null;
        removeToken();
      }
    }
  }
});
