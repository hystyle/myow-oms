<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>用户中心</h1>
        <p>管理内部用户、登录状态、密码策略和账号安全操作。</p>
      </div>
      <div class="heading-actions">
        <button v-if="hasPermission('system:user:add')" class="primary-action" type="button" @click="openCreate">新增用户</button>
        <button class="secondary-action" type="button" @click="loadUsers">刷新用户</button>
      </div>
    </header>

    <div v-if="errorMessage" class="error-banner">
      {{ errorMessage }}
    </div>
    <div v-if="toastMessage" class="success-banner">
      {{ toastMessage }}
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
                <button v-if="hasPermission('system:user:update')" type="button" @click="openEdit(row)">编辑</button>
                <button v-if="hasPermission('system:user:update')" type="button" @click="handleToggleStatus(row)">{{ isEnabled(row.status) ? '停用' : '启用' }}</button>
                <button v-if="hasPermission('system:user:update')" type="button" @click="handleResetPassword(row.userId)">重置密码</button>
                <button v-if="hasPermission('system:user:update')" type="button" @click="handleUnlock(row.userId)">解锁</button>
                <button v-if="hasPermission('system:user:update')" type="button" @click="handleForceChangePassword(row.userId)">强制改密</button>
                <button v-if="hasPermission('system:user:delete')" type="button" @click="handleDelete(row.userId)">删除</button>
              </td>
            </tr>
            <tr v-if="!loading && rows.length === 0">
              <td colspan="7" class="empty-cell">暂无用户数据</td>
            </tr>
          </tbody>
        </table>
        <footer class="pagination-bar">
          <span>第 {{ query.pageNum }} 页 / 共 {{ pageCount }} 页</span>
          <select v-model.number="query.pageSize" @change="changePageSize">
            <option :value="10">10 条/页</option>
            <option :value="20">20 条/页</option>
            <option :value="50">50 条/页</option>
            <option :value="100">100 条/页</option>
          </select>
          <button type="button" :disabled="query.pageNum <= 1 || loading" @click="changePage(query.pageNum - 1)">上一页</button>
          <button type="button" :disabled="query.pageNum >= pageCount || loading" @click="changePage(query.pageNum + 1)">下一页</button>
        </footer>
      </article>
    </section>

    <div v-if="drawerOpen" class="crud-backdrop" @click.self="closeDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ userForm.userId ? '编辑用户' : '新增用户' }}</h2>
            <p>提交到 UserController 的 create / update 接口。</p>
          </div>
          <button type="button" @click="closeDrawer">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitUser">
          <label>
            <span>用户 ID</span>
            <input v-model="userForm.userId" disabled />
          </label>
          <label>
            <span>登录账号</span>
            <input v-model="userForm.loginName" :disabled="Boolean(userForm.userId)" />
          </label>
          <label>
            <span>姓名</span>
            <input v-model="userForm.nickName" />
          </label>
          <label>
            <span>部门 ID</span>
            <input v-model="userForm.deptId" type="number" />
          </label>
          <label>
            <span>岗位 ID</span>
            <input v-model="userForm.positionId" type="number" />
          </label>
          <label>
            <span>角色 ID 列表</span>
            <input v-model="userForm.roleIds" placeholder="多个角色用英文逗号分隔" />
          </label>
          <label>
            <span>性别</span>
            <select v-model="userForm.gender">
              <option value="">未设置</option>
              <option value="0">未知</option>
              <option value="1">男</option>
              <option value="2">女</option>
            </select>
          </label>
          <label>
            <span>手机</span>
            <input v-model="userForm.phone" />
          </label>
          <label>
            <span>邮箱</span>
            <input v-model="userForm.email" />
          </label>
          <label class="form-wide">
            <span>备注</span>
            <textarea v-model="userForm.remark" rows="4" />
          </label>
          <footer class="crud-actions">
            <button type="button" @click="closeDrawer">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type { UserProfile } from '@myow/api';
import { usePermission } from '@/composables/usePermission';
import {
  createUser,
  deleteUser,
  forceUserChangePassword,
  getUser,
  pageUsers,
  resetUserPassword,
  unlockUser,
  updateUser,
  updateUserStatus
} from '@/services/userService';

const loading = ref(false);
const { hasPermission } = usePermission();
const saving = ref(false);
const errorMessage = ref('');
const toastMessage = ref('');
const rows = ref<UserProfile[]>([]);
const total = ref(0);
const drawerOpen = ref(false);
const userForm = reactive({
  userId: '',
  loginName: '',
  nickName: '',
  deptId: '',
  positionId: '',
  roleIds: '',
  gender: '',
  phone: '',
  email: '',
  remark: ''
});
const pageCount = computed(() => Math.max(1, Math.ceil(total.value / query.pageSize)));
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

function changePage(pageNum: number) {
  query.pageNum = Math.max(1, pageNum);
  void loadUsers();
}

function changePageSize() {
  query.pageNum = 1;
  void loadUsers();
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
  if (!window.confirm(`确认${isEnabled(row.status) ? '停用' : '启用'}该用户？`)) return;
  await updateUserStatus(row.userId, !isEnabled(row.status));
  showToast('用户状态已更新');
  await loadUsers();
}

async function handleResetPassword(userId: number) {
  if (!window.confirm('确认重置该用户密码？')) return;
  await resetUserPassword(userId);
  showToast('用户密码已重置');
}

async function handleUnlock(userId: number) {
  if (!window.confirm('确认解锁该用户？')) return;
  await unlockUser(userId);
  showToast('用户已解锁');
  await loadUsers();
}

async function handleForceChangePassword(userId: number) {
  if (!window.confirm('确认要求该用户下次登录强制改密？')) return;
  await forceUserChangePassword(userId);
  showToast('已设置强制改密');
  await loadUsers();
}

function openCreate() {
  fillUserForm();
  drawerOpen.value = true;
}

async function openEdit(row: UserProfile) {
  fillUserForm(row);
  drawerOpen.value = true;
  try {
    const detail = await getUser(row.userId);
    fillUserForm(detail);
  } catch {
    fillUserForm(row);
  }
}

async function submitUser() {
  const invalid = firstInvalidField();
  if (invalid) {
    errorMessage.value = `请填写${invalid}`;
    return;
  }
  saving.value = true;
  errorMessage.value = '';
  try {
    const payload = compact({
      userId: userForm.userId,
      loginName: userForm.loginName,
      nickName: userForm.nickName,
      deptId: userForm.deptId,
      positionId: userForm.positionId,
      roleIdList: parseIdList(userForm.roleIds),
      gender: userForm.gender,
      phone: userForm.phone,
      email: userForm.email,
      remark: userForm.remark
    });
    if (userForm.userId) {
      await updateUser(payload);
    } else {
      await createUser(payload);
    }
    closeDrawer();
    showToast('用户已保存');
    await loadUsers();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '用户保存失败';
  } finally {
    saving.value = false;
  }
}

async function handleDelete(userId: number) {
  if (!window.confirm('确认删除该用户？')) return;
  errorMessage.value = '';
  try {
    await deleteUser(userId);
    showToast('用户已删除');
    await loadUsers();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '用户删除失败';
  }
}

function firstInvalidField() {
  if (!userForm.userId && !userForm.loginName) return '登录账号';
  if (!userForm.nickName) return '姓名';
  if (!userForm.deptId) return '部门 ID';
  return '';
}

function closeDrawer() {
  drawerOpen.value = false;
}

function fillUserForm(user?: UserProfile) {
  userForm.userId = user?.userId ? String(user.userId) : '';
  userForm.loginName = user?.userName ?? '';
  userForm.nickName = user?.nickName ?? '';
  userForm.deptId = user?.deptId ? String(user.deptId) : '';
  userForm.positionId = user?.positionId ? String(user.positionId) : '';
  userForm.roleIds = user?.roleIdList?.join(',') ?? '';
  userForm.gender = user?.gender ?? '';
  userForm.phone = user?.phone ?? '';
  userForm.email = user?.email ?? '';
  userForm.remark = user?.remark ?? '';
}

function parseIdList(value: string) {
  return value
    .split(',')
    .map((item) => item.trim())
    .filter(Boolean)
    .map(Number);
}

function compact(source: Record<string, unknown>) {
  return Object.fromEntries(Object.entries(source).filter(([, value]) => {
    if (Array.isArray(value)) return value.length > 0;
    return value !== '' && value != null;
  }));
}

function showToast(message: string) {
  toastMessage.value = message;
  window.setTimeout(() => {
    if (toastMessage.value === message) {
      toastMessage.value = '';
    }
  }, 2600);
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
