<template>
  <section class="page-stack">
    <header class="page-heading"><div><h1>站点配置</h1><p>维护系统级配置项、开关和敏感值。</p></div><button class="primary-action" type="button">新增配置</button></header>
    <section class="config-layout">
      <aside class="panel tree-panel">
        <h2>配置分组</h2>
        <button v-for="group in groups" :key="group" class="tree-item" :class="{ active: current === group }" type="button" @click="current = group">{{ group }}</button>
      </aside>
      <article class="panel table-panel">
        <div class="panel__head"><div><h2>{{ current }}</h2><p>敏感配置默认脱敏，查看明文需权限和审计。</p></div><button class="secondary-action" type="button">刷新缓存</button></div>
        <table class="data-table">
          <thead><tr><th>配置键</th><th>配置值</th><th>类型</th><th>状态</th><th>更新时间</th><th>操作</th></tr></thead>
          <tbody>
            <tr v-for="row in rows" :key="row.key">
              <td><code>{{ row.key }}</code></td><td>{{ row.secret ? '******' : row.value }}</td><td>{{ row.type }}</td>
              <td><span class="status-tag" data-tone="green">启用</span></td><td>{{ row.time }}</td>
              <td class="table-actions"><button type="button">编辑</button><button v-if="row.secret" type="button">查看明文</button></td>
            </tr>
          </tbody>
        </table>
      </article>
    </section>
  </section>
</template>
<script setup lang="ts">
import { ref } from 'vue';
const groups = ['基础配置', '安全策略', '文件存储', '集成开关'];
const current = ref(groups[0]);
const rows = [
  { key: 'site.name', value: 'MYOW Platform', type: 'string', secret: false, time: '2026-07-02 18:00' },
  { key: 'security.password.expireDays', value: '90', type: 'number', secret: false, time: '2026-07-02 18:00' },
  { key: 'storage.s3.secretKey', value: 'AKIA...', type: 'secret', secret: true, time: '2026-07-02 18:00' }
];
</script>
