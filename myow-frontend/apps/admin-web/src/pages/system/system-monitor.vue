<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>系统监控</h1>
        <p>查看服务、Redis 和数据库健康状态。</p>
      </div>
      <button class="primary-action" type="button" @click="loadMonitor">刷新监控</button>
    </header>

    <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>

    <div class="metric-grid">
      <article v-for="item in cards" :key="item.title" class="metric-card">
        <span>{{ item.title }}</span>
        <strong>{{ item.value }}</strong>
        <p>{{ item.description }}</p>
      </article>
    </div>

    <section class="workspace-grid">
      <article v-for="section in sections" :key="section.title" class="panel">
        <div class="panel__head">
          <div>
            <h2>{{ section.title }}</h2>
            <p>{{ section.endpoint }}</p>
          </div>
        </div>
        <dl class="detail-grid">
          <div v-for="entry in section.entries" :key="entry.key">
            <dt>{{ entry.key }}</dt>
            <dd>{{ entry.value }}</dd>
          </div>
        </dl>
      </article>
    </section>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { loadDbMetrics, loadRedisMetrics, loadServerMetrics } from '@/services/system-service';

type MetricRecord = Record<string, unknown>;

const server = ref<MetricRecord>({});
const redis = ref<MetricRecord>({});
const db = ref<MetricRecord>({});
const errorMessage = ref('');

const cards = computed(() => [
  { title: '服务状态', value: Object.keys(server.value).length ? '在线' : '-', description: 'JVM、CPU、内存等服务指标。' },
  { title: 'Redis 状态', value: Object.keys(redis.value).length ? '在线' : '-', description: '缓存连接与运行指标。' },
  { title: '数据库状态', value: Object.keys(db.value).length ? '在线' : '-', description: '数据源连接与基础健康状态。' },
  { title: '刷新方式', value: '手动', description: '当前页面按需拉取后端监控接口。' }
]);

const sections = computed(() => [
  { title: '服务器', endpoint: '/myow/api/v1/system/monitor/server', entries: toEntries(server.value) },
  { title: 'Redis', endpoint: '/myow/api/v1/system/monitor/redis', entries: toEntries(redis.value) },
  { title: '数据库', endpoint: '/myow/api/v1/system/monitor/db', entries: toEntries(db.value) }
]);

onMounted(() => {
  void loadMonitor();
});

async function loadMonitor() {
  errorMessage.value = '';
  try {
    const [serverResult, redisResult, dbResult] = await Promise.all([
      loadServerMetrics(),
      loadRedisMetrics(),
      loadDbMetrics()
    ]);
    server.value = serverResult ?? {};
    redis.value = redisResult ?? {};
    db.value = dbResult ?? {};
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '监控数据加载失败';
  }
}

function toEntries(record: MetricRecord) {
  return Object.entries(record).slice(0, 12).map(([key, value]) => ({
    key,
    value: formatValue(value)
  }));
}

function formatValue(value: unknown) {
  if (value == null || value === '') return '-';
  if (typeof value === 'object') return JSON.stringify(value).slice(0, 120);
  return String(value);
}
</script>
