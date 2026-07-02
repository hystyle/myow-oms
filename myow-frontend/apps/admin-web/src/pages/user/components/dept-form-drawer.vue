<template>
  <div class="crud-backdrop" @click.self="emit('close')">
    <section class="crud-drawer">
      <header class="crud-drawer__head">
        <div>
          <h2>{{ dept?.deptId ? '编辑部门' : '新增部门' }}</h2>
          <p>提交到 /myow/system/dept/create 或 /update。</p>
        </div>
        <button type="button" @click="emit('close')">关闭</button>
      </header>
      <form class="crud-form" @submit.prevent="submit">
        <label>
          <span>部门 ID</span>
          <input v-model="form.deptId" disabled />
        </label>
        <label>
          <span>上级部门 ID</span>
          <input v-model="form.parentId" type="number" />
        </label>
        <label>
          <span>部门名称</span>
          <input v-model="form.deptName" />
        </label>
        <label>
          <span>排序</span>
          <input v-model="form.sort" type="number" />
        </label>
        <label>
          <span>负责人 ID</span>
          <input v-model="form.managerId" type="number" />
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
import type { DeptCreatePayload, DeptUpdatePayload } from '@myow/api';
import type { AdminRecord } from '@/services/admin-data-service';

interface DeptLike extends AdminRecord {
  deptId?: number;
  deptName?: string;
  name?: string;
}

const props = defineProps<{
  dept?: DeptLike | null;
  saving: boolean;
}>();

const emit = defineEmits<{
  close: [];
  submit: [payload: DeptCreatePayload | DeptUpdatePayload];
}>();

const form = reactive<Record<string, string | number>>({
  deptId: '',
  parentId: '',
  deptName: '',
  sort: 0,
  managerId: ''
});

watch(
  () => props.dept,
  (dept) => fillDeptForm(dept),
  { immediate: true }
);

function fillDeptForm(dept?: DeptLike | null) {
  form.deptId = dept?.deptId ?? '';
  form.parentId = (dept?.parentId as number) ?? '';
  form.deptName = dept?.deptName || dept?.name || '';
  form.sort = (dept?.sort as number) ?? 0;
  form.managerId = (dept?.managerId as number) ?? '';
}

function submit() {
  const payload = {
    deptId: form.deptId ? Number(form.deptId) : undefined,
    parentId: form.parentId ? Number(form.parentId) : undefined,
    deptName: String(form.deptName),
    sort: Number(form.sort) || undefined,
    managerId: form.managerId ? Number(form.managerId) : undefined
  };
  emit('submit', payload);
}
</script>
