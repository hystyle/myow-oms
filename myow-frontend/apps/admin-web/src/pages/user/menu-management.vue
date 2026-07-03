<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>菜单权限</h1>
        <p>维护后台目录、菜单、按钮权限点和前端路由映射。</p>
      </div>
      <div class="heading-actions">
        <button v-if="hasPermission('system:menu:add')" class="primary-action" type="button" @click="openCreate()">新增菜单</button>
        <button class="secondary-action" type="button" @click="loadMenus">刷新菜单</button>
      </div>
    </header>

    <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>
    <div v-if="toastMessage" class="success-banner">{{ toastMessage }}</div>

    <section class="toolbar query-panel">
      <label>
        <span>菜单名称</span>
        <input v-model="query.menuName" placeholder="搜索菜单名称" @keyup.enter="loadMenus" />
      </label>
      <label>
        <span>菜单类型</span>
        <select v-model="query.menuType">
          <option value="">全部类型</option>
          <option value="M">目录</option>
          <option value="C">菜单</option>
          <option value="F">按钮</option>
        </select>
      </label>
      <label>
        <span>状态</span>
        <select v-model="query.status">
          <option value="">全部状态</option>
          <option value="0">启用</option>
          <option value="1">停用</option>
        </select>
      </label>
      <div class="query-actions">
        <button type="button" @click="loadMenus">查询</button>
        <button type="button" @click="resetQuery">重置</button>
      </div>
    </section>

    <article class="panel table-panel">
      <div class="panel__head">
        <div>
          <h2>菜单树</h2>
          <p>目录、菜单和按钮权限点统一维护。</p>
        </div>
        <span class="page-status">{{ loading ? '加载中' : `${flatMenus.length} 项` }}</span>
      </div>
      <table class="data-table dense-table">
        <thead>
          <tr>
            <th>菜单名称</th>
            <th>类型</th>
            <th>路由</th>
            <th>组件</th>
            <th>权限码</th>
            <th>状态</th>
            <th>排序</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="menu in flatMenus" :key="menu.menuId">
            <td :style="{ paddingLeft: `${10 + menu.level * 22}px` }">
              <strong>{{ menu.menuName || '-' }}</strong>
            </td>
            <td><span class="status-tag" data-tone="blue">{{ menuTypeText(menu.menuType) }}</span></td>
            <td><code>{{ menu.path || '-' }}</code></td>
            <td><code>{{ menu.component || '-' }}</code></td>
            <td><code>{{ menu.apiPerms || menu.perms || '-' }}</code></td>
            <td><span class="status-tag" :data-tone="menu.status === '1' ? 'amber' : 'green'">{{ menu.status === '1' ? '停用' : '启用' }}</span></td>
            <td>{{ menu.sort ?? '-' }}</td>
            <td class="table-actions">
              <button v-if="hasPermission('system:menu:add')" type="button" @click="openCreate(menu)">新增子项</button>
              <button v-if="hasPermission('system:menu:update')" type="button" @click="openEdit(menu)">编辑</button>
              <button v-if="hasPermission('system:menu:delete')" type="button" @click="deleteMenu(menu)">删除</button>
            </td>
          </tr>
          <tr v-if="!loading && flatMenus.length === 0">
            <td colspan="8" class="empty-cell">当前筛选条件下没有菜单，请调整条件或重置筛选。</td>
          </tr>
        </tbody>
      </table>
    </article>

    <div v-if="drawerOpen" class="crud-backdrop" @click.self="closeDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ editingMenu?.menuId ? '编辑菜单' : '新增菜单' }}</h2>
            <p>目录用于导航，菜单用于页面路由，按钮用于权限点控制。</p>
          </div>
          <button type="button" @click="closeDrawer">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitMenu">
          <label>
            <span>菜单名称</span>
            <input v-model="form.menuName" required />
          </label>
          <label>
            <span>上级菜单 ID</span>
            <input v-model="form.parentId" />
          </label>
          <label>
            <span>菜单类型</span>
            <select v-model="form.menuType">
              <option value="M">目录</option>
              <option value="C">菜单</option>
              <option value="F">按钮</option>
            </select>
          </label>
          <label>
            <span>排序</span>
            <input v-model.number="form.sort" type="number" />
          </label>
          <label>
            <span>路由地址</span>
            <input v-model="form.path" />
          </label>
          <label>
            <span>组件路径</span>
            <input v-model="form.component" />
          </label>
          <label>
            <span>权限码</span>
            <input v-model="form.perms" placeholder="system:user:add" />
          </label>
          <label>
            <span>图标</span>
            <input v-model="form.icon" />
          </label>
          <label>
            <span>显示状态</span>
            <select v-model="form.visible">
              <option value="0">显示</option>
              <option value="1">隐藏</option>
            </select>
          </label>
          <label>
            <span>菜单状态</span>
            <select v-model="form.status">
              <option value="0">启用</option>
              <option value="1">停用</option>
            </select>
          </label>
          <label>
            <span>是否缓存</span>
            <select v-model="form.isCache">
              <option value="0">缓存</option>
              <option value="1">不缓存</option>
            </select>
          </label>
          <label>
            <span>是否外链</span>
            <select v-model="form.isFrame">
              <option value="0">外链</option>
              <option value="1">非外链</option>
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
import type { ApiId, PageResult } from '@myow/api';
import { confirmDelete } from '@/composables/use-confirm-action';
import { postAdminData } from '@/services/admin-data-service';
import { usePermission } from '@/composables/use-permission';

interface MenuRecord {
  menuId?: ApiId;
  parentId?: ApiId;
  menuName?: string;
  menuType?: string;
  path?: string;
  component?: string;
  apiPerms?: string;
  perms?: string;
  icon?: string;
  sort?: number;
  visible?: string;
  status?: string;
  isCache?: string;
  isFrame?: string;
  remark?: string;
  children?: MenuRecord[];
}

const { hasPermission } = usePermission();
const loading = ref(false);
const saving = ref(false);
const errorMessage = ref('');
const toastMessage = ref('');
const menus = ref<MenuRecord[]>([]);
const drawerOpen = ref(false);
const editingMenu = ref<MenuRecord | null>(null);
const query = reactive({
  menuName: '',
  menuType: '',
  status: ''
});
const form = reactive<MenuRecord>({
  parentId: '0',
  menuName: '',
  menuType: 'C',
  path: '',
  component: '',
  perms: '',
  icon: '',
  sort: 0,
  visible: '0',
  status: '0',
  isCache: '0',
  isFrame: '1',
  remark: ''
});
const flatMenus = computed(() => flattenMenus(buildMenuTree(menus.value)));

onMounted(() => {
  void loadMenus();
});

async function loadMenus() {
  loading.value = true;
  errorMessage.value = '';
  try {
    const page = await postAdminData<PageResult<MenuRecord>>('/myow/system/menu/page', {
      pageNum: 1,
      pageSize: 500,
      menuName: query.menuName || undefined,
      menuType: query.menuType || undefined,
      status: query.status || undefined
    });
    menus.value = page.list ?? [];
  } catch (error) {
    menus.value = [];
    errorMessage.value = error instanceof Error ? error.message : '菜单树加载失败';
  } finally {
    loading.value = false;
  }
}

function resetQuery() {
  query.menuName = '';
  query.menuType = '';
  query.status = '';
  void loadMenus();
}

function openCreate(parent?: MenuRecord) {
  editingMenu.value = null;
  fillForm({ parentId: parent?.menuId ?? '0', menuType: parent?.menuType === 'F' ? 'F' : 'C' });
  drawerOpen.value = true;
}

function openEdit(menu: MenuRecord) {
  editingMenu.value = menu;
  fillForm(menu);
  drawerOpen.value = true;
}

async function submitMenu() {
  saving.value = true;
  errorMessage.value = '';
  try {
    const payload = {
      ...form,
      menuId: editingMenu.value?.menuId,
      parentId: form.parentId ? String(form.parentId) : '0',
      sort: Number(form.sort) || 0
    };
    await postAdminData(`/myow/system/menu/${editingMenu.value?.menuId ? 'update' : 'create'}`, payload as Record<string, unknown>);
    closeDrawer();
    showToast('菜单已保存');
    await loadMenus();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '菜单保存失败';
  } finally {
    saving.value = false;
  }
}

async function deleteMenu(menu: MenuRecord) {
  if (!menu.menuId) return;
  if (!confirmDelete(`菜单 ${menu.menuName || menu.menuId}`, '删除目录或菜单会影响子菜单、按钮权限和角色授权，可能导致用户无法访问相关页面。')) return;
  errorMessage.value = '';
  try {
    await postAdminData('/myow/system/menu/delete', { id: menu.menuId });
    showToast('菜单已删除');
    await loadMenus();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '菜单删除失败';
  }
}

function fillForm(menu: MenuRecord) {
  form.parentId = menu.parentId ?? '0';
  form.menuName = menu.menuName ?? '';
  form.menuType = menu.menuType ?? 'C';
  form.path = menu.path ?? '';
  form.component = menu.component ?? '';
  form.perms = menu.perms ?? menu.apiPerms ?? '';
  form.icon = menu.icon ?? '';
  form.sort = menu.sort ?? 0;
  form.visible = menu.visible ?? '0';
  form.status = menu.status ?? '0';
  form.isCache = menu.isCache ?? '0';
  form.isFrame = menu.isFrame ?? '1';
  form.remark = menu.remark ?? '';
}

function closeDrawer() {
  drawerOpen.value = false;
  editingMenu.value = null;
}

function buildMenuTree(records: MenuRecord[]) {
  const nodeMap = new Map<string, MenuRecord & { children: MenuRecord[] }>();
  records.forEach((record) => nodeMap.set(String(record.menuId), { ...record, children: [] }));
  const roots: Array<MenuRecord & { children: MenuRecord[] }> = [];
  nodeMap.forEach((node) => {
    const parent = node.parentId == null ? null : nodeMap.get(String(node.parentId));
    if (parent && parent.menuId !== node.menuId) {
      parent.children.push(node);
    } else {
      roots.push(node);
    }
  });
  return roots.sort(sortMenu);
}

function flattenMenus(nodes: MenuRecord[], level = 0): Array<MenuRecord & { level: number }> {
  return nodes.flatMap((node) => {
    const children = [...(node.children ?? [])].sort(sortMenu);
    return [{ ...node, level }, ...flattenMenus(children, level + 1)];
  });
}

function sortMenu(a: MenuRecord, b: MenuRecord) {
  return Number(a.sort ?? 0) - Number(b.sort ?? 0);
}

function menuTypeText(type?: string) {
  if (type === 'M') return '目录';
  if (type === 'F') return '按钮';
  return '菜单';
}

function showToast(message: string) {
  toastMessage.value = message;
  window.setTimeout(() => {
    if (toastMessage.value === message) {
      toastMessage.value = '';
    }
  }, 2600);
}
</script>
