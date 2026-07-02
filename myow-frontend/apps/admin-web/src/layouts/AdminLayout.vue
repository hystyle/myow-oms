<template>
  <div class="admin-shell">
    <aside class="admin-sidebar">
      <div class="admin-logo">
        <span class="admin-logo-mark">M</span>
        <span>MYOW Platform</span>
      </div>
      <nav class="admin-menu">
        <router-link to="/dashboard">工作台</router-link>
        <router-link to="/user">用户中心</router-link>
        <router-link to="/system">系统中心</router-link>
        <router-link v-for="menu in displayMenus" :key="menu.menuId" :to="menu.path || '/dashboard'">
          {{ menu.menuName }}
        </router-link>
      </nav>
    </aside>
    <section class="admin-main">
      <header class="admin-topbar">
        <div>
          <strong>后台管理端</strong>
          <span class="admin-env">本地开发</span>
        </div>
        <span class="admin-user">{{ authStore.user?.nickName || authStore.user?.userName || 'User' }}</span>
        <button type="button" @click="logout">Logout</button>
      </header>
      <main class="admin-content">
        <router-view />
      </main>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const router = useRouter();
const authStore = useAuthStore();

const displayMenus = computed(() =>
  authStore.menuList
    .filter((menu) => menu.menuType !== 'F')
    .filter((menu) => menu.visible !== '1')
    .filter((menu) => menu.status !== '1')
    .filter((menu) => menu.path && menu.path !== '/dashboard')
    .slice()
    .sort((a, b) => (a.sort ?? 0) - (b.sort ?? 0))
);

async function logout() {
  await authStore.logout();
  await router.push({ name: 'AdminLogin' });
}
</script>
