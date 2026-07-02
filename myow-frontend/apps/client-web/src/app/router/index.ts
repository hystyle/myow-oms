import { createRouter, createWebHistory } from 'vue-router';
import { workspaceRoutes } from './modules/workspace';
import { businessRoutes } from './modules/business';

export const router = createRouter({
  history: createWebHistory('/client/'),
  routes: [
    { path: '/login', name: 'ClientLogin', component: () => import('@/pages/login/client-login.vue'), meta: { public: true } },
    {
      path: '/',
      component: () => import('@/layouts/client-layout.vue'),
      children: [
        { path: '', redirect: '/dashboard' },
        ...workspaceRoutes,
        ...businessRoutes
      ]
    },
    { path: '/:pathMatch(.*)*', name: 'ClientNotFound', component: () => import('@/pages/error/client-not-found.vue'), meta: { public: true } }
  ]
});
