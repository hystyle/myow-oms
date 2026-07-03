<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>定时任务</h1>
        <p>维护调度任务、Cron 表达式、处理器和最近执行结果。</p>
      </div>
      <button class="primary-action" type="button" @click="openCreate">新增任务</button>
    </header>

    <div v-if="toastMessage" class="success-banner">{{ toastMessage }}</div>
    <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>

    <section class="toolbar query-panel">
      <label><span>关键词</span><input v-model="query.keyword" placeholder="任务名称、任务组、处理器" @keyup.enter="loadJobs" /></label>
      <label>
        <span>状态</span>
        <select v-model="query.status">
          <option value="">全部状态</option>
          <option value="1">启用</option>
          <option value="0">暂停</option>
        </select>
      </label>
      <div class="query-actions">
        <button type="button" :disabled="loading" @click="loadJobs">查询</button>
        <button type="button" :disabled="loading" @click="resetQuery">重置</button>
      </div>
    </section>

    <section class="workspace-grid">
      <article class="panel table-panel">
        <div class="panel__head">
          <div><h2>任务列表</h2><p>支持暂停、恢复、立即执行和查看日志。</p></div>
          <span class="page-status">{{ total }} 个任务</span>
        </div>
        <table class="data-table dense-table">
          <thead>
            <tr><th>任务名称</th><th>任务组</th><th>Cron</th><th>处理器</th><th>状态</th><th>更新时间</th><th>操作</th></tr>
          </thead>
          <tbody>
            <tr v-for="job in jobs" :key="job.id" :class="{ 'selected-row': selectedJob?.id === job.id }">
              <td><strong>{{ job.code }}</strong></td>
              <td>{{ job.name }}</td>
              <td><code>{{ attr(job, 'cronExpression') }}</code></td>
              <td><code>{{ attr(job, 'handlerName') }}</code></td>
              <td><span class="status-tag" :data-tone="job.status === 1 ? 'green' : 'amber'">{{ job.status === 1 ? '启用' : '暂停' }}</span></td>
              <td>{{ formatTime(job.updateTime || job.createTime) }}</td>
              <td class="table-actions">
                <button type="button" @click="selectJob(job)">日志</button>
                <button type="button" @click="openEdit(job)">编辑</button>
                <button type="button" @click="run(job)">执行一次</button>
                <button type="button" @click="toggleStatus(job)">{{ job.status === 1 ? '暂停' : '恢复' }}</button>
                <button type="button" @click="remove(job)">删除</button>
              </td>
            </tr>
            <tr v-if="!loading && jobs.length === 0"><td class="empty-cell" colspan="7">暂无定时任务</td></tr>
            <tr v-if="loading"><td class="empty-cell" colspan="7">加载中...</td></tr>
          </tbody>
        </table>
        <footer class="pagination-bar">
          <span>第 {{ query.pageNum }} / {{ pageCount }} 页</span>
          <select v-model.number="query.pageSize" @change="loadJobs"><option :value="10">10 条/页</option><option :value="20">20 条/页</option><option :value="50">50 条/页</option></select>
          <button type="button" :disabled="query.pageNum <= 1" @click="changePage(query.pageNum - 1)">上一页</button>
          <button type="button" :disabled="query.pageNum >= pageCount" @click="changePage(query.pageNum + 1)">下一页</button>
        </footer>
      </article>

      <article class="panel">
        <div class="panel__head">
          <div><h2>执行日志</h2><p>{{ selectedJob?.code || '选择任务' }} 最近执行记录。</p></div>
          <span class="status-tag" data-tone="blue">最近记录</span>
        </div>
        <div class="timeline-list">
          <button v-for="log in logs" :key="log.id" class="log-item" type="button" @click="selectedLog = log">
            <span>{{ formatTime(log.createTime) }}</span>
            <strong><span class="status-tag" :data-tone="log.status === 1 ? 'green' : 'red'">{{ log.status === 1 ? '成功' : '失败' }}</span></strong>
          </button>
          <p v-if="logs.length === 0" class="empty-cell">暂无执行日志</p>
        </div>
        <dl v-if="selectedLog" class="detail-grid log-detail">
          <div><dt>任务组</dt><dd>{{ selectedLog.name }}</dd></div>
          <div><dt>结束时间</dt><dd>{{ formatTime(selectedLog.updateTime) }}</dd></div>
          <div class="form-wide"><dt>失败原因 / 返回</dt><dd>{{ attr(selectedLog, 'errorMsg') || '执行成功' }}</dd></div>
        </dl>
      </article>
    </section>

    <div v-if="drawerOpen" class="crud-backdrop" @click.self="closeDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div><h2>{{ editingId ? '编辑任务' : '新增任务' }}</h2><p>Cron 保存前应确认处理器 Bean 已存在。</p></div>
          <button type="button" @click="closeDrawer">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submit">
          <label><span>任务名称</span><input v-model="form.jobName" required /></label>
          <label><span>任务组</span><input v-model="form.jobGroup" required /></label>
          <label><span>Cron 表达式</span><input v-model="form.cronExpression" required placeholder="0 */5 * * * ?" /></label>
          <label><span>处理器 Bean</span><input v-model="form.handlerName" required /></label>
          <label v-if="editingId"><span>状态</span><select v-model.number="form.status"><option :value="1">启用</option><option :value="0">暂停</option></select></label>
          <footer class="crud-actions">
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
import { createJob, deleteJob, pageJobLogs, pageJobs, pauseJob, resumeJob, runJob, updateJob } from '@/services/system-service';

const loading = ref(false);
const saving = ref(false);
const drawerOpen = ref(false);
const editingId = ref('');
const jobs = ref<SystemRecord[]>([]);
const logs = ref<SystemRecord[]>([]);
const selectedJob = ref<SystemRecord | null>(null);
const selectedLog = ref<SystemRecord | null>(null);
const total = ref(0);
const toastMessage = ref('');
const errorMessage = ref('');
const query = reactive({ keyword: '', status: '', pageNum: 1, pageSize: 10 });
const form = reactive({ jobName: '', jobGroup: '', cronExpression: '', handlerName: '', status: 1 });

const pageCount = computed(() => Math.max(1, Math.ceil(total.value / query.pageSize)));

onMounted(loadJobs);

async function loadJobs() {
  await runWithFeedback(async () => {
    loading.value = true;
    const result = await pageJobs({ ...query, status: query.status || undefined });
    jobs.value = result.list ?? [];
    total.value = Number(result.total ?? jobs.value.length);
    if (!selectedJob.value || !jobs.value.some((item) => item.id === selectedJob.value?.id)) {
      await selectJob(jobs.value[0] ?? null);
    }
  }, false);
  loading.value = false;
}

async function selectJob(job: SystemRecord | null) {
  selectedJob.value = job;
  selectedLog.value = null;
  if (!job) {
    logs.value = [];
    return;
  }
  const result = await pageJobLogs({ pageNum: 1, pageSize: 10 });
  logs.value = (result.list ?? []).filter((item) => String(attr(item, 'jobId') ?? '') === String(job.id));
  selectedLog.value = logs.value[0] ?? null;
}

function resetQuery() {
  query.keyword = '';
  query.status = '';
  query.pageNum = 1;
  void loadJobs();
}

function changePage(pageNum: number) {
  query.pageNum = pageNum;
  void loadJobs();
}

function openCreate() {
  editingId.value = '';
  Object.assign(form, { jobName: '', jobGroup: '', cronExpression: '', handlerName: '', status: 1 });
  drawerOpen.value = true;
}

function openEdit(job: SystemRecord) {
  editingId.value = String(job.id);
  Object.assign(form, {
    jobName: job.code ?? '',
    jobGroup: job.name ?? '',
    cronExpression: String(attr(job, 'cronExpression') ?? ''),
    handlerName: String(attr(job, 'handlerName') ?? ''),
    status: Number(job.status ?? 1)
  });
  drawerOpen.value = true;
}

function closeDrawer() {
  drawerOpen.value = false;
}

async function submit() {
  await runWithFeedback(async () => {
    saving.value = true;
    if (editingId.value) {
      await updateJob({ id: editingId.value, ...form });
      showToast('任务已更新');
    } else {
      await createJob(form);
      showToast('任务已创建');
    }
    closeDrawer();
    await loadJobs();
  });
  saving.value = false;
}

async function run(job: SystemRecord) {
  if (!confirmImportantAction({
    title: `立即执行任务 ${job.code || job.name || job.id}`,
    risk: '立即执行会触发后端任务处理器，可能产生数据清理、通知或同步动作。',
    confirmText: '确认立即执行该任务？'
  })) return;
  await runWithFeedback(async () => {
    await runJob(job.id);
    showToast('任务已触发执行');
    await selectJob(job);
  });
}

async function toggleStatus(job: SystemRecord) {
  const action = job.status === 1 ? '暂停' : '恢复';
  if (!confirmImportantAction({
    title: `${action}任务 ${job.code || job.name || job.id}`,
    risk: '任务状态变更会影响后续自动调度，请确认当前操作符合运维预期。',
    confirmText: `确认${action}该任务？`
  })) return;
  await runWithFeedback(async () => {
    if (job.status === 1) {
      await pauseJob(job.id);
      showToast('任务已暂停');
    } else {
      await resumeJob(job.id);
      showToast('任务已恢复');
    }
    await loadJobs();
  });
}

async function remove(job: SystemRecord) {
  if (!confirmDelete(`任务 ${job.code || job.name || job.id}`, '删除任务会移除调度配置，后续不会再自动执行。历史执行日志仍可能保留。')) return;
  await runWithFeedback(async () => {
    await deleteJob(job.id);
    showToast('任务已删除');
    await loadJobs();
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

function formatTime(value?: string) {
  return value ? value.replace('T', ' ').slice(0, 19) : '-';
}
</script>
