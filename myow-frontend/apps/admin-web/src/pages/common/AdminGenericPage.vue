<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>{{ pageTitle }}</h1>
        <p>{{ pageDescription }}</p>
      </div>
      <div class="heading-actions">
        <button v-if="canCreate" class="primary-action" type="button" @click="openCreate">新增</button>
        <button class="secondary-action" type="button" @click="loadData">刷新</button>
      </div>
    </header>

    <div v-if="errorMessage" class="error-banner">
      {{ errorMessage }}
    </div>
    <div v-if="toastMessage" class="success-banner">
      {{ toastMessage }}
    </div>

    <section class="toolbar">
      <input v-model="query.keyword" :placeholder="searchPlaceholder" @keyup.enter="loadData" />
      <select v-model="query.status">
        <option value="">全部状态</option>
        <option v-for="option in statusOptions" :key="String(option.value)" :value="option.value">
          {{ option.label }}
        </option>
      </select>
      <button type="button" @click="loadData">查询</button>
      <button type="button" @click="resetQuery()">重置</button>
    </section>

    <article class="panel table-panel">
      <div class="panel__head">
        <div>
          <h2>{{ listTitle }}</h2>
          <p>{{ endpoint }}</p>
        </div>
        <span class="page-status">{{ loading ? '加载中' : `${total} 条` }}</span>
      </div>

      <table class="data-table dense-table">
        <thead>
          <tr>
            <th v-for="column in columns" :key="column.key">{{ column.label }}</th>
            <th v-if="hasRowActions">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in rows" :key="rowKey(row, index)">
            <td v-for="column in columns" :key="column.key">
              <span v-if="column.key === 'status'" class="status-tag" :data-tone="statusTone(readValue(row, column.key))">
                {{ statusText(readValue(row, column.key)) }}
              </span>
              <code v-else-if="column.code">{{ formatValue(readValue(row, column.key)) }}</code>
              <span v-else>{{ formatValue(readValue(row, column.key)) }}</span>
            </td>
            <td v-if="hasRowActions" class="table-actions">
              <button v-if="canDetail" type="button" @click="openDetail(row)">详情</button>
              <button v-if="canUpdate" type="button" @click="openEdit(row)">编辑</button>
              <button v-for="action in rowActions" :key="action.label" type="button" @click="handleRowAction(action, row)">
                {{ action.label }}
              </button>
              <button v-if="canDelete" type="button" @click="handleDelete(row)">删除</button>
            </td>
          </tr>
          <tr v-if="!loading && rows.length === 0">
            <td :colspan="columns.length + (hasRowActions ? 1 : 0)" class="empty-cell">暂无数据</td>
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

    <div v-if="drawerOpen" class="crud-backdrop" @click.self="closeDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ drawerTitle }}</h2>
            <p>{{ mode === 'detail' ? '查看后端返回的详情数据。' : '填写后将提交到真实后端接口。' }}</p>
          </div>
          <button type="button" @click="closeDrawer">关闭</button>
        </header>

        <form class="crud-form" @submit.prevent="submitForm">
          <label v-for="field in formFields" :key="field.key" :class="{ 'form-wide': field.type === 'textarea' }">
            <span>{{ field.label }}</span>
            <textarea
              v-if="field.type === 'textarea'"
              v-model="formModel[field.key]"
              :disabled="mode === 'detail' || field.readonly"
              rows="5"
            />
            <select v-else-if="field.type === 'select'" v-model="formModel[field.key]" :disabled="mode === 'detail' || field.readonly">
              <option v-for="option in field.options ?? []" :key="String(option.value)" :value="option.value">
                {{ option.label }}
              </option>
            </select>
            <input
              v-else
              v-model="formModel[field.key]"
              :type="field.type === 'number' ? 'number' : field.type === 'datetime' ? 'datetime-local' : 'text'"
              :disabled="mode === 'detail' || field.readonly"
            />
          </label>

          <footer class="crud-actions">
            <button type="button" @click="closeDrawer">取消</button>
            <button v-if="mode !== 'detail'" class="primary-action" type="submit" :disabled="saving">
              {{ saving ? '保存中' : '保存' }}
            </button>
          </footer>
        </form>
      </section>
    </div>

    <div v-if="actionResultOpen" class="crud-backdrop" @click.self="closeActionResult">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ actionResultTitle }}</h2>
            <p>后端返回结果</p>
          </div>
          <button type="button" @click="closeActionResult">关闭</button>
        </header>
        <pre class="result-view">{{ actionResultText }}</pre>
      </section>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { usePermission } from '@/composables/usePermission';
import {
  createAdminRecord,
  deleteAdminRecord,
  detailAdminRecord,
  downloadAdminRecord,
  pageAdminRecords,
  updateAdminRecord,
  type AdminRecord
} from '@/services/adminDataService';

interface TableColumn {
  key: string;
  label: string;
  code?: boolean;
}

interface FormOption {
  label: string;
  value: string | number;
}

interface FormField {
  key: string;
  label: string;
  type?: 'text' | 'number' | 'textarea' | 'select' | 'datetime';
  readonly?: boolean;
  hideOnCreate?: boolean;
  json?: boolean;
  options?: FormOption[];
}

interface RowAction {
  label: string;
  endpoint: string;
  permission?: string;
  confirm?: string;
  success?: string;
  idKey?: string;
  payloadKey?: string;
  resultMode?: 'toast' | 'drawer' | 'open-url' | 'download';
  urlKey?: string;
  variablesPrompt?: boolean;
  refresh?: boolean;
}

const route = useRoute();
const { hasPermission } = usePermission();
const loading = ref(false);
const saving = ref(false);
const errorMessage = ref('');
const toastMessage = ref('');
const actionResultOpen = ref(false);
const actionResultTitle = ref('');
const actionResultText = ref('');
const rows = ref<AdminRecord[]>([]);
const total = ref(0);
const drawerOpen = ref(false);
const mode = ref<'create' | 'edit' | 'detail'>('create');
const formModel = reactive<Record<string, string | number | undefined>>({});
const query = reactive({
  keyword: '',
  status: '',
  pageNum: 1,
  pageSize: 20
});

const endpoint = computed(() => String(route.meta.endpoint ?? ''));
const baseEndpoint = computed(() => String(route.meta.baseEndpoint ?? endpoint.value.replace(/\/(page|my-page)$/, '')));
const idKey = computed(() => String(route.meta.idKey ?? 'id'));
const detailAction = computed(() => String(route.meta.detailAction ?? (baseEndpoint.value.includes('/api/v1/') ? 'detail' : 'get')));
const pageTitle = computed(() => String(route.meta.title ?? '数据管理'));
const pageDescription = computed(() => String(route.meta.description ?? '维护平台基础数据。'));
const listTitle = computed(() => String(route.meta.listTitle ?? `${pageTitle.value}列表`));
const searchPlaceholder = computed(() => String(route.meta.searchPlaceholder ?? '搜索编码、名称或关键字'));
const columns = computed<TableColumn[]>(() => route.meta.columns as TableColumn[] | undefined ?? defaultColumns.value);
const configuredFormFields = computed<FormField[]>(() => route.meta.formFields as FormField[] | undefined ?? []);
const formFields = computed<FormField[]>(() => configuredFormFields.value.filter((field) => !(mode.value === 'create' && field.hideOnCreate)));
const statusOptions = computed<FormOption[]>(() => route.meta.statusOptions as FormOption[] | undefined ?? [
  { label: '启用 / 正常 / 成功', value: 1 },
  { label: '停用 / 草稿 / 失败', value: 0 }
]);
const requiredFields = computed<string[]>(() => route.meta.requiredFields as string[] | undefined ?? []);
const rowActions = computed<RowAction[]>(() =>
  (route.meta.rowActions as RowAction[] | undefined ?? []).filter((action) => hasPermission(action.permission))
);
const canCreate = computed(() => route.meta.canCreate !== false && formFields.value.length > 0 && hasPermission(String(route.meta.createPerm ?? '')));
const canUpdate = computed(() => route.meta.canUpdate !== false && formFields.value.length > 0 && hasPermission(String(route.meta.updatePerm ?? '')));
const canDelete = computed(() => route.meta.canDelete !== false && hasPermission(String(route.meta.deletePerm ?? '')));
const canDetail = computed(() => route.meta.canDetail !== false);
const hasRowActions = computed(() => canDetail.value || canUpdate.value || canDelete.value || rowActions.value.length > 0);
const pageCount = computed(() => Math.max(1, Math.ceil(total.value / query.pageSize)));
const drawerTitle = computed(() => {
  if (mode.value === 'create') return `新增${pageTitle.value}`;
  if (mode.value === 'edit') return `编辑${pageTitle.value}`;
  return `${pageTitle.value}详情`;
});
const defaultColumns = computed<TableColumn[]>(() => {
  const sample = rows.value[0];
  if (!sample) {
    return [
      { key: 'id', label: 'ID' },
      { key: 'code', label: '编码', code: true },
      { key: 'name', label: '名称' },
      { key: 'status', label: '状态' },
      { key: 'createTime', label: '创建时间' },
      { key: 'updateTime', label: '更新时间' }
    ];
  }
  return Object.keys(sample).slice(0, 8).map((key) => ({ key, label: toLabel(key), code: key.toLowerCase().includes('code') }));
});

onMounted(() => {
  void loadData();
});

watch(
  () => route.fullPath,
  () => {
    resetQuery(false);
    void loadData();
  }
);

async function loadData() {
  if (!endpoint.value) {
    rows.value = [];
    total.value = 0;
    return;
  }
  loading.value = true;
  errorMessage.value = '';
  toastMessage.value = '';
  try {
    const page = await pageAdminRecords(endpoint.value, {
      keyword: query.keyword || undefined,
      status: query.status === '' ? undefined : Number(query.status),
      pageNum: query.pageNum,
      pageSize: query.pageSize
    });
    rows.value = page.list ?? [];
    total.value = Number(page.total ?? rows.value.length);
  } catch (error) {
    rows.value = [];
    total.value = 0;
    errorMessage.value = error instanceof Error ? error.message : `${pageTitle.value}加载失败`;
  } finally {
    loading.value = false;
  }
}

function resetQuery(load = true) {
  query.keyword = '';
  query.status = '';
  query.pageNum = 1;
  if (load) {
    void loadData();
  }
}

function changePage(pageNum: number) {
  query.pageNum = Math.max(1, pageNum);
  void loadData();
}

function changePageSize() {
  query.pageNum = 1;
  void loadData();
}

async function openCreate() {
  mode.value = 'create';
  fillForm({});
  drawerOpen.value = true;
}

async function openEdit(row: AdminRecord) {
  mode.value = 'edit';
  fillForm(row);
  drawerOpen.value = true;
  await loadDetailIfPossible(row);
}

async function openDetail(row: AdminRecord) {
  mode.value = 'detail';
  fillForm(row);
  drawerOpen.value = true;
  await loadDetailIfPossible(row);
}

async function loadDetailIfPossible(row: AdminRecord) {
  const id = readValue(row, idKey.value);
  if (id == null) return;
  try {
    const detail = await detailAdminRecord(`${baseEndpoint.value}/${detailAction.value}`, idKey.value, id);
    fillForm(flattenRecord(detail ?? row));
  } catch {
    fillForm(row);
  }
}

async function submitForm() {
  const invalidLabel = firstInvalidField();
  if (invalidLabel) {
    errorMessage.value = `请填写${invalidLabel}`;
    return;
  }
  saving.value = true;
  errorMessage.value = '';
  try {
    const payload = normalizePayload(formModel);
    if (mode.value === 'create') {
      await createAdminRecord(`${baseEndpoint.value}/create`, payload);
    } else {
      await updateAdminRecord(`${baseEndpoint.value}/update`, payload);
    }
    closeDrawer();
    showToast(`${pageTitle.value}已保存`);
    await loadData();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : `${pageTitle.value}保存失败`;
  } finally {
    saving.value = false;
  }
}

async function handleDelete(row: AdminRecord) {
  const id = readValue(row, idKey.value);
  if (id == null) return;
  if (!window.confirm(`确认删除这条${pageTitle.value}数据？`)) return;
  errorMessage.value = '';
  try {
    await deleteAdminRecord(`${baseEndpoint.value}/delete`, idKey.value, id);
    showToast(`${pageTitle.value}已删除`);
    await loadData();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : `${pageTitle.value}删除失败`;
  }
}

async function handleRowAction(action: RowAction, row: AdminRecord) {
  if (action.confirm && !window.confirm(action.confirm)) {
    return;
  }
  const actionIdKey = action.idKey ?? idKey.value;
  const payloadKey = action.payloadKey ?? actionIdKey;
  const id = readValue(row, actionIdKey);
  errorMessage.value = '';
  try {
    if (action.resultMode === 'download') {
      const result = await downloadAdminRecord(action.endpoint, payloadKey, id);
      downloadBlob(result.blob, result.fileName);
      showToast(action.success ?? `${action.label}已开始`);
      return;
    }
    const payload: AdminRecord = { [payloadKey]: id, id };
    if (action.variablesPrompt) {
      const variables = readVariablesFromPrompt();
      if (variables == null) {
        return;
      }
      payload.variables = variables;
    }
    const result = await createAdminRecord(action.endpoint, payload);
    handleActionResult(action, result);
    if (action.refresh !== false) {
      await loadData();
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : `${action.label}失败`;
  }
}

function readVariablesFromPrompt() {
  const text = window.prompt('请输入变量 JSON，例如 {"name":"test"}', '{}');
  if (text == null) {
    return null;
  }
  try {
    const value = JSON.parse(text);
    if (!value || typeof value !== 'object' || Array.isArray(value)) {
      throw new Error('变量必须是 JSON 对象');
    }
    return value as Record<string, string>;
  } catch (error) {
    throw new Error(error instanceof Error ? error.message : '变量 JSON 格式错误');
  }
}

function downloadBlob(blob: Blob, fileName: string) {
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = fileName || 'download';
  document.body.appendChild(link);
  link.click();
  link.remove();
  window.URL.revokeObjectURL(url);
}

function closeDrawer() {
  drawerOpen.value = false;
}

function closeActionResult() {
  actionResultOpen.value = false;
}

function fillForm(record: AdminRecord) {
  Object.keys(formModel).forEach((key) => delete formModel[key]);
  const flat = flattenRecord(record);
  configuredFormFields.value.forEach((field) => {
    formModel[field.key] = toFormValue(flat[field.key]);
  });
  const id = readValue(record, idKey.value);
  if (mode.value !== 'create' && id != null) {
    formModel[idKey.value] = toFormValue(id);
    formModel.id = toFormValue(id);
  }
}

function normalizePayload(source: AdminRecord) {
  const payload: AdminRecord = {};
  Object.entries(source).forEach(([key, value]) => {
    if (value === '') return;
    const field = configuredFormFields.value.find((item) => item.key === key);
    payload[key] = field?.json ? parseJsonValue(key, value) : value;
  });
  return payload;
}

function parseJsonValue(key: string, value: unknown) {
  if (typeof value !== 'string') return value;
  try {
    return JSON.parse(value);
  } catch {
    throw new Error(`${key} 必须是合法 JSON`);
  }
}

function firstInvalidField() {
  for (const key of requiredFields.value) {
    const field = formFields.value.find((item) => item.key === key);
    const value = formModel[key];
    if (value == null || value === '') {
      return field?.label ?? key;
    }
  }
  return '';
}

function showToast(message: string) {
  toastMessage.value = message;
  window.setTimeout(() => {
    if (toastMessage.value === message) {
      toastMessage.value = '';
    }
  }, 2600);
}

function handleActionResult(action: RowAction, result: unknown) {
  if (action.resultMode === 'open-url') {
    const url = readValue(flattenRecord(result as AdminRecord), action.urlKey ?? 'url');
    if (typeof url === 'string' && url) {
      window.open(url, '_blank', 'noopener,noreferrer');
      showToast(action.success ?? `${action.label}已打开`);
      return;
    }
  }
  if (action.resultMode === 'drawer') {
    actionResultTitle.value = action.label;
    actionResultText.value = JSON.stringify(result ?? {}, null, 2);
    actionResultOpen.value = true;
    return;
  }
  showToast(action.success ?? `${action.label}已完成`);
}

function rowKey(row: AdminRecord, index: number) {
  return String(readValue(row, idKey.value) ?? row.id ?? row.userId ?? row.roleId ?? row.menuId ?? row.deptId ?? row.code ?? index);
}

function readValue(row: AdminRecord, key: string) {
  if (Object.prototype.hasOwnProperty.call(row, key)) {
    return row[key];
  }
  const attrs = row.attributes;
  if (attrs && typeof attrs === 'object' && Object.prototype.hasOwnProperty.call(attrs, key)) {
    return (attrs as Record<string, unknown>)[key];
  }
  return undefined;
}

function flattenRecord(row: AdminRecord) {
  const attrs = row.attributes && typeof row.attributes === 'object' ? row.attributes as Record<string, unknown> : {};
  return { ...attrs, ...row };
}

function toFormValue(value: unknown) {
  if (value == null) return '';
  if (typeof value === 'number' || typeof value === 'string') return value;
  if (typeof value === 'boolean') return String(value);
  return JSON.stringify(value);
}

function statusText(value: unknown) {
  if (value === true || value === 'true' || value === 1) return '启用';
  if (value === false || value === 'false' || value === 0) return '停用';
  if (value === '0') return '正常';
  if (value === '1') return '停用';
  return formatValue(value);
}

function statusTone(value: unknown) {
  if (value === true || value === 'true' || value === 1 || value === '0') return 'green';
  if (value === false || value === 'false' || value === 0 || value === '1') return 'amber';
  return 'blue';
}

function formatValue(value: unknown) {
  if (value == null || value === '') return '-';
  if (Array.isArray(value)) return value.join(' / ');
  if (typeof value === 'object') return JSON.stringify(value);
  return String(value).replace('T', ' ').slice(0, 40);
}

function toLabel(key: string) {
  const labels: Record<string, string> = {
    id: 'ID',
    code: '编码',
    name: '名称',
    status: '状态',
    createTime: '创建时间',
    updateTime: '更新时间'
  };
  return labels[key] ?? key;
}
</script>
