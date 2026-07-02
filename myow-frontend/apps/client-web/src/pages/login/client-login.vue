<template>
  <main class="login-page">
    <section class="login-copy">
      <div class="portal-mark">MYOW</div>
      <h1>客户自助门户</h1>
      <p>面向货主客户的订单、库存、账单和 API 对接入口。管理端与客户端分离，权限边界独立。</p>
      <div class="login-proof">
        <span>订单履约</span>
        <span>库存同步</span>
        <span>费用流水</span>
        <span>API 凭证</span>
      </div>
    </section>
    <section class="login-panel">
      <h2>客户端登录</h2>
      <p>使用客户主账号或子账号进入门户。</p>
      <form>
        <label>客户账号<input autocomplete="username" placeholder="请输入客户账号" /></label>
        <label>密码<input type="password" autocomplete="current-password" placeholder="请输入密码" /></label>
        <button type="button">登录</button>
      </form>
      <p class="login-tip">客户登录接口将在 customer 账号域实现后接入。</p>
    </section>
  </main>
</template>