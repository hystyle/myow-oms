<template>
  <main class="login-page">
    <section class="login-panel">
      <h1>MYOW Admin</h1>
      <p>Internal management console</p>
      <form @submit.prevent="submit">
        <label>
          Account
          <input v-model="form.loginName" autocomplete="username" />
        </label>
        <label>
          Password
          <input v-model="form.password" type="password" autocomplete="current-password" />
        </label>
        <button type="submit" :disabled="authStore.loading">
          {{ authStore.loading ? 'Signing in...' : 'Sign in' }}
        </button>
      </form>
    </section>
  </main>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

const form = reactive({ loginName: '', password: '', loginClient: 'ADMIN' });

async function submit() {
  await authStore.login(form);
  const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/dashboard';
  await router.push(redirect);
}
</script>
