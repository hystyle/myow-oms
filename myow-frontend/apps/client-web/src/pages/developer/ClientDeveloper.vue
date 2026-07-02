<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>开发者中心</h1>
        <p>管理 ERP 对接凭证、白名单和接口调用状态。</p>
      </div>
      <button class="primary-action" type="button">生成新密钥</button>
    </header>

    <section class="workspace-grid">
      <article class="panel">
        <div class="panel__head">
          <h2>API 凭证</h2>
          <p>客户主账号可查看和重置。</p>
        </div>
        <dl class="detail-grid">
          <div>
            <dt>AppKey</dt>
            <dd><code>ck_live_9f2a****21</code></dd>
          </div>
          <div>
            <dt>AppSecret</dt>
            <dd><code>已隐藏</code></dd>
          </div>
          <div>
            <dt>白名单</dt>
            <dd>203.0.113.0 / 198.51.100.8</dd>
          </div>
          <div>
            <dt>状态</dt>
            <dd>启用</dd>
          </div>
        </dl>
      </article>

      <article class="panel">
        <div class="panel__head">
          <h2>接口范围</h2>
          <p>开放给 ERP、WMS 和打单系统的客户侧能力。</p>
        </div>
        <ul class="check-list">
          <li>SKU 主数据推送</li>
          <li>ASN 入库预报</li>
          <li>订单创建与取消</li>
          <li>库存可用数回传</li>
          <li>Tracking Number 回传</li>
        </ul>
      </article>
    </section>
  </section>
</template>
