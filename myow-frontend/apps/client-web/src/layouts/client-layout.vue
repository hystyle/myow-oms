<template>
  <div class="client-shell">
    <aside class="client-sidebar">
      <div class="client-logo">
        <span class="client-logo-mark">M</span>
        <span>MYOW Portal</span>
      </div>
      <nav class="client-menu">
        <router-link to="/dashboard">运营看板</router-link>
        <router-link to="/outbound">订单履约</router-link>
        <router-link to="/inbound-asn">入库预报</router-link>
        <router-link to="/returns">退货售后</router-link>
        <router-link to="/sku">商品 SKU</router-link>
        <router-link to="/inventory">库存中心</router-link>
        <router-link to="/billing/bill">账单费用</router-link>
        <router-link to="/developer/api-credential">开发者中心</router-link>
        <router-link to="/account/member">账号权限</router-link>
        <router-link to="/support/ticket">客服工单</router-link>
      </nav>
    </aside>
    <section class="client-main">
      <header class="client-topbar">
        <strong>客户端门户</strong>
        <span>Acme Trading / 主账号</span>
      </header>
      <main class="client-content"><router-view /></main>
    </section>
  </div>
</template>
