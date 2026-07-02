<template>
  <div class="admin-shell">
    <aside class="admin-sidebar">
      <div class="admin-logo">
        <span class="admin-logo-mark">M</span>
        <span>MYOW Platform</span>
      </div>
      <nav class="admin-menu">
        <template v-for="group in menuGroups" :key="group.title">
          <span class="admin-menu-title">{{ group.title }}</span>
          <router-link v-for="menu in group.items" :key="menu.menuId" :to="normalizePath(menu.path)">
            {{ menuDisplayName(menu.menuName, menu.menuId) }}
          </router-link>
        </template>
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
import type { MenuItem } from '@myow/api';
import { useAuthStore } from '@/stores/user-session';

const router = useRouter();
const authStore = useAuthStore();

interface MenuGroup {
  title: string;
  items: MenuItem[];
}

const fallbackMenuGroups: MenuGroup[] = [
  {
    title: '启动模块',
    items: [
      menu(1, '工作台', '/dashboard'),
      menu(2, '用户账号', '/user'),
      menu(3, '系统概览', '/system')
    ]
  },
  {
    title: '用户权限',
    items: [
      menu(10, '部门组织', '/user/depts'),
      menu(11, '角色权限', '/user/roles'),
      menu(12, '菜单权限', '/user/menus'),
      menu(13, '字典管理', '/user/dicts'),
      menu(14, '登录日志', '/user/login-logs')
    ]
  },
  {
    title: '系统运维',
    items: [
      menu(20, '定时任务', '/system/jobs'),
      menu(21, '通知公告', '/system/notices'),
      menu(22, '站点配置', '/system/site-configs'),
      menu(23, '文件管理', '/system/files'),
      menu(24, '在线用户', '/system/online-users'),
      menu(25, '敏感词', '/system/sensitive-words'),
      menu(26, '消息模板', '/system/message-templates'),
      menu(27, '导出任务', '/system/export-tasks'),
      menu(28, '系统监控', '/system/monitor')
    ]
  }
];

const menuGroups = computed<MenuGroup[]>(() => {
  const backendMenus = authStore.menuList
    .filter((item) => item.menuType !== 'F')
    .filter((item) => item.visible !== '1')
    .filter((item) => item.status !== '1')
    .filter((item) => item.path)
    .filter((item) => isSupportedRoute(item.path))
    .slice()
    .sort((a, b) => (a.sort ?? 0) - (b.sort ?? 0));
  if (backendMenus.length === 0) {
    return fallbackMenuGroups;
  }
  return groupBackendMenus(backendMenus);
});

function menu(menuId: number, menuName: string, path: string): MenuItem {
  return { menuId, menuName, path };
}

const menuNameMap: Record<string, string> = {
  System: '系统中心',
  User: '用户账号',
  Role: '角色权限',
  Menu: '菜单权限',
  Dept: '部门组织',
  Dict: '字典管理',
  'Login Log': '登录日志',
  'System Support': '系统运维',
  'Scheduled Job': '定时任务',
  Notice: '通知公告',
  File: '文件管理',
  'Site Config': '站点配置',
  'Sensitive Word': '敏感词',
  'Message Template': '消息模板',
  'Export Task': '导出任务',
  Monitor: '系统监控',
  'Online User': '在线用户'
};

const menuIdNameMap: Record<number, string> = {
  1000: '系统中心',
  1100: '用户账号',
  1200: '角色权限',
  1300: '菜单权限',
  1400: '部门组织',
  1700: '字典管理',
  1820: '登录日志',
  1900: '系统运维',
  1910: '定时任务',
  1920: '通知公告',
  1930: '文件管理',
  1940: '站点配置',
  1950: '敏感词',
  1960: '消息模板',
  1970: '导出任务',
  1980: '系统监控',
  1990: '在线用户'
};

function menuDisplayName(name?: string, menuId?: number) {
  if (menuId && menuIdNameMap[menuId]) return menuIdNameMap[menuId];
  if (!name) return '';
  return menuNameMap[name] ?? name;
}

function groupBackendMenus(menus: MenuItem[]) {
  const parentById = new Map(menus.map((item) => [item.menuId, item]));
  const groups = new Map<string, MenuGroup>();
  menus.forEach((item) => {
    if (!item.path || item.path === '/dashboard') {
      return;
    }
    const parent = item.parentId == null ? undefined : parentById.get(item.parentId);
    const title = parent && parent.menuId !== item.menuId ? menuDisplayName(parent.menuName, parent.menuId) : inferGroupTitle(item.path);
    const key = String(parent?.menuId ?? title);
    if (!groups.has(key)) {
      groups.set(key, { title, items: [] });
    }
    groups.get(key)?.items.push(item);
  });
  return Array.from(groups.values());
}

function inferGroupTitle(path?: string) {
  if (!path) return '其他';
  if (path.startsWith('/user') || path.startsWith('user')) return '用户权限';
  if (path.startsWith('/system-support') || path.startsWith('system-support')) return '系统运维';
  if (path.startsWith('/system') || path.startsWith('system')) return '系统运维';
  return '启动模块';
}

function normalizePath(path?: string) {
  if (!path) return '/dashboard';
  return path.startsWith('/') ? path : `/${path}`;
}

function isSupportedRoute(path?: string) {
  const resolved = router.resolve(normalizePath(path));
  return resolved.matched.some((item) => item.name && item.name !== 'NotFound');
}

async function logout() {
  await authStore.logout();
  await router.push({ name: 'AdminLogin' });
}
</script>
