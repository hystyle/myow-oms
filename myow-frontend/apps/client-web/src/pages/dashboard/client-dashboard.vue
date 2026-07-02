<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>运营看板</h1>
        <p>客户视角的订单履约、库存风险、账单和接口状态。</p>
      </div>
      <span class="page-status">客户端门户原型</span>
    </header>

    <div class="metric-grid">
      <article v-for="metric in metrics" :key="metric.label" class="metric-card">
        <span>{{ metric.label }}</span>
        <strong>{{ metric.value }}</strong>
        <p>{{ metric.note }}</p>
      </article>
    </div>

    <section class="workspace-grid">
      <article class="panel">
        <div class="panel__head">
          <h2>今日履约</h2>
          <p>订单从接收、审单、锁库到出库的关键状态。</p>
        </div>
        <div class="timeline-list">
          <div v-for="item in fulfillment" :key="item.label">
            <span>{{ item.label }}</span>
            <strong>{{ item.value }}</strong>
          </div>
        </div>
      </article>

      <article class="panel">
        <div class="panel__head">
          <h2>快捷入口</h2>
          <p>客户最常用的自助操作。</p>
        </div>
        <div class="shortcut-list">
          <router-link to="/orders">查看订单履约</router-link>
          <router-link to="/inventory">查看库存水位</router-link>
          <router-link to="/billing">查看费用流水</router-link>
          <router-link to="/developer">管理 API 凭证</router-link>
        </div>
      </article>
    </section>
  </section>
</template>

<script setup lang="ts">
const metrics = [
  { label: '待处理订单', value: '18', note: '缺货、地址异常或欠费拦截。' },
  { label: '可用库存 SKU', value: '426', note: '按客户和仓库合并统计。' },
  { label: '本月费用', value: '$12,480', note: '仓储、操作、尾程和增值服务。' },
  { label: 'API 状态', value: '正常', note: 'ERP 同步和库存回传可用。' }
];

const fulfillment = [
  { label: '已接收', value: '126' },
  { label: '审单通过', value: '108' },
  { label: '已下发 WMS', value: '94' },
  { label: '已出库', value: '76' }
];
</script>