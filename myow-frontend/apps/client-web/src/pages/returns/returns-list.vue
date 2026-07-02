<template>
  <section class="page-stack">
    <header class="page-heading"><div><h1>退货售后</h1><p>发起 RMA、查看质检照片并下达重新上架、销毁或退回指令。</p></div><button class="primary-action" type="button">发起退货</button></header>
    <article class="panel table-panel">
      <div class="panel__head"><h2>退货申请</h2><p>逆向物流状态和处理指令。</p></div>
      <table class="data-table"><thead><tr><th>RMA</th><th>关联订单</th><th>SKU</th><th>仓库反馈</th><th>卖家指令</th><th>状态</th></tr></thead>
        <tbody><tr v-for="row in rows" :key="row.rma"><td><code>{{ row.rma }}</code></td><td>{{ row.order }}</td><td>{{ row.sku }}</td><td>{{ row.feedback }}</td><td>{{ row.action }}</td><td><span class="status-tag" :data-tone="row.tone">{{ row.status }}</span></td></tr></tbody></table>
    </article>
  </section>
</template>
<script setup lang="ts">
const rows = [
  { rma: 'RMA20260702001', order: 'SO202607020003', sku: 'MYOW-BAG-001', feedback: '外箱破损，商品完好', action: '重新上架', status: '待仓库执行', tone: 'blue' },
  { rma: 'RMA20260701004', order: 'SO202607010099', sku: 'MYOW-LAMP-009', feedback: '灯罩破裂', action: '销毁', status: '已完成', tone: 'green' }
];
</script>
