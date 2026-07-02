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

    <div v-if="drawerOpen" class="crud-backdrop" @click.self="closeDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ deptForm.deptId ? '编辑部门' : '新增部门' }}</h2>
            <p>提交到 /myow/system/dept/create 或 /update。</p>
          </div>
          <button type="button" @click="closeDrawer">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitDept">
          <label>
            <span>部门 ID</span>
            <input v-model="deptForm.deptId" disabled />
          </label>
          <label>
            <span>上级部门 ID</span>
            <input v-model="deptForm.parentId" type="number" />
          </label>
          <label>
            <span>部门名称</span>
            <input v-model="deptForm.deptName" />
          </label>
          <label>
            <span>排序</span>
            <input v-model="deptForm.sort" type="number" />
          </label>
          <label>
            <span>负责人 ID</span>
            <input v-model="deptForm.managerId" type="number" />
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
import { postAdminData, type AdminRecord } from '@/services/adminDataService';
import { usePermission } from '@/composables/usePermission';

interface DeptNode extends AdminRecord {
  deptId?: number;
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
const deptForm = reactive<Record<string, string | number>>({
  deptId: '',
  parentId: '',
  deptName: '',
  sort: 0,
  managerId: ''
});
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
  deptForm.deptId = '';
  deptForm.parentId = selected.value?.deptId ?? '';
  deptForm.deptName = '';
  deptForm.sort = 0;
  deptForm.managerId = '';
  drawerOpen.value = true;
}

function openEdit() {
  if (!selected.value) return;
  deptForm.deptId = selected.value.deptId ?? '';
  deptForm.parentId = selected.value.parentId as number ?? '';
  deptForm.deptName = selected.value.deptName || selected.value.name || '';
  deptForm.sort = selected.value.sort as number ?? 0;
  deptForm.managerId = selected.value.managerId as number ?? '';
  drawerOpen.value = true;
}

async function submitDept() {
  if (!deptForm.deptName) {
    errorMessage.value = '请填写部门名称';
    return;
  }
  if (deptForm.sort === '') {
    errorMessage.value = '请填写排序';
    return;
  }
  saving.value = true;
  errorMessage.value = '';
  try {
    const payload = compact({
      deptId: deptForm.deptId,
      parentId: deptForm.parentId,
      deptName: deptForm.deptName,
      sort: deptForm.sort,
      managerId: deptForm.managerId
    });
    await postAdminData(`/myow/system/dept/${deptForm.deptId ? 'update' : 'create'}`, payload);
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
  if (!window.confirm('确认删除该部门？')) return;
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

function compact(source: Record<string, unknown>) {
  return Object.fromEntries(Object.entries(source).filter(([, value]) => value !== '' && value != null));
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
