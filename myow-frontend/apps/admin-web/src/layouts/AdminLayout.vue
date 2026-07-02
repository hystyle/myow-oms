<template>
  <div class="admin-shell">
    <aside class="admin-sidebar">
      <div class="admin-logo">
        <span class="admin-logo-mark">M</span>
        <span>MYOW Platform</span>
      </div>
      <nav class="admin-menu">
        <span class="admin-menu-title">启动模块</span>
        <router-link to="/dashboard">工作台</router-link>
        <router-link to="/user">用户账号</router-link>
        <router-link to="/system">系统概览</router-link>
        <span class="admin-menu-title">用户权限</span>
        <router-link to="/user/depts">部门组织</router-link>
        <router-link to="/user/roles">角色权限</router-link>
        <router-link to="/user/menus">菜单权限</router-link>
        <router-link to="/user/dicts">字典管理</router-link>
        <router-link to="/user/login-logs">登录日志</router-link>
        <span class="admin-menu-title">系统运维</span>
        <router-link to="/system/jobs">定时任务</router-link>
        <router-link to="/system/notices">通知公告</router-link>
        <router-link to="/system/site-configs">站点配置</router-link>
        <router-link to="/system/files">文件管理</router-link>
        <router-link to="/system/online-users">在线用户</router-link>
        <router-link to="/system/sensitive-words">敏感词</router-link>
        <router-link to="/system/message-templates">消息模板</router-link>
        <router-link to="/system/export-tasks">导出任务</router-link>
        <router-link to="/system/monitor">系统监控</router-link>
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
