<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>定时任务</h1>
        <p>维护调度任务、Cron 表达式、处理器和最近执行结果。</p>
      </div>
      <button class="primary-action" type="button" @click="drawerOpen = true">新增任务</button>
    </header>

    <section class="toolbar query-panel">
      <label><span>关键词</span><input placeholder="任务名称、任务组、处理器" /></label>
      <label><span>状态</span><select><option>全部状态</option><option>启用</option><option>暂停</option></select></label>
      <div class="query-actions"><button type="button">查询</button><button type="button">重置</button></div>
    </section>

    <section class="workspace-grid">
      <article class="panel table-panel">
        <div class="panel__head">
          <div><h2>任务列表</h2><p>支持暂停、恢复、立即执行和查看日志。</p></div>
          <span class="page-status">{{ jobs.length }} 个任务</span>
        </div>
        <table class="data-table dense-table">
          <thead><tr><th>任务名称</th><th>任务组</th><th>Cron</th><th>处理器</th><th>状态</th><th>最近执行</th><th>操作</th></tr></thead>
          <tbody>
            <tr v-for="job in jobs" :key="job.id" :class="{ 'selected-row': selectedJob.id === job.id }">
              <td><strong>{{ job.name }}</strong></td>
              <td>{{ job.group }}</td>
              <td><code>{{ job.cron }}</code></td>
              <td><code>{{ job.handler }}</code></td>
              <td><span class="status-tag" :data-tone="job.status === '启用' ? 'green' : 'amber'">{{ job.status }}</span></td>
              <td>{{ job.lastRun }}</td>
              <td class="table-actions">
                <button type="button" @click="selectedJob = job">日志</button>
                <button type="button">执行一次</button>
                <button type="button">{{ job.status === '启用' ? '暂停' : '恢复' }}</button>
              </td>
            </tr>
          </tbody>
        </table>
      </article>

      <article class="panel">
        <div class="panel__head">
          <div><h2>执行日志</h2><p>{{ selectedJob.name }} 最近执行记录。</p></div>
          <span class="status-tag" data-tone="blue">最近 10 条</span>
        </div>
        <div class="timeline-list">
          <button v-for="log in logs" :key="log.id" class="log-item" type="button" @click="selectedLog = log">
            <span>{{ log.time }}</span>
            <strong><span class="status-tag" :data-tone="log.result === '成功' ? 'green' : 'red'">{{ log.result }}</span></strong>
          </button>
        </div>
        <dl class="detail-grid log-detail">
          <div><dt>耗时</dt><dd>{{ selectedLog.cost }}</dd></div>
          <div><dt>Trace ID</dt><dd><code>{{ selectedLog.traceId }}</code></dd></div>
          <div class="form-wide"><dt>失败原因 / 返回</dt><dd>{{ selectedLog.message }}</dd></div>
        </dl>
      </article>
    </section>

    <div v-if="drawerOpen" class="crud-backdrop" @click.self="drawerOpen = false">
      <section class="crud-drawer">
        <header class="crud-drawer__head"><div><h2>新增任务</h2><p>Cron 保存前应预览下次执行时间。</p></div><button type="button" @click="drawerOpen = false">关闭</button></header>
        <form class="crud-form">
          <label><span>任务名称</span><input /></label>
          <label><span>任务组</span><input /></label>
          <label><span>Cron 表达式</span><input placeholder="0 */5 * * * ?" /></label>
          <label><span>处理器 Bean</span><input /></label>
          <label class="form-wide"><span>参数 JSON</span><textarea rows="4" /></label>
          <footer class="crud-actions"><button type="button" @click="drawerOpen = false">取消</button><button class="primary-action" type="button">保存</button></footer>
        </form>
      </section>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const drawerOpen = ref(false);
const jobs = [
  { id: '1', name: '库存同步', group: 'inventory', cron: '0 */5 * * * ?', handler: 'inventorySyncJob', status: '启用', lastRun: '2026-07-02 21:55' },
  { id: '2', name: '账单生成', group: 'finance', cron: '0 0 2 * * ?', handler: 'billingGenerateJob', status: '启用', lastRun: '2026-07-02 02:00' },
  { id: '3', name: '过期公告下线', group: 'system', cron: '0 */10 * * * ?', handler: 'noticeExpireJob', status: '暂停', lastRun: '2026-07-01 22:10' }
];
const logs = [
  { id: 'l1', result: '成功', time: '2026-07-02 21:55:00', cost: '418 ms', traceId: 'job-81a7', message: '同步 426 个 SKU 库存。' },
  { id: 'l2', result: '失败', time: '2026-07-02 21:50:00', cost: '2.4 s', traceId: 'job-7c20', message: 'WMS API timeout，已等待下次调度重试。' }
];
const selectedJob = ref(jobs[0]);
const selectedLog = ref(logs[0]);
</script>
