# 客户端页面：账单、扣费流水与充值

## 1. 页面定位

用于客户看清为什么扣费、扣了多少、是否影响发货。财务页面要强调可信、透明、可下载、可追溯。

## 2. 页面清单

| 页面 | 路由 | 权限前缀 | 优先级 |
| --- | --- | --- | --- |
| 账单中心 | `/client/billing/bill` | `client:bill` | P0 |
| 扣费流水 | `/client/billing/charge-flow` | `client:charge-flow` | P0 |
| 充值记录 | `/client/billing/recharge` | `client:recharge` | P1 |
| 发票申请 | `/client/billing/invoice` | `client:invoice` | P2 |

## 3. 账单中心

### 顶部摘要

| 卡片 | 字段 | 说明 |
| --- | --- | --- |
| 当前余额 | balance | 账户可用余额 |
| 信用额度 | creditLimit | 如无则展示 `-` |
| 待确认账单 | pendingBillAmount | 待查看或待对账金额 |
| 本月费用 | monthChargeAmount | 当前自然月费用 |

### 查询条件

| 字段 | Label | 类型 | 默认展示 |
| --- | --- | --- | --- |
| billNo | 账单编号 | input | 是 |
| relatedNo | 关联单号 | input | 是 |
| feeType | 费用类型 | select | 是 |
| billStatus | 状态 | select | 是 |
| createdTime | 创建时间 | dateRange | 是 |

### 表格列

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| 账单编号 | billNo | 支持复制，点击详情 |
| 关联单号 | relatedNo | 出库单 / 入库单 / 充值单 |
| 费用类型 | feeType | 标签 |
| 费用说明 | feeDescription | 客户可理解说明 |
| 币种 | currency | - |
| 金额 | amount | 金额右对齐 |
| 状态 | billStatus | 状态标签 |
| 创建时间 | createdTime | - |
| 操作 | actions | 详情、导出 |

## 4. 扣费流水

扣费流水必须展示计费规则快照入口，但不展示内部供应商成本。

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| 流水号 | flowNo | 支持复制 |
| 关联单号 | relatedNo | 支持复制 |
| 费用类型 | feeType | 标签 |
| 费用说明 | feeDescription | 客户可理解说明 |
| 扣费金额 | chargeAmount | 金额 + 币种 |
| 扣费后余额 | afterBalance | 金额 + 币种 |
| 发生时间 | occurredTime | yyyy-MM-dd HH:mm:ss |
| 操作 | actions | 查看规则快照 |

## 5. API 映射

| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| 账单摘要 | GET | `/client/billing/summary` | - | `ClientBillingSummaryRespVO` | `client:bill:query` |
| 账单分页 | POST | `/client/billing/bill/page` | `ClientBillPageReqVO` | `PageResult<ClientBillRespVO>` | `client:bill:query` |
| 账单详情 | GET | `/client/billing/bill/{id}` | - | `ClientBillDetailRespVO` | `client:bill:detail` |
| 扣费流水 | POST | `/client/billing/charge-flow/page` | `ClientChargeFlowPageReqVO` | `PageResult<ClientChargeFlowRespVO>` | `client:charge-flow:query` |
| 规则快照 | GET | `/client/billing/rule-snapshot/{id}` | - | `ClientRuleSnapshotRespVO` | `client:charge-flow:detail` |

## 6. 验收标准

- 所有金额必须带币种。
- 费用说明必须面向客户，不展示内部成本、内部规则代码。
- 欠费或余额不足必须给出充值入口。
- 账单导出按当前筛选条件导出。
