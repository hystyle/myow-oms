# 管理端页面：报价合同、账单与扣费流水

## 1. 页面定位

财务中心用于维护客户报价合同、费用规则、账单、扣费流水和对账状态，强调费用可信、规则可追溯、扣费可解释。

## 2. 页面清单

| 页面 | 路由 | 权限前缀 | 优先级 |
| --- | --- | --- | --- |
| 报价合同 | `/admin/finance/quote-contract` | `finance:quote-contract` | P0 |
| 账单管理 | `/admin/finance/bill` | `finance:bill` | P0 |
| 扣费流水 | `/admin/finance/charge-flow` | `finance:charge-flow` | P0 |
| 对账记录 | `/admin/finance/reconcile-record` | `finance:reconcile-record` | P1 |

## 3. 报价合同关键设计

### 查询条件

| 字段 | Label | 类型 | 组件 | 默认展示 |
| --- | --- | --- | --- | --- |
| contractNo | 合同编号 | string | input | 是 |
| customerName | 客户 | string | input | 是 |
| contractType | 合同类型 | enum | select | 是 |
| status | 状态 | enum | select | 是 |
| effectiveTime | 生效时间 | dateRange | date-picker | 是 |

### 表格列

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| 合同编号 | contractNo | 支持复制，点击详情 |
| 客户 | customerName | - |
| 合同类型 | contractType | 出库 / 入库 / 仓储 / 尾程 / 综合 |
| 币种 | currency | ISO 币种 |
| 状态 | status | 草稿 / 生效中 / 已停用 / 已过期 |
| 生效时间 | effectiveTime | yyyy-MM-dd HH:mm:ss |
| 失效时间 | expiredTime | yyyy-MM-dd HH:mm:ss |
| 操作 | actions | 详情、编辑、发布、停用、复制 |

### 多包裹优惠规则

报价合同费用规则必须支持多包裹优惠配置：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| enableMultiPackageDiscount | boolean | 是否开启多包裹优惠 |
| firstPackageSelectMode | enum | 首件选择方式，默认 `HIGHEST_FEE` |
| continuedPackageDiscountRate | decimal | 续件折扣率，如 `50%` |

计费展示规则：

1. 未开启多包裹优惠时，沿用原有计费逻辑。
2. 开启后，系统先按包裹明细计算每件原始费用。
3. 按原始费用从高到低排序。
4. 原始费用最高的一件作为首件，按 100% 计费。
5. 从第 2 件起，按配置的续件折扣率计费。
6. 汇总后生成最终费用。
7. 账单和扣费流水需要保存费用规则快照。

## 4. 账单管理

### 查询条件

| 字段 | Label | 类型 | 默认展示 |
| --- | --- | --- | --- |
| billNo | 账单编号 | input | 是 |
| customerName | 客户 | input | 是 |
| relatedNo | 关联单号 | input | 是 |
| billStatus | 账单状态 | select | 是 |
| reconcileStatus | 对账状态 | select | 是 |
| createdTime | 创建时间 | dateRange | 是 |

### 表格列

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| 账单编号 | billNo | 支持复制，点击详情 |
| 客户 | customerName | - |
| 关联单号 | relatedNo | 出库单 / 入库单 / 费用单 |
| 费用类型 | feeType | 标签 |
| 币种 | currency | - |
| 应收金额 | receivableAmount | 金额右对齐 |
| 已收金额 | receivedAmount | 金额右对齐 |
| 待对账金额 | pendingReconcileAmount | 金额右对齐 |
| 账单状态 | billStatus | 状态标签 |
| 对账状态 | reconcileStatus | 待对账 / 已对账 |
| 创建时间 | createdTime | - |
| 操作 | actions | 详情、标记已对账、导出 |

## 5. 扣费流水

扣费流水是账单可信的基础，只能新增系统记录，不允许人工编辑。

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| 流水号 | flowNo | 支持复制 |
| 客户 | customerName | - |
| 关联单号 | relatedNo | 支持复制 |
| 费用类型 | feeType | 标签 |
| 计费规则快照 | ruleSnapshot | 点击查看 |
| 币种 | currency | - |
| 扣费金额 | chargeAmount | 金额右对齐 |
| 扣费前余额 | beforeBalance | 金额右对齐 |
| 扣费后余额 | afterBalance | 金额右对齐 |
| 发生时间 | occurredTime | yyyy-MM-dd HH:mm:ss |

## 6. API 映射

| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| 报价分页 | POST | `/admin/finance/quote-contract/page` | `QuoteContractPageReqVO` | `PageResult<QuoteContractRespVO>` | `finance:quote-contract:query` |
| 报价保存 | POST | `/admin/finance/quote-contract` | `QuoteContractSaveReqVO` | `Long` | `finance:quote-contract:add` |
| 报价发布 | POST | `/admin/finance/quote-contract/{id}/publish` | - | `Boolean` | `finance:quote-contract:publish` |
| 账单分页 | POST | `/admin/finance/bill/page` | `BillPageReqVO` | `PageResult<BillRespVO>` | `finance:bill:query` |
| 标记已对账 | POST | `/admin/finance/bill/batch-reconcile` | `BillBatchReconcileReqVO` | `Boolean` | `finance:bill:reconcile` |
| 扣费流水分页 | POST | `/admin/finance/charge-flow/page` | `ChargeFlowPageReqVO` | `PageResult<ChargeFlowRespVO>` | `finance:charge-flow:query` |

## 7. 验收标准

- 所有金额必须带币种。
- 账单和扣费流水必须能查看计费规则快照。
- 多包裹优惠规则必须保存配置快照。
- 标记已对账必须二次确认并展示影响条数。
- 扣费流水不可编辑、不可删除。
