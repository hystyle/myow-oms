<template>
  <section class="page-stack">
    <header class="page-heading">
      <div><h1>通知公告</h1><p>维护草稿、发布、下线和有效期，可预留客户可见范围。</p></div>
      <button class="primary-action" type="button" @click="drawerOpen = true">新增公告</button>
    </header>
    <section class="toolbar query-panel">
      <label><span>标题</span><input placeholder="公告标题" /></label>
      <label><span>状态</span><select><option>全部状态</option><option>草稿</option><option>已发布</option><option>已下线</option></select></label>
      <div class="query-actions"><button type="button">查询</button><button type="button">重置</button></div>
    </section>
    <article class="panel table-panel">
      <div class="panel__head"><div><h2>公告列表</h2><p>状态流：草稿 -> 已发布 -> 已下线。</p></div><span class="page-status">{{ rows.length }} 条</span></div>
      <table class="data-table">
        <thead><tr><th>标题</th><th>类型</th><th>可见范围</th><th>状态</th><th>有效期</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="row in rows" :key="row.title">
            <td><strong>{{ row.title }}</strong></td><td>{{ row.type }}</td><td>{{ row.scope }}</td>
            <td><span class="status-tag" :data-tone="row.tone">{{ row.status }}</span></td><td>{{ row.expire }}</td>
            <td class="table-actions"><button type="button" @click="drawerOpen = true">编辑</button><button type="button">发布</button><button type="button">下线</button></td>
          </tr>
        </tbody>
      </table>
    </article>
    <div v-if="drawerOpen" class="crud-backdrop" @click.self="drawerOpen = false">
      <section class="crud-drawer">
        <header class="crud-drawer__head"><div><h2>公告编辑</h2><p>发布设置、可见范围和正文内容。</p></div><button type="button" @click="drawerOpen = false">关闭</button></header>
        <form class="crud-form">
          <label><span>标题</span><input /></label><label><span>类型</span><select><option>系统公告</option><option>客户公告</option></select></label>
          <label><span>有效期</span><input type="datetime-local" /></label><label><span>发布方式</span><select><option>保存草稿</option><option>立即发布</option><option>定时发布</option></select></label>
          <label class="form-wide"><span>可见范围</span><input placeholder="内部角色 / 部门 / 客户范围" /></label>
          <label class="form-wide"><span>公告内容</span><textarea rows="8" /></label>
          <footer class="crud-actions"><button type="button" @click="drawerOpen = false">取消</button><button class="primary-action" type="button">保存</button></footer>
        </form>
      </section>
    </div>
  </section>
</template>
<script setup lang="ts">
import { ref } from 'vue';
const drawerOpen = ref(false);
const rows = [
  { title: '美西仓截单时间调整', type: '客户公告', scope: 'US_WEST_CLUSTER', status: '已发布', tone: 'green', expire: '2026-07-31' },
  { title: '系统维护窗口', type: '系统公告', scope: '内部全员', status: '草稿', tone: 'amber', expire: '-' },
  { title: 'API 版本升级提醒', type: '客户公告', scope: '开发者账号', status: '已下线', tone: 'red', expire: '2026-06-30' }
];
</script>
