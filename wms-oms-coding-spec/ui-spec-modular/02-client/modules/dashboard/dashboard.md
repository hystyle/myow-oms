# 客户端页面：工作台

## 1. 页面定位

客户工作台用于回答客户每天最关心的问题：今天需要处理什么、哪些订单被阻断、库存是否有风险、账单余额是否影响履约。

## 2. 路由与文件

| 项 | 值 |
| --- | --- |
| 端 | 客户端 |
| 路由 | `/client/dashboard` |
| 页面组件 | `src/views/client/dashboard/index.vue` |
| API 文件 | `src/api/client/dashboard.ts` |
| 权限前缀 | `client:dashboard` |
| 优先级 | P0 |

## 3. 页面布局

```text
KPI Strip: 待处理订单 / 库存预警 / 待确认账单 / 异常工单
Main: 待办事项列表
Side: 余额风险 / 公告 / 最近同步
Chart: 发货趋势 / 热销 SKU / 库存周转
```

## 4. KPI 卡片

| 卡片 | 字段 | 点击跳转 | 说明 |
| --- | --- | --- | --- |
| 待处理订单 | pendingOrderCount | `/client/outbound?status=pending` | 需要客户确认或补充信息 |
| 库存预警 | inventoryWarningCount | `/client/inventory?status=warning` | 低库存、缺货 |
| 待确认账单 | pendingBillCount | `/client/billing/bill?status=pending` | 待查看或待对账 |
| 异常工单 | openTicketCount | `/client/support/ticket?status=open` | 待客户回复、处理中 |

## 5. 待办事项字段

| 字段 | Label | 展示规则 |
| --- | --- | --- |
| todoType | 类型 | 订单 / 库存 / 账单 / 工单 |
| title | 标题 | 面向客户的温和文案 |
| impact | 影响 | 如“可能影响发货” |
| actionText | 操作按钮 | 如“确认地址”“去充值”“查看详情” |
| relatedNo | 关联单号 | 支持复制 |
| createdTime | 产生时间 | yyyy-MM-dd HH:mm:ss |

## 6. 客户端文案规则

- 不展示“WMS API 异常”“任务执行失败”“接口超时”等内部词。
- 使用“系统正在同步”“仓库处理中”“需要确认地址”等客户可理解表达。
- 每个待办必须告诉客户是否需要操作。

## 7. API 映射

| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| 工作台统计 | GET | `/client/dashboard/summary` | - | `ClientDashboardSummaryRespVO` | `client:dashboard:query` |
| 待办列表 | POST | `/client/dashboard/todo/page` | `ClientTodoPageReqVO` | `PageResult<ClientTodoRespVO>` | `client:dashboard:query` |
| 发货趋势 | GET | `/client/dashboard/shipping-trend` | `TrendReqVO` | `TrendRespVO` | `client:dashboard:query` |

## 8. 验收标准

- 所有卡片可点击进入对应处理页。
- 待办事项必须有“是否需要客户操作”的明确表达。
- 客户端不得展示内部异常原因。
- 余额风险必须突出余额、信用额度、欠费状态。
