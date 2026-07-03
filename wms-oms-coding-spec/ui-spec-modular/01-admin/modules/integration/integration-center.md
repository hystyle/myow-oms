# 管理端页面：集成中心

## 1. 页面定位

集成中心用于管理 ERP/WMS/TMS/API 凭证、映射关系、同步日志和调用失败排查。重点是安全、可追踪、可重试。

## 2. 页面清单

| 页面 | 路由 | 权限前缀 | 优先级 |
| --- | --- | --- | --- |
| API 凭证 | `/admin/integration/api-credential` | `integration:api-credential` | P1 |
| 同步日志 | `/admin/integration/sync-log` | `integration:sync-log` | P1 |
| 映射管理 | `/admin/integration/mapping` | `integration:mapping` | P1 |

## 3. API 凭证页面

### 查询条件

| 字段 | Label | 类型 | 默认展示 |
| --- | --- | --- | --- |
| customerName | 客户 | input | 是 |
| appKey | AppKey | input | 是 |
| status | 状态 | select | 是 |
| createdTime | 创建时间 | dateRange | 是 |

### 表格列

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| 客户 | customerName | - |
| AppKey | appKey | 支持复制 |
| Secret | secretMasked | 仅脱敏展示 |
| IP 白名单 | ipWhitelist | 超长省略 |
| 状态 | status | 启用 / 禁用 |
| 创建时间 | createdTime | - |
| 操作 | actions | 详情、禁用、重置 Secret、查看调用日志 |

### 安全规则

- AppSecret 仅创建时完整展示。
- 重置 Secret 必须二次确认，并说明旧 Secret 将立即失效。
- 查看 Secret 明文默认不支持；如业务要求支持，必须单独权限和审计。

## 4. 同步日志页面

### 查询条件

| 字段 | Label | 类型 | 默认展示 |
| --- | --- | --- | --- |
| traceId | Trace ID | input | 是 |
| customerName | 客户 | input | 是 |
| systemType | 系统类型 | select | 是 |
| bizType | 业务类型 | select | 是 |
| result | 执行结果 | select | 是 |
| createdTime | 时间 | dateRange | 是 |

### 表格列

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| Trace ID | traceId | 支持复制，点击详情 |
| 客户 | customerName | - |
| 系统类型 | systemType | ERP / WMS / TMS / API |
| 业务类型 | bizType | SKU / 出库 / 入库 / 轨迹 / 费用 |
| 请求路径 | requestPath | 超长省略 |
| 执行结果 | result | 成功 / 失败 / 处理中 |
| 错误码 | errorCode | 失败时展示 |
| 开始时间 | startTime | - |
| 耗时 | durationMs | ms |
| 操作 | actions | 详情、重试、复制错误 |

## 5. API 映射

| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| 凭证分页 | POST | `/admin/integration/api-credential/page` | `ApiCredentialPageReqVO` | `PageResult<ApiCredentialRespVO>` | `integration:api-credential:query` |
| 创建凭证 | POST | `/admin/integration/api-credential` | `ApiCredentialCreateReqVO` | `ApiCredentialCreateRespVO` | `integration:api-credential:add` |
| 重置 Secret | POST | `/admin/integration/api-credential/{id}/reset-secret` | - | `ApiSecretRespVO` | `integration:api-credential:reset-secret` |
| 同步日志分页 | POST | `/admin/integration/sync-log/page` | `SyncLogPageReqVO` | `PageResult<SyncLogRespVO>` | `integration:sync-log:query` |
| 日志详情 | GET | `/admin/integration/sync-log/{id}` | - | `SyncLogDetailRespVO` | `integration:sync-log:detail` |
| 重试 | POST | `/admin/integration/sync-log/{id}/retry` | - | `Boolean` | `integration:sync-log:retry` |

## 6. 验收标准

- Secret 不允许在列表完整展示。
- 重置 Secret 有二次确认和审计日志。
- 失败日志详情必须展示错误码、traceId、请求路径、响应摘要。
- 重试操作必须受状态和权限控制。
