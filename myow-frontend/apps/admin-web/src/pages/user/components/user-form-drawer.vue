<template>
  <div class="crud-backdrop" @click.self="emit('close')">
    <section class="crud-drawer">
      <header class="crud-drawer__head">
        <div>
          <h2>{{ user?.userId ? '编辑用户' : '新增用户' }}</h2>
          <p>提交到 UserController 的 create / update 接口。</p>
        </div>
        <button type="button" @click="emit('close')">关闭</button>
      </header>
      <form class="crud-form" @submit.prevent="submit">
        <label>
          <span>用户 ID</span>
          <input v-model="form.userId" disabled />
        </label>
        <label>
          <span>登录账号</span>
          <input v-model="form.loginName" :disabled="Boolean(form.userId)" />
        </label>
        <label>
          <span>姓名</span>
          <input v-model="form.nickName" />
        </label>
        <label>
          <span>部门 ID</span>
          <input v-model="form.deptId" type="number" />
        </label>
        <label>
          <span>岗位 ID</span>
          <input v-model="form.positionId" type="number" />
        </label>
        <label>
          <span>角色 ID 列表</span>
          <input v-model="form.roleIds" placeholder="多个角色用英文逗号分隔" />
        </label>
        <label>
          <span>性别</span>
          <select v-model="form.gender">
            <option value="">未设置</option>
            <option value="0">未知</option>
            <option value="1">男</option>
            <option value="2">女</option>
          </select>
        </label>
        <label>
          <span>手机</span>
          <input v-model="form.phone" />
        </label>
        <label>
          <span>邮箱</span>
          <input v-model="form.email" />
        </label>
        <label class="form-wide">
          <span>备注</span>
          <textarea v-model="form.remark" rows="4" />
        </label>
        <footer class="crud-actions">
          <button type="button" @click="emit('close')">取消</button>
          <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中' : '保存' }}</button>
        </footer>
      </form>
    </section>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch } from 'vue';
import type { UserProfile, UserCreatePayload, UserUpdatePayload } from '@myow/api';

const props = defineProps<{
  user?: UserProfile;
  saving: boolean;
}>();

const emit = defineEmits<{
  close: [];
  submit: [payload: UserCreatePayload | UserUpdatePayload];
}>();

const form = reactive({
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

watch(
  () => props.user,
  (user) => fillUserForm(user),
  { immediate: true }
);

function fillUserForm(user?: UserProfile) {
  form.userId = user?.userId ? String(user.userId) : '';
  form.loginName = user?.userName ?? '';
  form.nickName = user?.nickName ?? '';
  form.deptId = user?.deptId ? String(user.deptId) : '';
  form.positionId = user?.positionId ? String(user.positionId) : '';
  form.roleIds = user?.roleIdList?.join(',') ?? '';
  form.gender = user?.gender ?? '';
  form.phone = user?.phone ?? '';
  form.email = user?.email ?? '';
  form.remark = user?.remark ?? '';
}

function submit() {
  const payload = {
    userId: form.userId ? Number(form.userId) : undefined,
    loginName: form.loginName,
    nickName: form.nickName,
    deptId: form.deptId ? Number(form.deptId) : undefined,
    positionId: form.positionId ? Number(form.positionId) : undefined,
    roleIdList: parseIdList(form.roleIds),
    gender: form.gender || undefined,
    phone: form.phone || undefined,
    email: form.email || undefined,
    remark: form.remark || undefined
  };
  emit('submit', payload);
}

function parseIdList(value: string) {
  return value
    .split(',')
    .map((item) => item.trim())
    .filter(Boolean)
    .map(Number);
}
</script>
