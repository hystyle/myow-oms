import type { RouteRecordRaw } from 'vue-router';

// 业务模块路由：订单、库存、账单
export const businessRoutes: RouteRecordRaw[] = [
  { path: 'orders', name: 'ClientOrders', component: () => import('@/pages/orders/orders-list.vue') },
  { path: 'inventory', name: 'ClientInventory', component: () => import('@/pages/inventory/inventory-list.vue') },
  { path: 'billing', name: 'ClientBilling', component: () => import('@/pages/billing/billing-list.vue') }
];
