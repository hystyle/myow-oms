import { computed, onMounted, reactive, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { usePermission } from '@/composables/use-permission';
import { useDictOptions } from '@/composables/use-dict-options';
import { pageAdminRecords, type AdminRecord } from '@/services/admin-data-service';
import type { FormOption, TableColumn } from './admin-crud-types';

// 列表查询与分页：负责加载行数据、列定义、查询条件、分页与重置
export function useAdminList() {
  const route = useRoute();
  const { hasPermission } = usePermission();
  const loading = ref(false);
  const errorMessage = ref('');
  const toastMessage = ref('');
  const rows = ref<AdminRecord[]>([]);
  const total = ref(0);
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
  const statusDictCode = computed(() => String(route.meta.statusDictCode ?? 'sys_system_status'));
  const { options: loadedStatusOptions } = useDictOptions(statusDictCode, [
    { label: '启用', value: '1' },
    { label: '停用', value: '0' }
  ]);
  const statusOptions = computed<readonly FormOption[]>(() => loadedStatusOptions.value);
  const canDetail = computed(() => route.meta.canDetail !== false);
  const canDelete = computed(() => route.meta.canDelete !== false && hasPermission(String(route.meta.deletePerm ?? '')));
  const pageCount = computed(() => Math.max(1, Math.ceil(total.value / query.pageSize)));
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
      const keywordField = String(route.meta.keywordQueryField ?? 'keyword');
      const statusField = String(route.meta.statusQueryField ?? 'status');
      const payload: Record<string, unknown> = {
        pageNum: query.pageNum,
        pageSize: query.pageSize
      };
      if (query.keyword) {
        payload[keywordField] = query.keyword;
      }
      if (query.status !== '') {
        payload[statusField] = Number(query.status);
      }
      const page = await pageAdminRecords(endpoint.value, payload);
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

  function rowKey(row: AdminRecord, index: number) {
    return String(readValue(row, idKey.value) ?? row.id ?? row.userId ?? row.roleId ?? row.menuId ?? row.deptId ?? row.code ?? index);
  }

  function readColumnValue(row: AdminRecord, column: TableColumn) {
    return readByAliases(row, column.key, column.aliases);
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

  function readByAliases(row: AdminRecord, key: string, aliases: string[] = []) {
    const value = readValue(row, key);
    if (value != null && value !== '') {
      return value;
    }
    for (const alias of aliases) {
      const aliasValue = readValue(row, alias);
      if (aliasValue != null && aliasValue !== '') {
        return aliasValue;
      }
    }
    return value;
  }

  function flattenRecord(row: AdminRecord) {
    const attrs = row.attributes && typeof row.attributes === 'object' ? row.attributes as Record<string, unknown> : {};
    return { ...attrs, ...row };
  }

  function statusText(value: unknown) {
    const option = statusOptions.value.find((item) => String(item.value) === String(value));
    if (option) return option.label;
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

  function showToast(message: string) {
    toastMessage.value = message;
    window.setTimeout(() => {
      if (toastMessage.value === message) {
        toastMessage.value = '';
      }
    }, 2600);
  }

  return {
    loading,
    errorMessage,
    toastMessage,
    rows,
    total,
    query,
    endpoint,
    baseEndpoint,
    idKey,
    detailAction,
    pageTitle,
    pageDescription,
    listTitle,
    searchPlaceholder,
    columns,
    statusOptions,
    canDetail,
    canDelete,
    pageCount,
    loadData,
    resetQuery,
    changePage,
    changePageSize,
    rowKey,
    readColumnValue,
    readValue,
    readByAliases,
    flattenRecord,
    statusText,
    statusTone,
    formatValue,
    showToast
  };
}
