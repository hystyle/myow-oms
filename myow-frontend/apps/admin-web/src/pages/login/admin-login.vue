<template>
  <main class="login-page">
    <section class="login-brand">
      <div class="login-brand__mark">MYOW</div>
      <h1>MYOW Platform</h1>
      <p>统一管理用户、权限、系统配置和业务模块的内部工作台。</p>
      <dl>
        <div>
          <dt>权限驱动</dt>
          <dd>菜单、按钮、数据范围由后端统一返回。</dd>
        </div>
        <div>
          <dt>接口文档</dt>
          <dd>基于 Springdoc 契约生成前端请求类型。</dd>
        </div>
        <div>
          <dt>业务扩展</dt>
          <dd>后续接入客商、海外仓、头程和财务模块。</dd>
        </div>
      </dl>
    </section>
    <section class="login-panel">
      <h2>管理端登录</h2>
      <p>使用内部员工账号进入后台。</p>
      <form @submit.prevent="submit">
        <label>
          账号
          <input v-model="form.loginName" autocomplete="username" placeholder="请输入登录账号" />
        </label>
        <label>
          密码
          <input v-model="form.password" type="password" autocomplete="current-password" placeholder="请输入密码" />
        </label>
        <button type="submit" :disabled="authStore.loading">
          {{ authStore.loading ? '登录中...' : '登录' }}
        </button>
      </form>
      <p class="login-hint">登录后将初始化当前用户、菜单树、按钮权限和数据权限。</p>
    </section>
  </main>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/user-session';

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
