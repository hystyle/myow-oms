<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>系统中心</h1>
        <p>维护系统运行支撑能力：定时任务、通知公告、配置、文件和导出。</p>
      </div>
      <button class="primary-action" type="button" @click="loadAll">刷新数据</button>
    </header>

    <div v-if="errorMessage" class="error-banner">
      {{ errorMessage }}
    </div>

    <div class="system-grid">
      <article v-for="item in modules" :key="item.title" class="system-card">
        <div>
          <h2>{{ item.title }}</h2>
          <p>{{ item.description }}</p>
        </div>
        <span class="status-tag" :data-tone="item.tone">{{ item.status }}</span>
      </article>
    </div>

    <article class="panel">
      <div class="panel__head">
        <div>
          <h2>定时任务</h2>
          <p>来自 JobController，可执行、暂停、恢复。</p>
        </div>
        <span class="page-status">{{ loading ? '加载中' : `${jobTotal} 条` }}</span>
      </div>
      <table class="data-table">
        <thead>
          <tr>
            <th>任务名称</th>
            <th>任务组</th>
            <th>Cron</th>
            <th>处理器</th>
            <th>状态</th>
            <th>更新时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="job in jobs" :key="job.id">
            <td>{{ job.name || '-' }}</td>
            <td>{{ attr(job, 'jobGroup') || job.code || '-' }}</td>
            <td><code>{{ attr(job, 'cronExpression') || '-' }}</code></td>
            <td><code>{{ attr(job, 'handlerName') || '-' }}</code></td>
            <td><span class="status-tag" :data-tone="statusTone(job.status)">{{ statusText(job.status) }}</span></td>
            <td>{{ formatTime(job.updateTime || job.createTime) }}</td>
            <td class="table-actions">
              <button type="button" @click="handleRun(job.id)">执行一次</button>
              <button v-if="job.status === 1" type="button" @click="handlePause(job.id)">暂停</button>
              <button v-else type="button" @click="handleResume(job.id)">恢复</button>
            </td>
          </tr>
          <tr v-if="!loading && jobs.length === 0">
            <td colspan="7" class="empty-cell">暂无定时任务</td>
          </tr>
        </tbody>
      </table>
    </article>

    <section class="workspace-grid">
      <article class="panel">
        <div class="panel__head">
          <div>
            <h2>通知公告</h2>
            <p>草稿、发布、下线和有效期。</p>
          </div>
          <span class="page-status">{{ noticeTotal }} 条</span>
        </div>
        <table class="data-table">
          <thead>
            <tr>
              <th>标题</th>
              <th>类型</th>
              <th>状态</th>
              <th>更新时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="notice in notices" :key="notice.id">
              <td>{{ notice.name || '-' }}</td>
              <td>{{ attr(notice, 'noticeType') || '-' }}</td>
              <td><span class="status-tag" :data-tone="noticeTone(notice.status)">{{ noticeStatus(notice.status) }}</span></td>
              <td>{{ formatTime(notice.updateTime || notice.createTime) }}</td>
              <td class="table-actions">
                <button v-if="notice.status === 0" type="button" @click="handlePublish(notice.id)">发布</button>
                <button v-if="notice.status === 1" type="button" @click="handleWithdraw(notice.id)">下线</button>
              </td>
            </tr>
            <tr v-if="!loading && notices.length === 0">
              <td colspan="5" class="empty-cell">暂无通知公告</td>
            </tr>
          </tbody>
        </table>
      </article>

      <article class="panel">
        <div class="panel__head">
          <div>
            <h2>任务执行日志</h2>
            <p>最近执行记录。</p>
          </div>
          <span class="page-status">{{ logTotal }} 条</span>
        </div>
        <table class="data-table">
          <thead>
            <tr>
              <th>任务</th>
              <th>状态</th>
              <th>执行时间</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="log in jobLogs" :key="log.id">
              <td>{{ log.name || attr(log, 'jobName') || '-' }}</td>
              <td><span class="status-tag" :data-tone="log.status === 1 ? 'green' : 'red'">{{ log.status === 1 ? '成功' : '失败' }}</span></td>
              <td>{{ formatTime(log.createTime) }}</td>
            </tr>
            <tr v-if="!loading && jobLogs.length === 0">
              <td colspan="3" class="empty-cell">暂无执行日志</td>
            </tr>
          </tbody>
        </table>
      </article>
    </section>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import type { SystemRecord } from '@myow/api';
import {
  pageJobLogs,
  pageJobs,
  pageNotices,
  pauseJob,
  publishNotice,
  resumeJob,
  runJob,
  withdrawNotice
} from '@/services/systemService';

const loading = ref(false);
const errorMessage = ref('');
const jobs = ref<SystemRecord[]>([]);
const notices = ref<SystemRecord[]>([]);
const jobLogs = ref<SystemRecord[]>([]);
const jobTotal = ref(0);
const noticeTotal = ref(0);
const logTotal = ref(0);

const modules = computed(() => [
  { title: '定时任务', description: '任务配置、启停、手动执行。', status: `${jobTotal.value} 条`, tone: 'green' },
  { title: '通知公告', description: '草稿、发布、下线和有效期。', status: `${noticeTotal.value} 条`, tone: 'green' },
  { title: '任务日志', description: '调度执行结果与失败原因。', status: `${logTotal.value} 条`, tone: 'blue' },
  { title: '站点配置', description: '配置项、开关、敏感值。', status: '待接入页面', tone: 'amber' }
]);

onMounted(() => {
  void loadAll();
});

async function loadAll() {
  loading.value = true;
  errorMessage.value = '';
  try {
    const [jobPage, noticePage, logPage] = await Promise.all([
      pageJobs({ pageNum: 1, pageSize: 20 }),
      pageNotices({ pageNum: 1, pageSize: 10 }),
      pageJobLogs({ pageNum: 1, pageSize: 10 })
    ]);
    jobs.value = jobPage.list ?? [];
    notices.value = noticePage.list ?? [];
    jobLogs.value = logPage.list ?? [];
    jobTotal.value = Number(jobPage.total ?? jobs.value.length);
    noticeTotal.value = Number(noticePage.total ?? notices.value.length);
    logTotal.value = Number(logPage.total ?? jobLogs.value.length);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '系统中心数据加载失败';
  } finally {
    loading.value = false;
  }
}

async function handleRun(id: number) {
  await runJob(id);
  await loadAll();
}

async function handlePause(id: number) {
  await pauseJob(id);
  await loadAll();
}

async function handleResume(id: number) {
  await resumeJob(id);
  await loadAll();
}

async function handlePublish(id: number) {
  await publishNotice(id);
  await loadAll();
}

async function handleWithdraw(id: number) {
  await withdrawNotice(id);
  await loadAll();
}

function attr(record: SystemRecord, key: string) {
  const value = record.attributes?.[key];
  return value == null ? '' : String(value);
}

function statusText(status?: number) {
  return status === 1 ? '启用' : '停用';
}

function statusTone(status?: number) {
  return status === 1 ? 'green' : 'amber';
}

function noticeStatus(status?: number) {
  if (status === 1) return '已发布';
  if (status === 2) return '已下线';
  return '草稿';
}

function noticeTone(status?: number) {
  if (status === 1) return 'green';
  if (status === 2) return 'red';
  return 'amber';
}

function formatTime(value?: string) {
  if (!value) {
    return '-';
  }
  return value.replace('T', ' ').slice(0, 19);
}
</script>
