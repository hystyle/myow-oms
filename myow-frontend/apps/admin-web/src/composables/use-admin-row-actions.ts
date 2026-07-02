import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import { usePermission } from '@/composables/use-permission';
import {
  createAdminRecord,
  deleteAdminRecord,
  downloadAdminRecord,
  type AdminRecord
} from '@/services/admin-data-service';
import type { RowAction } from './admin-crud-types';

// 行操作：负责删除、自定义行按钮执行、下载、结果抽屉展示
export function useAdminRowActions(deps: {
  baseEndpoint: () => string;
  idKey: () => string;
  readValue: (row: AdminRecord, key: string) => unknown;
  flattenRecord: (row: AdminRecord) => AdminRecord;
  showToast: (message: string) => void;
  loadData: () => Promise<void>;
}) {
  const route = useRoute();
  const { hasPermission } = usePermission();
  const actionResultOpen = ref(false);
  const actionResultTitle = ref('');
  const actionResultText = ref('');
  const errorMessage = ref('');

  const rowActions = computed<RowAction[]>(() =>
    (route.meta.rowActions as RowAction[] | undefined ?? []).filter((action) => hasPermission(action.permission))
  );
  const hasRowActions = computed(() => {
    const canDetail = route.meta.canDetail !== false;
    const canUpdate = route.meta.canUpdate !== false && hasPermission(String(route.meta.updatePerm ?? ''));
    const canDelete = route.meta.canDelete !== false && hasPermission(String(route.meta.deletePerm ?? ''));
    return canDetail || canUpdate || canDelete || rowActions.value.length > 0;
  });

  async function handleDelete(row: AdminRecord) {
    const id = deps.readValue(row, deps.idKey());
    if (id == null) return;
    const pageTitle = String(route.meta.title ?? '数据');
    if (!window.confirm(`确认删除这条${pageTitle}数据？`)) return;
    errorMessage.value = '';
    try {
      await deleteAdminRecord(`${deps.baseEndpoint()}/delete`, deps.idKey(), id);
      deps.showToast(`${pageTitle}已删除`);
      await deps.loadData();
    } catch (error) {
      errorMessage.value = error instanceof Error ? error.message : `${pageTitle}删除失败`;
    }
  }

  async function handleRowAction(action: RowAction, row: AdminRecord) {
    if (action.confirm && !window.confirm(action.confirm)) {
      return;
    }
    const actionIdKey = action.idKey ?? deps.idKey();
    const payloadKey = action.payloadKey ?? actionIdKey;
    const id = deps.readValue(row, actionIdKey);
    errorMessage.value = '';
    try {
      if (action.resultMode === 'download') {
        const result = await downloadAdminRecord(action.endpoint, payloadKey, id);
        downloadBlob(result.blob, result.fileName);
        deps.showToast(action.success ?? `${action.label}已开始`);
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
        await deps.loadData();
      }
    } catch (error) {
      errorMessage.value = error instanceof Error ? error.message : `${action.label}失败`;
    }
  }

  function handleActionResult(action: RowAction, result: unknown) {
    if (action.resultMode === 'open-url') {
      const url = deps.readValue(deps.flattenRecord(result as AdminRecord), action.urlKey ?? 'url');
      if (typeof url === 'string' && url) {
        window.open(url, '_blank', 'noopener,noreferrer');
        deps.showToast(action.success ?? `${action.label}已打开`);
        return;
      }
    }
    if (action.resultMode === 'drawer') {
      actionResultTitle.value = action.label;
      actionResultText.value = JSON.stringify(result ?? {}, null, 2);
      actionResultOpen.value = true;
      return;
    }
    deps.showToast(action.success ?? `${action.label}已完成`);
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

  function closeActionResult() {
    actionResultOpen.value = false;
  }

  return {
    actionResultOpen,
    actionResultTitle,
    actionResultText,
    errorMessage,
    rowActions,
    hasRowActions,
    handleDelete,
    handleRowAction,
    closeActionResult
  };
}
