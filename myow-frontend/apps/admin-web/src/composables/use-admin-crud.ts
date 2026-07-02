import { useAdminList } from './use-admin-list';
import { useAdminFormDrawer } from './use-admin-form-drawer';
import { useAdminRowActions } from './use-admin-row-actions';

// 聚合编排：组合 list/form-drawer/row-actions 三个单一职责 composable
export function useAdminCrud() {
  const list = useAdminList();

  const formDrawer = useAdminFormDrawer({
    baseEndpoint: () => list.baseEndpoint.value,
    detailAction: () => list.detailAction.value,
    idKey: () => list.idKey.value,
    readValue: list.readValue,
    readByAliases: list.readByAliases,
    flattenRecord: list.flattenRecord,
    showToast: list.showToast,
    loadData: list.loadData
  });

  const rowActions = useAdminRowActions({
    baseEndpoint: () => list.baseEndpoint.value,
    idKey: () => list.idKey.value,
    readValue: list.readValue,
    flattenRecord: list.flattenRecord,
    showToast: list.showToast,
    loadData: list.loadData
  });

  return {
    // list
    loading: list.loading,
    errorMessage: list.errorMessage,
    toastMessage: list.toastMessage,
    rows: list.rows,
    total: list.total,
    query: list.query,
    endpoint: list.endpoint,
    pageTitle: list.pageTitle,
    pageDescription: list.pageDescription,
    listTitle: list.listTitle,
    searchPlaceholder: list.searchPlaceholder,
    columns: list.columns,
    statusOptions: list.statusOptions,
    canDetail: list.canDetail,
    canDelete: list.canDelete,
    pageCount: list.pageCount,
    loadData: list.loadData,
    resetQuery: list.resetQuery,
    changePage: list.changePage,
    changePageSize: list.changePageSize,
    rowKey: list.rowKey,
    readColumnValue: list.readColumnValue,
    statusText: list.statusText,
    statusTone: list.statusTone,
    formatValue: list.formatValue,

    // form drawer
    saving: formDrawer.saving,
    drawerOpen: formDrawer.drawerOpen,
    mode: formDrawer.mode,
    formModel: formDrawer.formModel,
    formFields: formDrawer.formFields,
    canCreate: formDrawer.canCreate,
    canUpdate: formDrawer.canUpdate,
    drawerTitle: formDrawer.drawerTitle,
    openCreate: formDrawer.openCreate,
    openEdit: formDrawer.openEdit,
    openDetail: formDrawer.openDetail,
    submitForm: formDrawer.submitForm,
    closeDrawer: formDrawer.closeDrawer,

    // row actions
    actionResultOpen: rowActions.actionResultOpen,
    actionResultTitle: rowActions.actionResultTitle,
    actionResultText: rowActions.actionResultText,
    rowActions: rowActions.rowActions,
    hasRowActions: rowActions.hasRowActions,
    handleDelete: rowActions.handleDelete,
    handleRowAction: rowActions.handleRowAction,
    closeActionResult: rowActions.closeActionResult
  };
}
