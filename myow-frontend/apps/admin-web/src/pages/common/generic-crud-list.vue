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
              <span v-if="column.key === 'status'" class="status-tag" :data-tone="statusTone(readColumnValue(row, column))">
                {{ statusText(readColumnValue(row, column)) }}
              </span>
              <code v-else-if="column.code">{{ formatValue(readColumnValue(row, column)) }}</code>
              <span v-else>{{ formatValue(readColumnValue(row, column)) }}</span>
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
import { useAdminCrud } from '@/composables/use-admin-crud';

const {
  loading,
  saving,
  errorMessage,
  toastMessage,
  actionResultOpen,
  actionResultTitle,
  actionResultText,
  rows,
  total,
  drawerOpen,
  mode,
  formModel,
  query,
  endpoint,
  pageTitle,
  pageDescription,
  listTitle,
  searchPlaceholder,
  columns,
  formFields,
  statusOptions,
  rowActions,
  canCreate,
  canUpdate,
  canDelete,
  canDetail,
  hasRowActions,
  pageCount,
  drawerTitle,
  loadData,
  resetQuery,
  changePage,
  changePageSize,
  openCreate,
  openEdit,
  openDetail,
  submitForm,
  handleDelete,
  handleRowAction,
  closeDrawer,
  closeActionResult,
  rowKey,
  readColumnValue,
  statusText,
  statusTone,
  formatValue
} = useAdminCrud();
</script>
