import type { RouteRecordRaw } from 'vue-router';

// 工作台与开发者工具模块路由
export const workspaceRoutes: RouteRecordRaw[] = [
  { path: 'dashboard', name: 'ClientDashboard', component: () => import('@/pages/dashboard/client-dashboard.vue') },
  { path: 'developer', name: 'ClientDeveloper', component: () => import('@/pages/developer/client-developer.vue') }
];
