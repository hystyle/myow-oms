<template>
  <section class="page-stack">
    <header class="page-heading"><div><h1>客服工单</h1><p>从订单、库存、账单异常发起工单并追踪回复。</p></div><button class="primary-action" type="button">新建工单</button></header>
    <article class="panel table-panel">
      <div class="panel__head"><h2>工单列表</h2><p>异常上下文会自动带入工单。</p></div>
      <table class="data-table"><thead><tr><th>工单号</th><th>类型</th><th>关联单据</th><th>优先级</th><th>最近回复</th><th>状态</th></tr></thead>
        <tbody><tr v-for="row in rows" :key="row.no"><td><code>{{ row.no }}</code></td><td>{{ row.type }}</td><td>{{ row.bizNo }}</td><td>{{ row.priority }}</td><td>{{ row.reply }}</td><td><span class="status-tag" :data-tone="row.tone">{{ row.status }}</span></td></tr></tbody></table>
    </article>
  </section>
</template>
<script setup lang="ts">
const rows = [
  { no: 'TK20260702001', type: '地址异常', bizNo: 'SO202607020002', priority: '高', reply: '请补充门牌号', status: '待客户回复', tone: 'amber' },
  { no: 'TK20260701008', type: '费用争议', bizNo: 'BILL20260701002', priority: '中', reply: '已提交复核', status: '处理中', tone: 'blue' }
];
</script>
