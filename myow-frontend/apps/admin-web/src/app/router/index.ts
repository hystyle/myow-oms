import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/user-session';
import { userRoutes } from './modules/user';
import { systemRoutes } from './modules/system';

export const router = createRouter({
  history: createWebHistory('/'),
  routes: [
    { path: '/login', name: 'AdminLogin', component: () => import('@/pages/login/admin-login.vue'), meta: { public: true } },
    {
      path: '/',
      component: () => import('@/layouts/admin-layout.vue'),
      children: [
        { path: '', redirect: '/dashboard' },
        ...userRoutes,
        ...systemRoutes
      ]
    },
    { path: '/403', name: 'Forbidden', component: () => import('@/pages/error/forbidden.vue'), meta: { public: true } },
    { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/pages/error/not-found.vue'), meta: { public: true } }
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
