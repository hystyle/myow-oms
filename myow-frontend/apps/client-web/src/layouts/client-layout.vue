<template>
  <div class="client-shell">
    <aside class="client-sidebar">
      <div class="client-logo">
        <span class="client-logo-mark">M</span>
        <span>MYOW Portal</span>
      </div>
      <nav class="client-menu">
        <router-link to="/dashboard">运营看板</router-link>
        <router-link to="/orders">订单履约</router-link>
        <router-link to="/inventory">库存中心</router-link>
        <router-link to="/billing">账单费用</router-link>
        <router-link to="/developer">开发者中心</router-link>
      </nav>
    </aside>
    <section class="client-main">
      <header class="client-topbar">
        <strong>客户端门户</strong>
        <span>示例客户：Acme Trading</span>
      </header>
      <main class="client-content"><router-view /></main>
    </section>
  </div>
</template>