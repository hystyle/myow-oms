<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>账单费用</h1>
        <p>查看仓储费、出库费、尾程物流费和增值服务扣费流水。</p>
      </div>
      <button class="primary-action" type="button">导出账单</button>
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
        <h2>费用流水</h2>
        <p>后续接入财务账单 API。</p>
      </div>
      <table class="data-table">
        <thead>
          <tr>
            <th>流水号</th>
            <th>费用项</th>
            <th>关联单据</th>
            <th>金额</th>
            <th>状态</th>
            <th>时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.no">
            <td><code>{{ row.no }}</code></td>
            <td>{{ row.item }}</td>
            <td>{{ row.bizNo }}</td>
            <td>{{ row.amount }}</td>
            <td><span class="status-tag" :data-tone="row.tone">{{ row.status }}</span></td>
            <td>{{ row.time }}</td>
          </tr>
        </tbody>
      </table>
    </article>
  </section>
</template>

<script setup lang="ts">
const metrics = [
  { label: '账户余额', value: '$18,420', note: '可用于订单履约扣费。' },
  { label: '本月已扣', value: '$12,480', note: '按账期累计。' },
  { label: '待确认', value: '$1,260', note: '复测、差异或争议费用。' },
  { label: '信用额度', value: '$50,000', note: '客户授信额度。' }
];

const rows = [
  { no: 'BILL20260702001', item: '尾程物流费', bizNo: 'SO202607020001', amount: '$8.92', status: '已扣费', tone: 'green', time: '2026-07-02 12:11' },
  { no: 'BILL20260702002', item: '出库操作费', bizNo: 'SO202607020002', amount: '$1.35', status: '待确认', tone: 'amber', time: '2026-07-02 12:14' },
  { no: 'BILL20260702003', item: '仓储费', bizNo: 'STORAGE-202607', amount: '$320.00', status: '已扣费', tone: 'green', time: '2026-07-02 13:00' }
];
</script>
