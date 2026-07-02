<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>库存中心</h1>
        <p>按 SKU、仓库和库存状态查看可用、锁定、冻结和在途数量。</p>
      </div>
      <button class="primary-action" type="button">同步 ERP 库存</button>
    </header>

    <div class="metric-grid">
      <article v-for="item in metrics" :key="item.label" class="metric-card">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
        <p>{{ item.note }}</p>
      </article>
    </div>

    <article class="panel table-panel">
      <div class="panel__head">
        <h2>SKU 库存</h2>
        <p>后续接入海外仓库存 API。</p>
      </div>
      <table class="data-table">
        <thead>
          <tr>
            <th>SKU</th>
            <th>仓库</th>
            <th>可用</th>
            <th>待出库</th>
            <th>冻结</th>
            <th>在途</th>
            <th>水位</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="`${row.sku}-${row.warehouse}`">
            <td><code>{{ row.sku }}</code></td>
            <td>{{ row.warehouse }}</td>
            <td>{{ row.available }}</td>
            <td>{{ row.outbound }}</td>
            <td>{{ row.frozen }}</td>
            <td>{{ row.inTransit }}</td>
            <td><span class="status-tag" :data-tone="row.tone">{{ row.level }}</span></td>
          </tr>
        </tbody>
      </table>
    </article>
  </section>
</template>

<script setup lang="ts">
const metrics = [
  { label: '可用库存', value: '28,640', note: '可绑定订单发货。' },
  { label: '待出库', value: '1,284', note: '已锁库或拣货中。' },
  { label: '冻结库存', value: '96', note: '异常或质检不合格。' },
  { label: '在途库存', value: '7,520', note: 'ASN 已生成未上架。' }
];

const rows = [
  { sku: 'MYOW-BAG-001', warehouse: 'US_LA_01', available: 1280, outbound: 64, frozen: 0, inTransit: 500, level: '正常', tone: 'green' },
  { sku: 'MYOW-LAMP-009', warehouse: 'US_NJ_01', available: 24, outbound: 18, frozen: 6, inTransit: 0, level: '低水位', tone: 'amber' },
  { sku: 'MYOW-BOX-113', warehouse: 'UK_LON_01', available: 0, outbound: 0, frozen: 0, inTransit: 320, level: '缺货', tone: 'red' }
];
</script>
