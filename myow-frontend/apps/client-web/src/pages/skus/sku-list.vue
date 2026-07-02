<template>
  <section class="page-stack">
    <header class="page-heading"><div><h1>商品 SKU</h1><p>维护商品主数据、尺重、敏感属性和 WMS 同步状态。</p></div><button class="primary-action" type="button">新增 SKU</button></header>
    <section class="toolbar"><input placeholder="SKU、Seller SKU、商品名称" /><select><option>全部状态</option><option>草稿</option><option>可用</option><option>已实测</option></select><button type="button">查询</button></section>
    <article class="panel table-panel">
      <div class="panel__head"><h2>SKU 主数据</h2><p>商品数据只归属海外仓模块，头程按文本填写。</p></div>
      <table class="data-table"><thead><tr><th>SKU Code</th><th>Seller SKU</th><th>商品名称</th><th>尺重</th><th>敏感属性</th><th>状态</th><th>同步</th></tr></thead>
        <tbody><tr v-for="row in rows" :key="row.sku"><td><code>{{ row.sku }}</code></td><td>{{ row.sellerSku }}</td><td>{{ row.name }}</td><td>{{ row.size }}</td><td>{{ row.tags }}</td><td><span class="status-tag" :data-tone="row.tone">{{ row.status }}</span></td><td>{{ row.sync }}</td></tr></tbody></table>
    </article>
  </section>
</template>
<script setup lang="ts">
const rows = [
  { sku: 'MYOW-BAG-001', sellerSku: 'BAG-BLK-M', name: 'Travel Bag', size: '12x8x4 in / 1.8 lb', tags: '普货', status: '已实测', tone: 'green', sync: 'US_LA_01 成功' },
  { sku: 'MYOW-LAMP-009', sellerSku: 'LED-LAMP-9', name: 'Desk Lamp', size: '18x9x9 in / 4.2 lb', tags: '带电/易碎', status: '可用', tone: 'blue', sync: '待同步' }
];
</script>
