<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>角色权限</h1>
        <p>配置角色基础信息、功能权限和数据范围。</p>
      </div>
      <div class="heading-actions">
        <button v-if="hasPermission('system:role:add')" class="primary-action" type="button" @click="openCreate">新增角色</button>
        <button class="secondary-action" type="button" @click="loadRoles">刷新角色</button>
      </div>
    </header>

    <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>
    <div v-if="toastMessage" class="success-banner">{{ toastMessage }}</div>

    <section class="role-layout">
      <aside class="panel role-list-panel">
        <div class="panel__head">
          <div>
            <h2>角色列表</h2>
            <p>按角色选择右侧配置项。</p>
          </div>
          <span class="page-status">{{ loading ? '加载中' : `${total} 个` }}</span>
        </div>
        <div class="role-list">
          <button
            v-for="role in roles"
            :key="role.roleId"
            type="button"
            class="role-item"
            :class="{ active: selectedRole?.roleId === role.roleId }"
            @click="selectRole(role)"
          >
            <strong>{{ role.roleName || '-' }}</strong>
            <span>{{ role.roleCode || '-' }}</span>
          </button>
        </div>
        <div v-if="!loading && roles.length === 0" class="empty-cell">当前筛选条件下没有角色。</div>
      </aside>

      <article class="panel role-detail-panel">
        <div class="panel__head">
          <div>
            <h2>{{ selectedRole?.roleName || '请选择角色' }}</h2>
            <p>{{ selectedRole ? `角色编码：${selectedRole.roleCode || '-'}` : '选择左侧角色后配置权限。' }}</p>
          </div>
          <div v-if="selectedRole" class="table-actions">
            <button v-if="hasPermission('system:role:update')" type="button" @click="openEdit(selectedRole)">编辑</button>
            <button v-if="hasPermission('system:role:delete')" type="button" @click="deleteSelectedRole">删除</button>
          </div>
        </div>

        <div class="tabs">
          <button type="button" :class="{ active: activeTab === 'basic' }" @click="activeTab = 'basic'">基本信息</button>
          <button type="button" :class="{ active: activeTab === 'menus' }" @click="activeTab = 'menus'">菜单权限</button>
          <button type="button" :class="{ active: activeTab === 'scope' }" @click="activeTab = 'scope'">数据权限</button>
        </div>

        <dl v-if="selectedRole && activeTab === 'basic'" class="detail-grid">
          <div v-for="item in roleDetailItems" :key="item.key">
            <dt>{{ item.label }}</dt>
            <dd>{{ item.value }}</dd>
          </div>
        </dl>

        <div v-else-if="selectedRole && activeTab === 'menus'" class="permission-tree-panel">
          <div class="permission-tree-head">
            <p>勾选目录、菜单和按钮权限点后保存，保存会覆盖当前角色的菜单权限。</p>
            <button v-if="hasPermission('system:role:update')" class="primary-action" type="button" :disabled="savingPermission" @click="saveRoleMenus">
              {{ savingPermission ? '保存中' : '保存菜单权限' }}
            </button>
          </div>
          <div class="permission-tree">
            <label v-for="menu in flatMenuNodes" :key="menu.menuId" class="permission-node" :style="{ paddingLeft: `${10 + menu.level * 22}px` }">
              <input
                type="checkbox"
                :checked="isChecked(menu.menuId, checkedMenuIds)"
                @change="toggleChecked(menu.menuId, checkedMenuIds)"
              />
              <span>{{ menu.menuName || '-' }}</span>
              <em>{{ menuTypeText(menu.menuType) }}</em>
              <code v-if="menu.apiPerms || menu.perms">{{ menu.apiPerms || menu.perms }}</code>
            </label>
          </div>
        </div>

        <div v-else-if="selectedRole && activeTab === 'scope'" class="scope-grid">
          <button
            v-for="option in dataScopeOptions"
            :key="option.value"
            type="button"
            class="scope-option"
            :class="{ active: selectedRole.dataScope === option.value }"
            @click="setDataScope(option.value)"
          >
            <strong>{{ option.label }}</strong>
            <span>{{ option.description }}</span>
          </button>
          <div v-if="selectedRole.dataScope === '2'" class="permission-tree-panel form-wide">
            <div class="permission-tree-head">
              <p>自定义部门数据范围仅在“自定义部门”模式下生效。</p>
              <button v-if="hasPermission('system:role:update')" class="primary-action" type="button" :disabled="savingPermission" @click="saveRoleDataScope">
                {{ savingPermission ? '保存中' : '保存数据范围' }}
              </button>
            </div>
            <div class="permission-tree">
              <label v-for="dept in flatDeptNodes" :key="dept.deptId" class="permission-node" :style="{ paddingLeft: `${10 + dept.level * 22}px` }">
                <input
                  type="checkbox"
                  :checked="isChecked(dept.deptId, checkedDeptIds)"
                  @change="toggleChecked(dept.deptId, checkedDeptIds)"
                />
                <span>{{ dept.deptName || dept.name || '-' }}</span>
              </label>
            </div>
          </div>
          <div v-else class="form-wide permission-tree-panel">
            <div class="permission-tree-head">
              <p>当前模式不需要选择部门。保存后将只更新角色的数据范围类型。</p>
              <button v-if="hasPermission('system:role:update')" class="primary-action" type="button" :disabled="savingPermission" @click="saveRoleDataScope">
                {{ savingPermission ? '保存中' : '保存数据范围' }}
              </button>
            </div>
          </div>
        </div>

        <div v-else class="empty-cell">请选择一个角色。</div>
      </article>
    </section>

    <div v-if="drawerOpen" class="crud-backdrop" @click.self="closeDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ editingRole?.roleId ? '编辑角色' : '新增角色' }}</h2>
            <p>保存角色基础信息和数据范围。</p>
          </div>
          <button type="button" @click="closeDrawer">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitRole">
          <label>
            <span>角色编码</span>
            <input v-model="form.roleCode" required />
          </label>
          <label>
            <span>角色名称</span>
            <input v-model="form.roleName" required />
          </label>
          <label>
            <span>显示顺序</span>
            <input v-model.number="form.sort" type="number" required />
          </label>
          <label>
            <span>状态</span>
            <select v-model="form.status">
              <option value="0">启用</option>
              <option value="1">停用</option>
            </select>
          </label>
          <label class="form-wide">
            <span>数据范围</span>
            <select v-model="form.dataScope">
              <option v-for="option in dataScopeOptions" :key="option.value" :value="option.value">
                {{ option.label }}
              </option>
            </select>
          </label>
          <label class="form-wide">
            <span>备注</span>
            <textarea v-model="form.remark" rows="4" />
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
import type { ApiId, DeptTreeNode, PageResult } from '@myow/api';
import { postAdminData } from '@/services/admin-data-service';
import { usePermission } from '@/composables/use-permission';

interface RoleRecord {
  roleId?: ApiId;
  roleCode?: string;
  roleName?: string;
  sort?: number;
  dataScope?: string;
  status?: string;
  remark?: string;
  createTime?: string;
  updateTime?: string;
}

interface MenuRecord {
  menuId?: ApiId;
  parentId?: ApiId;
  menuName?: string;
  menuType?: string;
  apiPerms?: string;
  perms?: string;
  sort?: number;
  children?: MenuRecord[];
}

type MenuTreeNode = MenuRecord & { children: MenuTreeNode[] };
type FlatMenuNode = MenuTreeNode & { level: number };
type DeptTreeFlatNode = DeptTreeNode & { level: number };

const dataScopeOptions = [
  { value: '1', label: '全部数据', description: '可查看和操作授权模块内全部数据。' },
  { value: '2', label: '自定义部门', description: '按指定部门范围控制数据可见性。' },
  { value: '3', label: '本部门', description: '仅当前用户所属部门数据。' },
  { value: '4', label: '本部门及下级', description: '当前部门及下级部门数据。' },
  { value: '5', label: '仅本人', description: '仅本人创建或负责的数据。' },
  { value: '6', label: '下级或本人', description: '部门及下级数据，同时包含本人数据。' }
];

const { hasPermission } = usePermission();
const loading = ref(false);
const saving = ref(false);
const savingPermission = ref(false);
const errorMessage = ref('');
const toastMessage = ref('');
const roles = ref<RoleRecord[]>([]);
const menuNodes = ref<MenuRecord[]>([]);
const deptNodes = ref<DeptTreeNode[]>([]);
const checkedMenuIds = ref<ApiId[]>([]);
const checkedDeptIds = ref<ApiId[]>([]);
const total = ref(0);
const selectedRole = ref<RoleRecord | null>(null);
const activeTab = ref<'basic' | 'menus' | 'scope'>('basic');
const drawerOpen = ref(false);
const editingRole = ref<RoleRecord | null>(null);
const form = reactive<RoleRecord>({
  roleCode: '',
  roleName: '',
  sort: 0,
  dataScope: '5',
  status: '0',
  remark: ''
});

const roleDetailItems = computed(() => {
  const role = selectedRole.value;
  if (!role) return [];
  return [
    { key: 'roleId', label: '角色 ID', value: formatValue(role.roleId) },
    { key: 'roleCode', label: '角色编码', value: formatValue(role.roleCode) },
    { key: 'roleName', label: '角色名称', value: formatValue(role.roleName) },
    { key: 'dataScope', label: '数据范围', value: dataScopeOptions.find((item) => item.value === role.dataScope)?.label ?? formatValue(role.dataScope) },
    { key: 'status', label: '状态', value: role.status === '1' ? '停用' : '启用' },
    { key: 'updateTime', label: '更新时间', value: formatTime(role.updateTime || role.createTime) }
  ];
});
const flatMenuNodes = computed<FlatMenuNode[]>(() => flattenMenuTree(buildMenuTree(menuNodes.value)));
const flatDeptNodes = computed<DeptTreeFlatNode[]>(() => flattenDeptTree(deptNodes.value));

onMounted(() => {
  void loadPermissionTrees();
  void loadRoles();
});

async function loadPermissionTrees() {
  const [menuPage, deptTree] = await Promise.all([
    postAdminData<PageResult<MenuRecord>>('/myow/system/menu/page', { pageNum: 1, pageSize: 500 }),
    postAdminData<DeptTreeNode[]>('/myow/system/dept/tree')
  ]);
  menuNodes.value = menuPage.list ?? [];
  deptNodes.value = deptTree;
}

async function loadRoles() {
  loading.value = true;
  errorMessage.value = '';
  try {
    const page = await postAdminData<PageResult<RoleRecord>>('/myow/system/role/page', { pageNum: 1, pageSize: 200 });
    roles.value = page.list ?? [];
    total.value = Number(page.total ?? roles.value.length);
    selectedRole.value = roles.value.find((role) => role.roleId === selectedRole.value?.roleId) ?? roles.value[0] ?? null;
    await loadRolePermissions();
  } catch (error) {
    roles.value = [];
    selectedRole.value = null;
    errorMessage.value = error instanceof Error ? error.message : '角色列表加载失败';
  } finally {
    loading.value = false;
  }
}

function selectRole(role: RoleRecord) {
  selectedRole.value = role;
  activeTab.value = 'basic';
  void loadRolePermissions();
}

async function loadRolePermissions() {
  if (!selectedRole.value?.roleId) {
    checkedMenuIds.value = [];
    checkedDeptIds.value = [];
    return;
  }
  const roleId = selectedRole.value.roleId;
  const [menuIds, deptIds] = await Promise.all([
    postAdminData<ApiId[]>('/myow/system/role-menu/ids', { roleId }),
    postAdminData<ApiId[]>('/myow/system/role-dept/ids', { roleId })
  ]);
  checkedMenuIds.value = menuIds.map(String);
  checkedDeptIds.value = deptIds.map(String);
}

function openCreate() {
  editingRole.value = null;
  fillForm({});
  drawerOpen.value = true;
}

function openEdit(role: RoleRecord) {
  editingRole.value = role;
  fillForm(role);
  drawerOpen.value = true;
}

async function submitRole() {
  saving.value = true;
  errorMessage.value = '';
  try {
    const payload = {
      ...form,
      roleId: editingRole.value?.roleId,
      sort: Number(form.sort) || 0
    };
    await postAdminData(`/myow/system/role/${editingRole.value?.roleId ? 'update' : 'create'}`, payload as Record<string, unknown>);
    closeDrawer();
    showToast('角色已保存');
    await loadRoles();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '角色保存失败';
  } finally {
    saving.value = false;
  }
}

async function deleteSelectedRole() {
  if (!selectedRole.value?.roleId) return;
  if (!window.confirm('确认删除该角色？删除后关联用户将失去该角色权限。')) return;
  errorMessage.value = '';
  try {
    await postAdminData('/myow/system/role/delete', { id: selectedRole.value.roleId });
    showToast('角色已删除');
    await loadRoles();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '角色删除失败';
  }
}

async function saveRoleMenus() {
  if (!selectedRole.value?.roleId) return;
  savingPermission.value = true;
  errorMessage.value = '';
  try {
    await postAdminData('/myow/system/role-menu/ids/save', {
      roleId: selectedRole.value.roleId,
      menuIdList: checkedMenuIds.value
    });
    showToast('菜单权限已保存');
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '菜单权限保存失败';
  } finally {
    savingPermission.value = false;
  }
}

async function saveRoleDataScope() {
  if (!selectedRole.value?.roleId) return;
  savingPermission.value = true;
  errorMessage.value = '';
  try {
    await postAdminData('/myow/system/role/update', {
      ...selectedRole.value,
      sort: Number(selectedRole.value.sort) || 0
    } as Record<string, unknown>);
    await postAdminData('/myow/system/role-dept/ids/save', {
      roleId: selectedRole.value.roleId,
      deptIdList: selectedRole.value.dataScope === '2' ? checkedDeptIds.value : []
    });
    showToast('数据范围已保存');
    await loadRoles();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '数据范围保存失败';
  } finally {
    savingPermission.value = false;
  }
}

function setDataScope(value: string) {
  if (!selectedRole.value) return;
  selectedRole.value = { ...selectedRole.value, dataScope: value };
}

function fillForm(role: RoleRecord) {
  form.roleCode = role.roleCode ?? '';
  form.roleName = role.roleName ?? '';
  form.sort = role.sort ?? 0;
  form.dataScope = role.dataScope ?? '5';
  form.status = role.status ?? '0';
  form.remark = role.remark ?? '';
}

function closeDrawer() {
  drawerOpen.value = false;
  editingRole.value = null;
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

function formatTime(value?: string) {
  return value ? value.replace('T', ' ').slice(0, 19) : '-';
}

function isChecked(id: ApiId | undefined, ids: ApiId[]) {
  return id != null && ids.includes(String(id));
}

function toggleChecked(id: ApiId | undefined, ids: ApiId[]) {
  if (id == null) return;
  const normalizedId = String(id);
  const index = ids.indexOf(normalizedId);
  if (index >= 0) {
    ids.splice(index, 1);
  } else {
    ids.push(normalizedId);
  }
}

function buildMenuTree(records: MenuRecord[]) {
  const nodeMap = new Map<string, MenuTreeNode>();
  records.forEach((record) => nodeMap.set(String(record.menuId), { ...record, children: [] }));
  const roots: MenuTreeNode[] = [];
  nodeMap.forEach((node) => {
    const parent = node.parentId == null ? null : nodeMap.get(String(node.parentId));
    if (parent && parent !== node) {
      parent.children.push(node);
    } else {
      roots.push(node);
    }
  });
  return roots;
}

function flattenMenuTree(nodes: MenuTreeNode[], level = 0): FlatMenuNode[] {
  return nodes.flatMap((node) => {
    return [{ ...node, level }, ...flattenMenuTree(node.children, level + 1)];
  });
}

function flattenDeptTree(nodes: DeptTreeNode[], level = 0): DeptTreeFlatNode[] {
  return nodes.flatMap((node) => {
    return [{ ...node, level }, ...flattenDeptTree(node.children ?? [], level + 1)];
  });
}

function menuTypeText(type?: string) {
  if (type === 'M') return '目录';
  if (type === 'F') return '按钮';
  return '菜单';
}
</script>
