# 管理端页面：出库异常处理

## 1. 页面定位

用于运营和客服在客户发现问题前主动识别并处理出库异常，重点关注阻断发货、物流长时间未更新、WMS 下发失败、面单异常、欠费挂起等问题。

## 2. 路由与文件

| 项 | 值 |
| --- | --- |
| 端 | 管理端 |
| 路由 | `/admin/oms/outbound-exception` |
| 页面组件 | `src/views/admin/oms/outbound-exception/index.vue` |
| API 文件 | `src/api/oms/outboundException.ts` |
| 权限前缀 | `oms:outbound-exception` |
| 优先级 | P0 |

## 3. 页面布局

```text
KPI: 待处理异常 / 高风险异常 / 已超时 / 今日已处理
QueryPanel
Table
Right Drawer: 异常详情 + 处理建议 + 客户侧展示文案
```

## 4. 查询条件

| 字段 | Label | 类型 | 组件 | 默认展示 | 说明 |
| --- | --- | --- | --- | --- | --- |
| keyword | 单号 | string | input | 是 | 出库单号、跟踪号、客户单号 |
| customerName | 客户 | string | input | 是 | 客户名称 |
| carrierName | 承运商 | string | select | 是 | 可远程搜索 |
| logisticsProductId | 物流产品 | string | select | 是 | - |
| logisticsChannelId | 物流渠道 | string | select | 是 | - |
| logisticsStatus | 物流状态 | enum | select | 是 | - |
| exceptionType | 异常类型 | enum | select | 是 | - |
| riskLevel | 风险等级 | enum | select | 是 | 高 / 中 / 低 |
| ownerUser | 负责人 | string | input | 否 | 销售 / 客服 / 运营 |
| createdTime | 发现时间 | dateRange | date-picker | 是 | 默认最近 7 天 |

## 5. 表格列

| 列名 | 字段 | 宽度 | 展示规则 |
| --- | --- | --- | --- |
| 异常单号 | exceptionNo | 180 | 支持复制，点击详情 |
| 出库单号 | outboundNo | 180 | 支持复制 |
| 跟踪号 | trackingNo | 180 | 支持复制 |
| 客户 | customerName | 160 | 超长省略 |
| 承运商 / 渠道 | carrierChannel | 180 | 承运商 + 渠道 |
| 异常类型 | exceptionType | 150 | 状态标签 |
| 风险等级 | riskLevel | 100 | 高风险红色 |
| 当前进展 | currentProgress | 260 | 展示温和描述 |
| 是否需要客户操作 | customerActionRequired | 130 | 是 / 否 / 待判断 |
| 发现时间 | detectedTime | 170 | yyyy-MM-dd HH:mm:ss |
| 操作 | actions | 220 | 查看、更新物流、更换渠道、取消跟踪、标记已处理 |

## 6. 异常类型

| 值 | 中文 | 颜色 | 客户侧默认文案 | 客户是否需操作 |
| --- | --- | --- | --- | --- |
| LOGISTICS_NOT_UPDATED | 物流未更新 | warning | 物流状态正在更新中，系统会持续关注 | 否 |
| LABEL_FAILED | 面单获取失败 | danger | 物流面单暂未生成成功 | 视情况 |
| WMS_REJECTED | 仓库拒单 | danger | 仓库暂未接收该订单 | 否 |
| ADDRESS_INVALID | 地址校验失败 | danger | 收件地址需要确认 | 是 |
| OUT_OF_STOCK | 库存不足 | danger | 库存不足，订单暂无法发货 | 是 |
| BALANCE_HOLD | 欠费挂起 | danger | 账户余额不足，订单暂无法继续履约 | 是 |
| TRACKING_CANCELLED | 跟踪取消 | gray | 该跟踪已取消 | 否 |

## 7. 行操作

| 操作 | 权限码 | 状态条件 | 说明 |
| --- | --- | --- | --- |
| 查看详情 | `oms:outbound-exception:detail` | - | 查看异常详情和处理记录 |
| 更新物流 | `oms:outbound-exception:update-logistics` | 有跟踪号 | 主动拉取最新轨迹 |
| 更换渠道 | `oms:outbound-exception:change-channel` | 未出库 | 更换物流渠道并重新打单 |
| 取消跟踪 | `oms:outbound-exception:cancel-tracking` | 未出库且有跟踪号 | 取消当前跟踪号 |
| 标记已处理 | `oms:outbound-exception:resolve` | 未处理 | 填写处理备注 |

## 8. 详情结构

| 区块 | 内容 |
| --- | --- |
| 异常摘要 | 异常类型、风险等级、发现时间、是否超时 |
| 关联订单 | 出库单号、客户单号、客户、仓库、物流产品 |
| 内部诊断 | 原始错误、接口返回、任务日志、traceId |
| 客户侧展示 | 客户可见状态、客户提示文案、是否需要客户操作 |
| 处理动作 | 更新物流、更换渠道、取消跟踪、标记已处理 |
| 处理记录 | 操作人、动作、备注、时间 |

## 9. API 映射

| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| 分页查询 | POST | `/admin/oms/outbound-exception/page` | `OutboundExceptionPageReqVO` | `PageResult<OutboundExceptionRespVO>` | `oms:outbound-exception:query` |
| 详情 | GET | `/admin/oms/outbound-exception/{id}` | - | `OutboundExceptionDetailRespVO` | `oms:outbound-exception:detail` |
| 更新物流 | POST | `/admin/oms/outbound-exception/{id}/update-logistics` | - | `Boolean` | `oms:outbound-exception:update-logistics` |
| 更换渠道 | POST | `/admin/oms/outbound-exception/{id}/change-channel` | `ChangeChannelReqVO` | `Boolean` | `oms:outbound-exception:change-channel` |
| 取消跟踪 | POST | `/admin/oms/outbound-exception/{id}/cancel-tracking` | `CancelTrackingReqVO` | `Boolean` | `oms:outbound-exception:cancel-tracking` |
| 标记已处理 | POST | `/admin/oms/outbound-exception/{id}/resolve` | `ResolveExceptionReqVO` | `Boolean` | `oms:outbound-exception:resolve` |

## 10. 验收标准

- 客户侧文案不得直接展示接口异常、系统同步失败、承运商 API 异常。
- 异常详情必须区分内部诊断和客户展示。
- 更新物流、更换渠道、取消跟踪均需记录操作日志。
- 高风险异常必须在列表中突出。
