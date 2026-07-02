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
        { path: 'dashboard', name: 'ClientDashboard', component: () => import('@/pages/dashboard/ClientDashboard.vue') }
      ]
    },
    { path: '/:pathMatch(.*)*', name: 'ClientNotFound', component: () => import('@/pages/error/ClientNotFound.vue'), meta: { public: true } }
  ]
});
