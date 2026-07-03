import type { RouteRecordRaw } from 'vue-router';

// 工作台与开发者工具模块路由
export const workspaceRoutes: RouteRecordRaw[] = [
  { path: 'dashboard', name: 'ClientDashboard', component: () => import('@/pages/dashboard/client-dashboard.vue') },
  { path: 'developer', alias: ['/developer/api-credential'], name: 'ClientDeveloper', component: () => import('@/pages/developer/client-developer.vue') },
  { path: 'accounts', alias: ['/account/member'], name: 'ClientAccounts', component: () => import('@/pages/accounts/account-permission.vue') },
  { path: 'tickets', alias: ['/support/ticket'], name: 'ClientTickets', component: () => import('@/pages/support/ticket-center.vue') }
];
