<template>
  <section class="page-stack">
    <header class="page-heading"><div><h1>入库预报</h1><p>创建 ASN、下载箱标、追踪头程和确认到货差异。</p></div><button class="primary-action" type="button">新建预报</button></header>
    <section class="toolbar"><input placeholder="ASN 单号、箱号、SKU" /><select><option>全部仓库</option><option>US_LA_01</option></select><select><option>全部状态</option><option>在途</option><option>待确认差异</option></select><button type="button">查询</button></section>
    <article class="panel table-panel">
      <div class="panel__head"><h2>ASN 列表</h2><p>卖家可手工创建或通过 ERP 推送。</p></div>
      <table class="data-table"><thead><tr><th>ASN</th><th>目的仓</th><th>箱数</th><th>SKU 数</th><th>头程轨迹</th><th>状态</th><th>操作</th></tr></thead>
        <tbody><tr v-for="row in rows" :key="row.no"><td><code>{{ row.no }}</code></td><td>{{ row.warehouse }}</td><td>{{ row.boxes }}</td><td>{{ row.skus }}</td><td>{{ row.track }}</td><td><span class="status-tag" :data-tone="row.tone">{{ row.status }}</span></td><td class="row-actions">下载箱标 / 查看差异</td></tr></tbody></table>
    </article>
  </section>
</template>
<script setup lang="ts">
const rows = [
  { no: 'ASN20260702001', warehouse: 'US_LA_01', boxes: 42, skus: 18, track: '海运卡派 / 已到港', status: '在途', tone: 'blue' },
  { no: 'ASN20260701009', warehouse: 'US_NJ_01', boxes: 18, skus: 7, track: 'WMS 已清点', status: '待确认差异', tone: 'amber' }
];
</script>
