<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>用户中心</h1>
        <p>管理内部用户、登录状态、密码策略和账号安全操作。</p>
      </div>
      <button class="primary-action" type="button" @click="loadUsers">刷新用户</button>
    </header>

    <div v-if="errorMessage" class="error-banner">
      {{ errorMessage }}
    </div>

    <section class="toolbar">
      <input v-model="query.keyword" placeholder="搜索用户名、姓名、手机号" @keyup.enter="loadUsers" />
      <select v-model="query.status">
        <option value="">全部状态</option>
        <option value="true">正常</option>
        <option value="false">停用</option>
      </select>
      <button type="button" @click="loadUsers">查询</button>
      <button type="button" @click="resetQuery">重置</button>
    </section>

    <section class="split-view">
      <aside class="tree-panel">
        <h2>组织视图</h2>
        <button type="button" class="tree-item active">全部部门</button>
        <p class="tree-note">部门树接口尚未开放，当前按全部部门查询。</p>
      </aside>

      <article class="panel table-panel">
        <div class="panel__head">
          <div>
            <h2>用户列表</h2>
            <p>来自 UserController /system/user/page。</p>
          </div>
          <span class="page-status">{{ loading ? '加载中' : `${total} 条` }}</span>
        </div>
        <table class="data-table">
          <thead>
            <tr>
              <th>用户名</th>
              <th>姓名</th>
              <th>部门</th>
              <th>角色</th>
              <th>状态</th>
              <th>最近登录</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="row in rows" :key="row.userId">
              <td>{{ row.userName || '-' }}</td>
              <td>{{ row.nickName || '-' }}</td>
              <td>{{ row.deptName || '-' }}</td>
              <td>{{ formatRoles(row.roleNameList) }}</td>
              <td><span class="status-tag" :data-tone="userStatusTone(row.status)">{{ userStatusText(row.status) }}</span></td>
              <td>{{ formatTime(row.lastLoginTime) }}</td>
              <td class="table-actions">
                <button type="button" @click="handleToggleStatus(row)">{{ isEnabled(row.status) ? '停用' : '启用' }}</button>
                <button type="button" @click="handleResetPassword(row.userId)">重置密码</button>
                <button type="button" @click="handleUnlock(row.userId)">解锁</button>
                <button type="button" @click="handleForceChangePassword(row.userId)">强制改密</button>
              </td>
            </tr>
            <tr v-if="!loading && rows.length === 0">
              <td colspan="7" class="empty-cell">暂无用户数据</td>
            </tr>
          </tbody>
        </table>
      </article>
    </section>
  </section>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import type { UserProfile } from '@myow/api';
import {
  forceUserChangePassword,
  pageUsers,
  resetUserPassword,
  unlockUser,
  updateUserStatus
} from '@/services/userService';

const loading = ref(false);
const errorMessage = ref('');
const rows = ref<UserProfile[]>([]);
const total = ref(0);
const query = reactive({
  keyword: '',
  status: '',
  pageNum: 1,
  pageSize: 20
});

onMounted(() => {
  void loadUsers();
});

async function loadUsers() {
  loading.value = true;
  errorMessage.value = '';
  try {
    const page = await pageUsers({
      keyword: query.keyword || undefined,
      status: query.status || undefined,
      pageNum: query.pageNum,
      pageSize: query.pageSize
    });
    rows.value = page.list ?? [];
    total.value = Number(page.total ?? rows.value.length);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '用户数据加载失败';
  } finally {
    loading.value = false;
  }
}

function resetQuery() {
  query.keyword = '';
  query.status = '';
  query.pageNum = 1;
  void loadUsers();
}

async function handleToggleStatus(row: UserProfile) {
  if (!row.userId) {
    return;
  }
  await updateUserStatus(row.userId, !isEnabled(row.status));
  await loadUsers();
}

async function handleResetPassword(userId: number) {
  await resetUserPassword(userId);
}

async function handleUnlock(userId: number) {
  await unlockUser(userId);
  await loadUsers();
}

async function handleForceChangePassword(userId: number) {
  await forceUserChangePassword(userId);
  await loadUsers();
}

function formatRoles(roleNameList?: string[]) {
  return roleNameList?.length ? roleNameList.join(' / ') : '-';
}

function userStatusText(status?: string) {
  if (status === 'true') return '正常';
  if (status === 'false') return '停用';
  return status || '-';
}

function userStatusTone(status?: string) {
  if (status === 'true') return 'green';
  return 'amber';
}

function isEnabled(status?: string) {
  return status === 'true';
}

function formatTime(value?: string) {
  if (!value) {
    return '-';
  }
  return value.replace('T', ' ').slice(0, 19);
}
</script>
