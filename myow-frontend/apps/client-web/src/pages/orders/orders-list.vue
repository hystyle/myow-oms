<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>订单履约</h1>
        <p>查看一件代发、大货中转和补仓订单的履约状态。</p>
      </div>
      <button class="primary-action" type="button">创建订单</button>
    </header>

    <section class="toolbar">
      <input placeholder="搜索订单号、收件人、Tracking No." />
      <select>
        <option>全部状态</option>
        <option>待审单</option>
        <option>已锁库</option>
        <option>已下发仓库</option>
        <option>已出库</option>
      </select>
      <button type="button">查询</button>
    </section>

    <article class="panel table-panel">
      <div class="panel__head">
        <h2>订单列表</h2>
        <p>后续接入海外仓订单 API。</p>
      </div>
      <table class="data-table">
        <thead>
          <tr>
            <th>订单号</th>
            <th>仓库</th>
            <th>渠道</th>
            <th>状态</th>
            <th>Tracking No.</th>
            <th>创建时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.orderNo">
            <td><code>{{ row.orderNo }}</code></td>
            <td>{{ row.warehouse }}</td>
            <td>{{ row.channel }}</td>
            <td><span class="status-tag" :data-tone="row.tone">{{ row.status }}</span></td>
            <td>{{ row.trackingNo }}</td>
            <td>{{ row.createdAt }}</td>
          </tr>
        </tbody>
      </table>
    </article>
  </section>
</template>

<script setup lang="ts">
const rows = [
  { orderNo: 'SO202607020001', warehouse: 'US_LA_01', channel: 'FedEx Ground', status: '已下发仓库', tone: 'blue', trackingNo: '-', createdAt: '2026-07-02 09:30' },
  { orderNo: 'SO202607020002', warehouse: 'US_NJ_01', channel: 'UPS Ground', status: '缺货挂起', tone: 'amber', trackingNo: '-', createdAt: '2026-07-02 10:12' },
  { orderNo: 'SO202607020003', warehouse: 'US_LA_01', channel: 'USPS', status: '已出库', tone: 'green', trackingNo: '9400******', createdAt: '2026-07-02 11:08' }
];
</script>
