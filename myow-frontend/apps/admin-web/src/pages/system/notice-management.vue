<template>
  <section class="page-stack">
    <header class="page-heading">
      <div><h1>通知公告</h1><p>维护草稿、发布、下线和有效期，可预留客户可见范围。</p></div>
      <button class="primary-action" type="button" @click="openCreate">新增公告</button>
    </header>

    <div v-if="toastMessage" class="success-banner">{{ toastMessage }}</div>
    <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>

    <section class="toolbar query-panel">
      <label><span>标题</span><input v-model="query.keyword" placeholder="公告标题" @keyup.enter="loadNotices" /></label>
      <label>
        <span>状态</span>
        <select v-model="query.status">
          <option value="">全部状态</option>
          <option v-for="option in noticeStatusOptions" :key="String(option.value)" :value="option.value">{{ option.label }}</option>
        </select>
      </label>
      <div class="query-actions">
        <button type="button" :disabled="loading" @click="loadNotices">查询</button>
        <button type="button" :disabled="loading" @click="resetQuery">重置</button>
      </div>
    </section>

    <article class="panel table-panel">
      <div class="panel__head"><div><h2>公告列表</h2><p>状态流：草稿 -> 已发布 -> 已下线。</p></div><span class="page-status">{{ total }} 条</span></div>
      <table class="data-table">
        <thead><tr><th>标题</th><th>类型</th><th>状态</th><th>发布时间</th><th>有效期</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="row in rows" :key="row.id">
            <td><strong>{{ row.code }}</strong></td>
            <td>{{ attr(row, 'noticeType') || '-' }}</td>
            <td><span class="status-tag" :data-tone="statusTone(row.status)">{{ statusText(row.status) }}</span></td>
            <td>{{ formatTime(attr(row, 'publishTime')) }}</td>
            <td>{{ formatTime(attr(row, 'expireTime')) }}</td>
            <td class="table-actions">
              <button type="button" @click="openDetail(row)">详情</button>
              <button v-if="row.status === 0" type="button" @click="openEdit(row)">编辑</button>
              <button v-if="row.status === 0" type="button" @click="publish(row)">发布</button>
              <button v-if="row.status === 1" type="button" @click="withdraw(row)">下线</button>
              <button type="button" @click="remove(row)">删除</button>
            </td>
          </tr>
          <tr v-if="!loading && rows.length === 0"><td class="empty-cell" colspan="6">暂无公告</td></tr>
          <tr v-if="loading"><td class="empty-cell" colspan="6">加载中...</td></tr>
        </tbody>
      </table>
      <footer class="pagination-bar">
        <span>第 {{ query.pageNum }} / {{ pageCount }} 页</span>
        <select v-model.number="query.pageSize" @change="loadNotices"><option :value="10">10 条/页</option><option :value="20">20 条/页</option><option :value="50">50 条/页</option></select>
        <button type="button" :disabled="query.pageNum <= 1" @click="changePage(query.pageNum - 1)">上一页</button>
        <button type="button" :disabled="query.pageNum >= pageCount" @click="changePage(query.pageNum + 1)">下一页</button>
      </footer>
    </article>

    <div v-if="drawerOpen" class="crud-backdrop" @click.self="closeDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div><h2>{{ drawerTitle }}</h2><p>发布设置、可见范围和正文内容。</p></div>
          <button type="button" @click="closeDrawer">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submit">
          <label><span>标题</span><input v-model="form.title" :disabled="readonly" required /></label>
          <label>
            <span>类型</span>
            <select v-model="form.noticeType" :disabled="readonly">
              <option v-for="option in noticeTypeOptions" :key="String(option.value)" :value="option.value">{{ option.label }}</option>
            </select>
          </label>
          <label><span>有效期</span><input v-model="form.expireTime" :disabled="readonly" type="datetime-local" /></label>
          <label><span>状态</span><input :value="editingRecord ? statusText(editingRecord.status) : '草稿'" disabled /></label>
          <label class="form-wide"><span>公告内容</span><textarea v-model="form.content" :disabled="readonly" rows="8" required /></label>
          <footer v-if="!readonly" class="crud-actions">
            <button type="button" @click="closeDrawer">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中...' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type { SystemRecord } from '@myow/api';
import { confirmDelete, confirmImportantAction } from '@/composables/use-confirm-action';
import { useDictOptions } from '@/composables/use-dict-options';
import { createNotice, deleteNotice, pageNotices, publishNotice, updateNotice, withdrawNotice } from '@/services/system-service';

const loading = ref(false);
const saving = ref(false);
const drawerOpen = ref(false);
const readonly = ref(false);
const rows = ref<SystemRecord[]>([]);
const total = ref(0);
const toastMessage = ref('');
const errorMessage = ref('');
const editingRecord = ref<SystemRecord | null>(null);
const query = reactive({ keyword: '', status: '', pageNum: 1, pageSize: 10 });
const form = reactive({ title: '', content: '', noticeType: 'SYSTEM', expireTime: '' });
const { options: noticeStatusOptions } = useDictOptions('sys_notice_status', [
  { label: '草稿', value: '0' },
  { label: '已发布', value: '1' },
  { label: '已下线', value: '2' }
]);
const { options: noticeTypeOptions } = useDictOptions('sys_notice_type', [
  { label: '系统公告', value: 'SYSTEM' },
  { label: '客户公告', value: 'CUSTOMER' }
]);

const pageCount = computed(() => Math.max(1, Math.ceil(total.value / query.pageSize)));
const drawerTitle = computed(() => {
  if (readonly.value) return '公告详情';
  return editingRecord.value ? '公告编辑' : '新增公告';
});

onMounted(loadNotices);

async function loadNotices() {
  await runWithFeedback(async () => {
    loading.value = true;
    const result = await pageNotices({ ...query, status: query.status || undefined });
    rows.value = result.list ?? [];
    total.value = Number(result.total ?? rows.value.length);
  }, false);
  loading.value = false;
}

function resetQuery() {
  query.keyword = '';
  query.status = '';
  query.pageNum = 1;
  void loadNotices();
}

function changePage(pageNum: number) {
  query.pageNum = pageNum;
  void loadNotices();
}

function openCreate() {
  readonly.value = false;
  editingRecord.value = null;
  Object.assign(form, { title: '', content: '', noticeType: 'SYSTEM', expireTime: '' });
  drawerOpen.value = true;
}

function openEdit(row: SystemRecord) {
  readonly.value = false;
  editingRecord.value = row;
  fillForm(row);
  drawerOpen.value = true;
}

function openDetail(row: SystemRecord) {
  readonly.value = true;
  editingRecord.value = row;
  fillForm(row);
  drawerOpen.value = true;
}

function closeDrawer() {
  drawerOpen.value = false;
}

async function submit() {
  await runWithFeedback(async () => {
    saving.value = true;
    const payload = {
      title: form.title,
      content: form.content,
      noticeType: form.noticeType,
      expireTime: form.expireTime ? `${form.expireTime}:00` : undefined
    };
    if (editingRecord.value) {
      await updateNotice({ id: editingRecord.value.id, ...payload });
      showToast('公告已更新');
    } else {
      await createNotice(payload);
      showToast('公告已创建');
    }
    closeDrawer();
    await loadNotices();
  });
  saving.value = false;
}

async function publish(row: SystemRecord) {
  if (!confirmImportantAction({
    title: `发布公告 ${row.code || row.id}`,
    risk: '发布后公告将对目标范围可见，内容通常不应再直接修改。',
    confirmText: '确认发布该公告？'
  })) return;
  await runWithFeedback(async () => {
    await publishNotice(row.id);
    showToast('公告已发布');
    await loadNotices();
  });
}

async function withdraw(row: SystemRecord) {
  if (!confirmImportantAction({
    title: `下线公告 ${row.code || row.id}`,
    risk: '下线后普通用户将不再看到该公告，可能影响通知触达。',
    confirmText: '确认下线该公告？'
  })) return;
  await runWithFeedback(async () => {
    await withdrawNotice(row.id);
    showToast('公告已下线');
    await loadNotices();
  });
}

async function remove(row: SystemRecord) {
  if (!confirmDelete(`公告 ${row.code || row.id}`, '删除公告会移除管理端记录入口。已发布公告建议优先下线，而不是直接删除。')) return;
  await runWithFeedback(async () => {
    await deleteNotice(row.id);
    showToast('公告已删除');
    await loadNotices();
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

function fillForm(row: SystemRecord) {
  Object.assign(form, {
    title: row.code ?? '',
    content: String(attr(row, 'content') ?? ''),
    noticeType: String(attr(row, 'noticeType') ?? 'SYSTEM'),
    expireTime: toDatetimeLocal(attr(row, 'expireTime'))
  });
}

function showToast(message: string) {
  toastMessage.value = message;
}

function attr(record: SystemRecord, key: string) {
  return record.attributes?.[key];
}

function statusText(status?: string | number | boolean) {
  if (status === 1) return '已发布';
  if (status === 2) return '已下线';
  return '草稿';
}

function statusTone(status?: string | number | boolean) {
  if (status === 1) return 'green';
  if (status === 2) return 'red';
  return 'amber';
}

function formatTime(value: unknown) {
  return typeof value === 'string' && value ? value.replace('T', ' ').slice(0, 19) : '-';
}

function toDatetimeLocal(value: unknown) {
  return typeof value === 'string' && value ? value.slice(0, 16) : '';
}
</script>
