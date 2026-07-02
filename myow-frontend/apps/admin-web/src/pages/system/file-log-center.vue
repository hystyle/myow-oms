<template>
  <section class="page-stack">
    <header class="page-heading"><div><h1>文件与日志</h1><p>查看上传文件、操作日志、登录日志和在线会话。</p></div><button class="primary-action" type="button">上传文件</button></header>
    <div class="tabs"><button v-for="tab in tabs" :key="tab" :class="{ active: current === tab }" type="button" @click="current = tab">{{ tab }}</button></div>
    <article class="panel table-panel">
      <div class="panel__head"><div><h2>{{ current }}</h2><p>前端原型数据，后续按模块接真实 API。</p></div><span class="page-status">{{ rows.length }} 条</span></div>
      <table class="data-table">
        <thead><tr><th>对象</th><th>类型</th><th>结果</th><th>说明</th><th>时间</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="row in rows" :key="row.id">
            <td><code>{{ row.id }}</code></td><td>{{ row.type }}</td><td><span class="status-tag" :data-tone="row.tone">{{ row.result }}</span></td><td>{{ row.note }}</td><td>{{ row.time }}</td>
            <td class="table-actions"><button type="button">详情</button><button type="button">下载</button></td>
          </tr>
        </tbody>
      </table>
    </article>
  </section>
</template>
<script setup lang="ts">
import { ref } from 'vue';
const tabs = ['文件管理', '操作日志', '登录日志', '在线用户'];
const current = ref(tabs[0]);
const rows = [
  { id: 'FILE-1001', type: '导入模板', result: '可用', tone: 'green', note: 'sku-template.xlsx', time: '2026-07-02 11:00' },
  { id: 'LOG-8821', type: '操作日志', result: '成功', tone: 'green', note: '发布公告', time: '2026-07-02 12:10' },
  { id: 'LOGIN-781', type: '登录日志', result: '失败', tone: 'red', note: '验证码错误', time: '2026-07-02 13:20' }
];
</script>
