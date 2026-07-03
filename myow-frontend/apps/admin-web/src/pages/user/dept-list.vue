<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>部门组织</h1>
        <p>维护组织结构、父子部门和部门状态。</p>
      </div>
      <div class="heading-actions">
        <button v-if="hasPermission('system:dept:add')" class="primary-action" type="button" @click="openCreate">新增部门</button>
        <button class="secondary-action" type="button" @click="loadTree">刷新组织</button>
      </div>
    </header>

    <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>
    <div v-if="toastMessage" class="success-banner">{{ toastMessage }}</div>

    <section class="org-layout">
      <article class="panel org-tree">
        <div class="panel__head">
          <div>
            <h2>组织树</h2>
            <p>/myow/system/dept/tree</p>
          </div>
          <span class="page-status">{{ flatNodes.length }} 个部门</span>
        </div>
        <div class="org-node-list">
          <button
            v-for="node in flatNodes"
            :key="node.deptId"
            type="button"
            class="org-node"
            :class="{ active: selected?.deptId === node.deptId }"
            :style="{ paddingLeft: `${12 + node.level * 18}px` }"
            @click="selected = node"
          >
            <strong>{{ node.deptName || node.name || '-' }}</strong>
            <span>{{ node.status === false || node.status === 'false' ? '停用' : '启用' }}</span>
          </button>
        </div>
      </article>

      <article class="panel">
        <div class="panel__head">
          <div>
            <h2>部门详情</h2>
            <p>选中组织节点后查看关键字段。</p>
          </div>
          <div v-if="selected" class="table-actions">
            <button v-if="hasPermission('system:dept:update')" type="button" @click="openEdit">编辑</button>
            <button v-if="hasPermission('system:dept:delete')" type="button" @click="handleDelete">删除</button>
          </div>
        </div>
        <dl v-if="selected" class="detail-grid">
          <div v-for="item in detailItems" :key="item.key">
            <dt>{{ item.label }}</dt>
            <dd>{{ item.value }}</dd>
          </div>
        </dl>
        <div v-else class="empty-cell">请选择部门</div>
      </article>
    </section>

    <dept-form-drawer
      v-if="drawerOpen"
      :dept="selected"
      :saving="saving"
      @close="closeDrawer"
      @submit="submitDept"
    />
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import type { ApiId, DeptCreatePayload, DeptUpdatePayload } from '@myow/api';
import { confirmDelete } from '@/composables/use-confirm-action';
import { postAdminData, type AdminRecord } from '@/services/admin-data-service';
import { usePermission } from '@/composables/use-permission';
import DeptFormDrawer from './components/dept-form-drawer.vue';

interface DeptNode extends AdminRecord {
  deptId?: ApiId;
  deptName?: string;
  name?: string;
  status?: string | boolean;
  children?: DeptNode[];
  level: number;
}

const errorMessage = ref('');
const { hasPermission } = usePermission();
const toastMessage = ref('');
const saving = ref(false);
const tree = ref<DeptNode[]>([]);
const selected = ref<DeptNode | null>(null);
const drawerOpen = ref(false);
const flatNodes = computed(() => flatten(tree.value));
const detailItems = computed(() => {
  const node = selected.value;
  if (!node) return [];
  return [
    { key: 'deptId', label: '部门 ID', value: formatValue(node.deptId) },
    { key: 'deptName', label: '部门名称', value: formatValue(node.deptName || node.name) },
    { key: 'parentId', label: '上级部门', value: formatValue(node.parentId) },
    { key: 'leader', label: '负责人', value: formatValue(node.leader) },
    { key: 'phone', label: '联系电话', value: formatValue(node.phone) },
    { key: 'email', label: '邮箱', value: formatValue(node.email) },
    { key: 'status', label: '状态', value: node.status === false || node.status === 'false' ? '停用' : '启用' }
  ];
});

onMounted(() => {
  void loadTree();
});

async function loadTree() {
  errorMessage.value = '';
  try {
    const result = await postAdminData<DeptNode[]>('/myow/system/dept/tree');
    tree.value = Array.isArray(result) ? result : [];
    selected.value = flatNodes.value[0] ?? null;
  } catch (error) {
    tree.value = [];
    selected.value = null;
    errorMessage.value = error instanceof Error ? error.message : '组织树加载失败';
  }
}

function flatten(nodes: DeptNode[], level = 0): DeptNode[] {
  return nodes.flatMap((node) => {
    const current = { ...node, level };
    return [current, ...flatten(node.children ?? [], level + 1)];
  });
}

function openCreate() {
  selected.value = null;
  drawerOpen.value = true;
}

function openEdit() {
  if (!selected.value) return;
  drawerOpen.value = true;
}

async function submitDept(payload: DeptCreatePayload | DeptUpdatePayload) {
  saving.value = true;
  errorMessage.value = '';
  try {
    const isUpdate = 'deptId' in payload && Boolean(payload.deptId);
    await postAdminData(`/myow/system/dept/${isUpdate ? 'update' : 'create'}`, payload as unknown as Record<string, unknown>);
    closeDrawer();
    showToast('部门已保存');
    await loadTree();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '部门保存失败';
  } finally {
    saving.value = false;
  }
}

async function handleDelete() {
  if (!selected.value?.deptId) return;
  if (!confirmDelete(`部门 ${selected.value.deptName || selected.value.name || selected.value.deptId}`, '删除部门会影响用户归属、角色数据权限和组织树结构。存在子部门或用户时后端应拒绝删除。')) return;
  errorMessage.value = '';
  try {
    await postAdminData('/myow/system/dept/delete', { id: selected.value.deptId });
    showToast('部门已删除');
    await loadTree();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '部门删除失败';
  }
}

function closeDrawer() {
  drawerOpen.value = false;
}

function showToast(message: string) {
  toastMessage.value = message;
  window.setTimeout(() => {
    if (toastMessage.value === message) {
      toastMessage.value = '';
    }
  }, 2600);
}

function formatValue(value: unknown) {
  return value == null || value === '' ? '-' : String(value);
}
</script>
