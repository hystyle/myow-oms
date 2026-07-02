<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>工作台</h1>
        <p>查看当前账号、权限初始化状态和基础模块入口。</p>
      </div>
      <span class="page-status">已连接前端主框架</span>
    </header>

    <div class="metric-grid">
      <article class="metric-card">
        <span>当前用户</span>
        <strong>{{ authStore.user?.nickName || authStore.user?.userName || '-' }}</strong>
        <p>{{ authStore.user?.adminFlag ? '平台管理员' : '普通用户' }}</p>
      </article>
      <article class="metric-card">
        <span>菜单权限</span>
        <strong>{{ authStore.menuList.length }}</strong>
        <p>来自 /profile/bootstrap</p>
      </article>
      <article class="metric-card">
        <span>按钮权限</span>
        <strong>{{ authStore.permissionList.length }}</strong>
        <p>用于控制新增、编辑、删除等操作。</p>
      </article>
      <article class="metric-card">
        <span>强制改密</span>
        <strong>{{ authStore.forceChangePassword ? '是' : '否' }}</strong>
        <p>登录安全策略已接入。</p>
      </article>
    </div>

    <section class="workspace-grid">
      <article class="panel">
        <div class="panel__head">
          <h2>启动模块</h2>
          <p>当前优先实现 user 与 system。</p>
        </div>
        <div class="module-list">
          <router-link to="/user">
            <strong>用户中心</strong>
            <span>用户、角色、菜单、部门、数据权限</span>
          </router-link>
          <router-link to="/system">
            <strong>系统中心</strong>
            <span>任务、公告、站点配置、文件、导出</span>
          </router-link>
        </div>
      </article>

      <article class="panel">
        <div class="panel__head">
          <h2>前端契约</h2>
          <p>管理端初始化接口已经接入。</p>
        </div>
        <ul class="check-list">
          <li>登录接口：/myow/auth/login</li>
          <li>退出接口：/myow/auth/logout</li>
          <li>启动接口：/myow/profile/bootstrap</li>
          <li>Token Header：Authorization</li>
        </ul>
      </article>
    </section>
  </section>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/user-session';

const authStore = useAuthStore();
</script>
