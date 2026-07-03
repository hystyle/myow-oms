import { computed, reactive, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { usePermission } from '@/composables/use-permission';
import {
  createAdminRecord,
  detailAdminRecord,
  updateAdminRecord,
  type AdminRecord
} from '@/services/admin-data-service';
import { pageDictData, pageDicts } from '@/services/user-service';
import type { FormField, FormOption } from './admin-crud-types';

// 表单与抽屉：负责 create/edit/detail 模式切换、表单填充、提交保存
export function useAdminFormDrawer(deps: {
  baseEndpoint: () => string;
  detailAction: () => string;
  idKey: () => string;
  readValue: (row: AdminRecord, key: string) => unknown;
  readByAliases: (row: AdminRecord, key: string, aliases?: string[]) => unknown;
  flattenRecord: (row: AdminRecord) => AdminRecord;
  showToast: (message: string) => void;
  loadData: () => Promise<void>;
}) {
  const route = useRoute();
  const { hasPermission } = usePermission();
  const saving = ref(false);
  const drawerOpen = ref(false);
  const mode = ref<'create' | 'edit' | 'detail'>('create');
  const formModel = reactive<Record<string, string | number | undefined>>({});
  const fieldDictOptions = ref<Record<string, FormOption[]>>({});

  const configuredFormFields = computed<FormField[]>(() => route.meta.formFields as FormField[] | undefined ?? []);
  const formFields = computed<FormField[]>(() => configuredFormFields.value
    .map((field) => field.dictCode ? { ...field, options: fieldDictOptions.value[field.dictCode] ?? field.options ?? [] } : field)
    .filter((field) => !(mode.value === 'create' && field.hideOnCreate)));
  const requiredFields = computed<string[]>(() => route.meta.requiredFields as string[] | undefined ?? []);
  const canCreate = computed(() => route.meta.canCreate !== false && formFields.value.length > 0 && hasPermission(String(route.meta.createPerm ?? '')));
  const canUpdate = computed(() => route.meta.canUpdate !== false && formFields.value.length > 0 && hasPermission(String(route.meta.updatePerm ?? '')));
  const pageTitle = computed(() => String(route.meta.title ?? '数据管理'));
  const drawerTitle = computed(() => {
    if (mode.value === 'create') return `新增${pageTitle.value}`;
    if (mode.value === 'edit') return `编辑${pageTitle.value}`;
    return `${pageTitle.value}详情`;
  });

  watch(
    () => configuredFormFields.value.map((field) => field.dictCode).filter(Boolean).join('|'),
    () => {
      void loadFieldDictOptions();
    },
    { immediate: true }
  );

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
    const id = deps.readValue(row, deps.idKey());
    if (id == null) return;
    try {
      const detail = await detailAdminRecord(`${deps.baseEndpoint()}/${deps.detailAction()}`, deps.idKey(), id);
      fillForm(deps.flattenRecord(detail ?? row));
    } catch {
      fillForm(row);
    }
  }

  async function submitForm() {
    const invalidLabel = firstInvalidField();
    if (invalidLabel) {
      throw new Error(`请填写${invalidLabel}`);
    }
    saving.value = true;
    try {
      const payload = normalizePayload(formModel);
      if (mode.value === 'create') {
        await createAdminRecord(`${deps.baseEndpoint()}/create`, payload);
      } else {
        await updateAdminRecord(`${deps.baseEndpoint()}/update`, payload);
      }
      closeDrawer();
      deps.showToast(`${pageTitle.value}已保存`);
      await deps.loadData();
    } finally {
      saving.value = false;
    }
  }

  function closeDrawer() {
    drawerOpen.value = false;
  }

  function fillForm(record: AdminRecord) {
    Object.keys(formModel).forEach((key) => delete formModel[key]);
    const flat = deps.flattenRecord(record);
    configuredFormFields.value.forEach((field) => {
      formModel[field.key] = toFormValue(deps.readByAliases(flat, field.key, field.aliases));
    });
    const id = deps.readValue(record, deps.idKey());
    if (mode.value !== 'create' && id != null) {
      formModel[deps.idKey()] = toFormValue(id);
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

  function toFormValue(value: unknown) {
    if (value == null) return '';
    if (typeof value === 'number' || typeof value === 'string') return value;
    if (typeof value === 'boolean') return String(value);
    return JSON.stringify(value);
  }

  async function loadFieldDictOptions() {
    const dictCodes = Array.from(new Set(configuredFormFields.value
      .map((field) => field.dictCode)
      .filter((dictCode): dictCode is string => Boolean(dictCode))));
    if (dictCodes.length === 0) {
      fieldDictOptions.value = {};
      return;
    }
    const entries = await Promise.all(dictCodes.map(async (dictCode) => [dictCode, await loadDictOptions(dictCode)] as const));
    fieldDictOptions.value = Object.fromEntries(entries);
  }

  async function loadDictOptions(dictCode: string): Promise<FormOption[]> {
    try {
      const dictPage = await pageDicts({ dictCode, pageNum: 1, pageSize: 1 });
      const dict = dictPage.list?.[0];
      if (!dict?.dictId) return [];
      const dataPage = await pageDictData({ dictId: dict.dictId, disabledFlag: false, pageNum: 1, pageSize: 500 });
      return (dataPage.list ?? []).map((item) => ({
        label: item.dataLabel || item.dataValue || '',
        value: item.dataValue || ''
      }));
    } catch {
      return [];
    }
  }

  return {
    saving,
    drawerOpen,
    mode,
    formModel,
    formFields,
    canCreate,
    canUpdate,
    drawerTitle,
    openCreate,
    openEdit,
    openDetail,
    submitForm,
    closeDrawer
  };
}
