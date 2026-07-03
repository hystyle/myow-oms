import type { RouteRecordRaw } from 'vue-router';

export const customerRoutes: RouteRecordRaw[] = [
  {
    path: 'customer/customers',
    alias: ['/admin/customer/customers', '/customer/customers'],
    name: 'CustomerProfileCenter',
    component: () => import('@/pages/customer/customer-profile-list.vue'),
    meta: {
      title: '客户档案'
    }
  },
  {
    path: 'customer/blacklist',
    alias: ['/admin/customer/blacklist', '/customer/blacklist'],
    name: 'CustomerBlacklistCenter',
    component: () => import('@/pages/customer/customer-blacklist.vue'),
    meta: {
      title: '黑名单'
    }
  }
];
