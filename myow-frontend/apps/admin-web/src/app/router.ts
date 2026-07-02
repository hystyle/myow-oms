import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

export const router = createRouter({
  history: createWebHistory('/admin/'),
  routes: [
    { path: '/login', name: 'AdminLogin', component: () => import('@/pages/login/AdminLogin.vue'), meta: { public: true } },
    {
      path: '/',
      component: () => import('@/layouts/AdminLayout.vue'),
      children: [
        { path: '', redirect: '/dashboard' },
        { path: 'dashboard', name: 'AdminDashboard', component: () => import('@/pages/dashboard/AdminDashboard.vue') }
      ]
    },
    { path: '/403', name: 'Forbidden', component: () => import('@/pages/error/Forbidden.vue'), meta: { public: true } },
    { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/pages/error/NotFound.vue'), meta: { public: true } }
  ]
});

router.beforeEach(async (to) => {
  if (to.meta.public) {
    return true;
  }
  const authStore = useAuthStore();
  if (!authStore.token) {
    return { name: 'AdminLogin', query: { redirect: to.fullPath } };
  }
  if (!authStore.bootstrapped) {
    await authStore.bootstrap();
  }
  return true;
});
