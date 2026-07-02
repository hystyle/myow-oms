<template>
  <div class="client-shell">
    <aside class="client-sidebar">
      <div class="client-logo">MYOW Portal</div>
      <nav class="client-menu">
        <router-link to="/dashboard">Dashboard</router-link>
      </nav>
    </aside>
    <section class="client-main">
      <header class="client-topbar"><span>Client Portal</span></header>
      <main class="client-content"><router-view /></main>
    </section>
  </div>
</template>
