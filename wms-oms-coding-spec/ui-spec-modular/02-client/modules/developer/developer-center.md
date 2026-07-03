# 客户端页面：开发者中心与客服支持

## 1. 页面定位

开发者中心服务客户技术人员自助完成 API 对接；客服支持服务客户提交、追踪和回复问题。

## 2. 页面清单

| 页面 | 路由 | 权限前缀 | 优先级 |
| --- | --- | --- | --- |
| API 凭证 | `/client/developer/api-credential` | `client:api-credential` | P1 |
| 调用日志 | `/client/developer/api-log` | `client:api-log` | P1 |
| 工单中心 | `/client/support/ticket` | `client:ticket` | P1 |
| 公告 | `/client/support/notice` | `client:notice` | P1 |

## 3. API 凭证页面

### 表格列

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| AppKey | appKey | 支持复制 |
| AppSecret | secretMasked | 仅脱敏展示，创建时完整展示一次 |
| IP 白名单 | ipWhitelist | 多个 IP 换行或 +N |
| Webhook 地址 | webhookUrl | 超长省略 |
| 状态 | status | 启用 / 禁用 |
| 创建时间 | createdTime | - |
| 操作 | actions | 重置 Secret、编辑白名单、禁用 |

### 安全规则

- Secret 仅创建时完整展示。
- 重置 Secret 必须二次确认，并说明旧 Secret 会立即失效。
- 修改 IP 白名单后需要展示生效提示。

## 4. 调用日志页面

### 查询条件

| 字段 | Label | 类型 | 默认展示 |
| --- | --- | --- | --- |
| traceId | Trace ID | input | 是 |
| requestPath | 请求路径 | input | 是 |
| result | 调用结果 | select | 是 |
| createdTime | 调用时间 | dateRange | 是 |

### 表格列

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| Trace ID | traceId | 支持复制，点击详情 |
| 请求路径 | requestPath | - |
| 调用结果 | result | 成功 / 失败 |
| 错误码 | errorCode | 失败时展示 |
| 客户提示 | clientMessage | 面向客户的错误说明 |
| 调用时间 | createdTime | - |
| 操作 | actions | 查看详情、复制错误 |
