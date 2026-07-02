<template>
  <section class="page-stack">
    <header class="page-heading"><div><h1>账号权限</h1><p>客户主账号管理子账号、角色、数据范围和登录安全。</p></div><button class="primary-action" type="button">新增子账号</button></header>
    <section class="workspace-grid">
      <article class="panel table-panel"><div class="panel__head"><h2>子账号</h2><p>客户企业内部账号权限。</p></div>
        <table class="data-table"><thead><tr><th>账号</th><th>姓名</th><th>角色</th><th>数据范围</th><th>状态</th></tr></thead><tbody><tr v-for="row in accounts" :key="row.account"><td>{{ row.account }}</td><td>{{ row.name }}</td><td>{{ row.role }}</td><td>{{ row.scope }}</td><td><span class="status-tag" data-tone="green">启用</span></td></tr></tbody></table>
      </article>
      <article class="panel"><div class="panel__head"><h2>角色建议</h2><p>主账号、操作员、财务、技术、只读成员。</p></div><ul class="check-list"><li>操作员：订单、入库、退货</li><li>财务：账单、充值、发票</li><li>技术：API 凭证、白名单、调用日志</li></ul></article>
    </section>
  </section>
</template>
<script setup lang="ts">
const accounts = [
  { account: 'ops@acme.com', name: '小李', role: '操作员', scope: '自己创建' },
  { account: 'finance@acme.com', name: '老王', role: '财务', scope: '全企业账单' },
  { account: 'dev@acme.com', name: 'Alex', role: '技术', scope: 'API 管理' }
];
</script>
