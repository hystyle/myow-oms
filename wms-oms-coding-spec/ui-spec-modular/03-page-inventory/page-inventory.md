# 05. 页面清单、路由与优先级

## 1. 管理端页面清单

| 模块 | 页面 | 路由 | 优先级 | 页面类型 | 权限前缀 |
| --- | --- | --- | --- | --- | --- |
| 工作台 | 内部工作台 | `/admin/dashboard` | P0 | 看板 + 待办 | `admin:dashboard` |
| 用户中心 | 用户管理 | `/admin/system/user` | P0 | 树 + 列表 + 抽屉 | `system:user` |
| 用户中心 | 角色管理 | `/admin/system/role` | P0 | 左右分栏 + 权限树 | `system:role` |
| 用户中心 | 菜单权限 | `/admin/system/menu` | P0 | 树形表格 | `system:menu` |
| 用户中心 | 部门管理 | `/admin/system/dept` | P0 | 树形表格 | `system:dept` |
| 系统中心 | 定时任务 | `/admin/system/job` | P0 | 列表 + 日志 | `system:job` |
| 系统中心 | 通知公告 | `/admin/system/notice` | P0 | 列表 + 编辑抽屉 | `system:notice` |
| 系统中心 | 站点配置 | `/admin/system/config` | P0 | 列表 + 审计 | `system:config` |
| 客商中心 | 客户管理 | `/admin/customer/customer` | P1 | 列表 + 详情 | `customer:customer` |
| 客商中心 | 供应商管理 | `/admin/customer/supplier` | P1 | 列表 + 详情 | `customer:supplier` |
| 海外仓 OMS | SKU 管理 | `/admin/oms/sku` | P0 | 列表 + 抽屉 + 详情 | `oms:sku` |
| 海外仓 OMS | 库存管理 | `/admin/oms/inventory` | P0 | 列表 + 流水 | `oms:inventory` |
| 海外仓 OMS | 入库预报 | `/admin/oms/inbound-asn` | P0 | 列表 + 详情 | `oms:inbound-asn` |
| 海外仓 OMS | 出库订单 | `/admin/oms/outbound` | P0 | 列表 + 详情 + 异常处理 | `oms:outbound` |
| 海外仓 OMS | 出库异常 | `/admin/oms/outbound-exception` | P0 | 异常工作台 | `oms:outbound-exception` |
| 海外仓 OMS | 包裹跟踪 | `/admin/oms/package-tracking` | P1 | 列表 + 轨迹详情 | `oms:package-tracking` |
| 海外仓 OMS | 仓库管理 | `/admin/oms/warehouse` | P1 | 列表 + 配置 | `oms:warehouse` |
| 海外仓 OMS | 物流产品 | `/admin/oms/logistics-product` | P1 | 列表 + 配置 | `oms:logistics-product` |
| 头程管理 | 头程订单 | `/admin/firstmile/order` | P2 | 列表 + 节点追踪 | `firstmile:order` |
| 财务中心 | 报价合同 | `/admin/finance/quote-contract` | P0 | 列表 + 费用规则 | `finance:quote-contract` |
| 财务中心 | 账单管理 | `/admin/finance/bill` | P0 | 列表 + 明细 + 对账 | `finance:bill` |
| 财务中心 | 扣费流水 | `/admin/finance/charge-flow` | P0 | 列表 + 详情 | `finance:charge-flow` |
| 集成中心 | API 凭证 | `/admin/integration/api-credential` | P1 | 列表 + 密钥管理 | `integration:api-credential` |
| 集成中心 | 同步日志 | `/admin/integration/sync-log` | P1 | 日志列表 + 详情 | `integration:sync-log` |

## 2. 客户端页面清单

| 模块 | 页面 | 路由 | 优先级 | 页面类型 | 权限前缀 |
| --- | --- | --- | --- | --- | --- |
| 工作台 | 客户工作台 | `/client/dashboard` | P0 | KPI + 待办 + 风险 | `client:dashboard` |
| 订单履约 | 出库订单 | `/client/outbound` | P0 | 列表 + 详情 + 自助处理 | `client:outbound` |
| 订单履约 | 创建出库单 | `/client/outbound/create` | P0 | 表单 / 导入 | `client:outbound` |
| 订单履约 | 入库预报 | `/client/inbound-asn` | P0 | 列表 + 创建 + 差异确认 | `client:inbound-asn` |
| 商品库存 | SKU 管理 | `/client/sku` | P0 | 列表 + 新建 + 导入 | `client:sku` |
| 商品库存 | 库存看板 | `/client/inventory` | P0 | 聚合看板 + 流水 | `client:inventory` |
| 账单财务 | 账单中心 | `/client/billing/bill` | P0 | 账单 + 明细 + 导出 | `client:bill` |
| 账单财务 | 扣费流水 | `/client/billing/charge-flow` | P0 | 列表 + 费用解释 | `client:charge-flow` |
| 账单财务 | 充值记录 | `/client/billing/recharge` | P1 | 列表 + 充值入口 | `client:recharge` |
| 开发者中心 | API 凭证 | `/client/developer/api-credential` | P1 | 密钥 + 白名单 | `client:api-credential` |
| 开发者中心 | 调用日志 | `/client/developer/api-log` | P1 | 日志 + 错误追踪 | `client:api-log` |
| 账号权限 | 子账号 | `/client/account/member` | P1 | 列表 + 角色 | `client:member` |
| 客服支持 | 工单中心 | `/client/support/ticket` | P1 | 工单 + 回复 | `client:ticket` |
| 客服支持 | 公告 | `/client/support/notice` | P1 | 列表 + 详情 | `client:notice` |

## 3. 页面实现顺序

P0 第一批：登录、主框架、403/404、用户/角色/菜单/部门、客户端工作台、出库订单、库存、账单、报价合同。

P0 第二批：入库预报、SKU、出库异常、扣费流水、集成同步日志。

P1：客户/供应商、仓库、物流产品、API 凭证、工单、子账号。

P2：头程、发票、退货、VAS、对账争议高级能力。
