import { createRouter, createWebHistory } from 'vue-router';

export const router = createRouter({
  history: createWebHistory('/client/'),
  routes: [
    { path: '/login', name: 'ClientLogin', component: () => import('@/pages/login/ClientLogin.vue'), meta: { public: true } },
    {
      path: '/',
      component: () => import('@/layouts/ClientLayout.vue'),
      children: [
        { path: '', redirect: '/dashboard' },
        { path: 'dashboard', name: 'ClientDashboard', component: () => import('@/pages/dashboard/ClientDashboard.vue') },
        { path: 'orders', name: 'ClientOrders', component: () => import('@/pages/orders/ClientOrders.vue') },
        { path: 'inventory', name: 'ClientInventory', component: () => import('@/pages/inventory/ClientInventory.vue') },
        { path: 'billing', name: 'ClientBilling', component: () => import('@/pages/billing/ClientBilling.vue') },
        { path: 'developer', name: 'ClientDeveloper', component: () => import('@/pages/developer/ClientDeveloper.vue') }
      ]
    },
    { path: '/:pathMatch(.*)*', name: 'ClientNotFound', component: () => import('@/pages/error/ClientNotFound.vue'), meta: { public: true } }
  ]
});
