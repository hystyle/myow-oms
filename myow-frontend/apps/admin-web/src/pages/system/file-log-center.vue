<template>
  <section class="page-stack">
    <header class="page-heading">
      <div><h1>文件与日志</h1><p>查看上传文件、操作日志、登录日志和在线会话。</p></div>
      <button v-if="current === '文件管理'" class="primary-action" type="button" @click="openUpload">上传文件</button>
    </header>

    <div v-if="toastMessage" class="success-banner">{{ toastMessage }}</div>
    <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>

    <div class="tabs">
      <button v-for="tab in tabs" :key="tab" :class="{ active: current === tab }" type="button" @click="selectTab(tab)">{{ tab }}</button>
    </div>

    <section class="toolbar query-panel">
      <label><span>关键词</span><input v-model="query.keyword" placeholder="文件名、模块、账号、Token" @keyup.enter="loadData" /></label>
      <div class="query-actions"><button type="button" :disabled="loading" @click="loadData">查询</button><button type="button" :disabled="loading" @click="resetQuery">重置</button></div>
    </section>

    <article class="panel table-panel">
      <div class="panel__head"><div><h2>{{ current }}</h2><p>{{ currentHint }}</p></div><span class="page-status">{{ total }} 条</span></div>
      <table class="data-table">
        <thead><tr><th>对象</th><th>类型</th><th>结果</th><th>说明</th><th>时间</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="row in rows" :key="row.id">
            <td><code>{{ row.code }}</code></td>
            <td>{{ row.name }}</td>
            <td><span class="status-tag" :data-tone="row.status === 1 ? 'green' : 'red'">{{ row.status === 1 ? '可用' : '不可用' }}</span></td>
            <td>{{ note(row) }}</td>
            <td>{{ formatTime(row.createTime || row.updateTime) }}</td>
            <td class="table-actions">
              <button v-if="current === '文件管理'" type="button" @click="download(row)">下载</button>
              <button v-if="current === '文件管理'" type="button" @click="remove(row)">删除</button>
              <button v-if="current === '在线用户'" type="button">强退</button>
            </td>
          </tr>
          <tr v-if="!loading && rows.length === 0"><td class="empty-cell" colspan="6">{{ emptyText }}</td></tr>
          <tr v-if="loading"><td class="empty-cell" colspan="6">加载中...</td></tr>
        </tbody>
      </table>
      <footer class="pagination-bar">
        <span>第 {{ query.pageNum }} / {{ pageCount }} 页</span>
        <button type="button" :disabled="query.pageNum <= 1" @click="changePage(query.pageNum - 1)">上一页</button>
        <button type="button" :disabled="query.pageNum >= pageCount" @click="changePage(query.pageNum + 1)">下一页</button>
      </footer>
    </article>

    <div v-if="uploadOpen" class="crud-backdrop" @click.self="closeUpload">
      <section class="crud-drawer">
        <header class="crud-drawer__head"><div><h2>上传文件</h2><p>上传后会写入文件元数据，可在列表下载。</p></div><button type="button" @click="closeUpload">关闭</button></header>
        <form class="crud-form" @submit.prevent="submitUpload">
          <label><span>业务模块</span><input v-model="uploadForm.moduleName" required placeholder="system / customer / overseas" /></label>
          <label><span>文件</span><input ref="fileInput" type="file" required /></label>
          <footer class="crud-actions"><button type="button" @click="closeUpload">取消</button><button class="primary-action" type="submit" :disabled="saving">{{ saving ? '上传中...' : '上传' }}</button></footer>
        </form>
      </section>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type { SystemRecord } from '@myow/api';
import { deleteFile, downloadFile, pageFiles, pageOnlineUsers, uploadFile } from '@/services/system-service';

const tabs = ['文件管理', '操作日志', '登录日志', '在线用户'];
const current = ref(tabs[0]);
const loading = ref(false);
const saving = ref(false);
const uploadOpen = ref(false);
const rows = ref<SystemRecord[]>([]);
const total = ref(0);
const toastMessage = ref('');
const errorMessage = ref('');
const fileInput = ref<HTMLInputElement | null>(null);
const query = reactive({ keyword: '', pageNum: 1, pageSize: 20 });
const uploadForm = reactive({ moduleName: 'system' });

const pageCount = computed(() => Math.max(1, Math.ceil(total.value / query.pageSize)));
const currentHint = computed(() => {
  if (current.value === '文件管理') return '文件元数据来自 system file 接口。';
  if (current.value === '在线用户') return '在线会话来自 system online-user 接口。';
  return '该标签后续将接入 user 域日志接口。';
});
const emptyText = computed(() => current.value === '操作日志' || current.value === '登录日志' ? '该日志入口待接入 user 域接口' : '暂无数据');

onMounted(loadData);

async function loadData() {
  await runWithFeedback(async () => {
    loading.value = true;
    if (current.value === '文件管理') {
      const result = await pageFiles(query);
      rows.value = result.list ?? [];
      total.value = Number(result.total ?? rows.value.length);
      return;
    }
    if (current.value === '在线用户') {
      const result = await pageOnlineUsers(query);
      rows.value = result.list ?? [];
      total.value = Number(result.total ?? rows.value.length);
      return;
    }
    rows.value = [];
    total.value = 0;
  }, false);
  loading.value = false;
}

function selectTab(tab: string) {
  current.value = tab;
  query.pageNum = 1;
  void loadData();
}

function resetQuery() {
  query.keyword = '';
  query.pageNum = 1;
  void loadData();
}

function changePage(pageNum: number) {
  query.pageNum = pageNum;
  void loadData();
}

function openUpload() {
  uploadOpen.value = true;
}

function closeUpload() {
  uploadOpen.value = false;
  if (fileInput.value) fileInput.value.value = '';
}

async function submitUpload() {
  const file = fileInput.value?.files?.[0];
  if (!file) {
    errorMessage.value = '请选择文件';
    return;
  }
  await runWithFeedback(async () => {
    saving.value = true;
    await uploadFile(file, uploadForm.moduleName);
    showToast('文件已上传');
    closeUpload();
    await loadData();
  });
  saving.value = false;
}

async function download(row: SystemRecord) {
  await runWithFeedback(async () => {
    const result = await downloadFile(row.id);
    const url = URL.createObjectURL(result.blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = result.fileName || String(row.code ?? 'download');
    link.click();
    URL.revokeObjectURL(url);
  });
}

async function remove(row: SystemRecord) {
  await runWithFeedback(async () => {
    await deleteFile(row.id);
    showToast('文件已删除');
    await loadData();
  });
}

async function runWithFeedback(task: () => Promise<void>, clearToast = true) {
  if (clearToast) toastMessage.value = '';
  errorMessage.value = '';
  try {
    await task();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '操作失败';
  }
}

function showToast(message: string) {
  toastMessage.value = message;
}

function attr(record: SystemRecord, key: string) {
  return record.attributes?.[key];
}

function note(row: SystemRecord) {
  if (current.value === '文件管理') {
    const size = Number(attr(row, 'fileSize') ?? 0);
    return `${attr(row, 'contentType') || '-'} / ${formatSize(size)}`;
  }
  return String(attr(row, 'message') ?? '-');
}

function formatSize(size: number) {
  if (size >= 1024 * 1024) return `${(size / 1024 / 1024).toFixed(2)} MB`;
  if (size >= 1024) return `${(size / 1024).toFixed(2)} KB`;
  return `${size} B`;
}

function formatTime(value?: string) {
  return value ? value.replace('T', ' ').slice(0, 19) : '-';
}
</script>
