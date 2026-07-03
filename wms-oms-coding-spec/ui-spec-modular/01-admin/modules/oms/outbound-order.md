# 管理端页面：出库订单管理

## 1. 页面定位

用于内部运营、客服、仓库对接人员查看、筛选、处理客户出库订单，支持异常判断、订单追踪、重新下发、取消、导出等操作。

## 2. 路由与文件

| 项 | 值 |
| --- | --- |
| 端 | 管理端 |
| 菜单路径 | 海外仓 OMS / 出库订单 |
| 路由 | `/admin/oms/outbound` |
| 页面组件 | `src/views/admin/oms/outbound/index.vue` |
| API 文件 | `src/api/oms/outbound.ts` |
| 权限前缀 | `oms:outbound` |
| 优先级 | P0 |

## 3. 页面布局

```text
PageHeader: 出库订单 / 说明 / 导出
QueryPanel: 单号 / 客户 / 仓库 / 物流产品 / 状态 / 异常类型 / 创建时间
Toolbar: 批量导出 / 列设置 / 刷新
Table: 出库订单列表
Drawer: 出库订单详情
Modal: 取消订单、重新下发确认、包裹明细
```

## 4. 查询条件

| 字段 | Label | 类型 | 组件 | 默认展示 | 说明 |
| --- | --- | --- | --- | --- | --- |
| keyword | 单号 | string | input | 是 | 支持出库单号、客户单号、参考单号、跟踪号 |
| customerName | 客户 | string | input | 是 | 客户名称模糊搜索 |
| warehouseId | 仓库 | string | select | 是 | 远程搜索 |
| logisticsProductId | 物流产品 | string | select | 是 | 按当前客户可用产品过滤 |
| logisticsChannelId | 物流渠道 | string | select | 否 | 高级条件 |
| orderStatus | 订单状态 | enum | select | 是 | 多选 |
| exceptionType | 异常类型 | enum | select | 是 | 仅异常单有值 |
| createdTime | 创建时间 | dateRange | date-picker | 是 | 默认最近 7 天，可清空 |
| salesName | 销售 | string | input | 否 | 高级条件 |
| customerServiceName | 客服 | string | input | 否 | 高级条件 |

## 5. 工具栏

| 操作 | 类型 | 权限码 | 说明 |
| --- | --- | --- | --- |
| 导出 | 辅助按钮 | `oms:outbound:export` | 按当前筛选导出 |
| 批量重新下发 | 批量按钮 | `oms:outbound:resend` | 仅可选择 WMS 下发失败订单 |
| 列设置 | 图标按钮 | - | 保存用户列偏好 |
| 刷新 | 图标按钮 | - | 保持当前查询条件刷新 |

## 6. 表格列

| 列名 | 字段 | 宽度 | 固定 | 展示规则 |
| --- | --- | --- | --- | --- |
| 出库单号 | outboundNo | 180 | 左 | 支持复制，点击打开详情 |
| 客户单号 | customerOrderNo | 180 | 否 | 支持复制 |
| 客户 | customerName | 160 | 否 | 超长省略 |
| 仓库 | warehouseName | 140 | 否 | 展示云仓 / 实发仓可用 tooltip |
| 物流产品 | logisticsProductName | 160 | 否 | 超长省略 |
| 物流渠道 | logisticsChannelName | 160 | 否 | 超长省略 |
| 跟踪号 | trackingNo | 180 | 否 | 多个展示首个 +N，点击查看包裹明细 |
| 订单状态 | orderStatus | 120 | 否 | 状态标签 |
| 物流状态 | logisticsStatus | 120 | 否 | 客服判断用 |
| 异常 | exceptionType | 140 | 否 | 无异常展示 `-` |
| 包裹数 | packageCount | 90 | 否 | 点击打开包裹明细 |
| 创建时间 | createdTime | 170 | 否 | yyyy-MM-dd HH:mm:ss |
| 操作 | actions | 220 | 右 | 详情、重新下发、取消、更多 |

## 7. 状态枚举

| 值 | 中文 | 颜色 | 说明 | 允许操作 |
| --- | --- | --- | --- | --- |
| DRAFT | 草稿 | gray | 尚未提交 | 编辑、取消 |
| PENDING_AUDIT | 待审核 | warning | 等待内部审核 | 审核、取消 |
| PENDING_STOCK | 待锁库 | warning | 等待库存锁定 | 详情 |
| OUT_OF_STOCK | 缺货挂起 | danger | 库存不足 | 详情、取消 |
| BALANCE_HOLD | 欠费挂起 | danger | 客户余额不足 | 详情、取消 |
| PENDING_LABEL | 待打单 | warning | 等待面单生成 | 详情、取消 |
| WMS_PENDING | 待下发 WMS | info | 等待下发仓库 | 详情 |
| WMS_FAILED | WMS 下发失败 | danger | 下发仓库失败 | 详情、重新下发、取消 |
| WMS_SENT | 已下发 WMS | info | 仓库已接收 | 详情 |
| SHIPPED | 已出库 | success | 仓库已发货 | 详情、查看轨迹 |
| CANCELLED | 已取消 | gray | 订单已取消 | 详情 |

## 8. 行操作

| 操作 | 权限码 | 状态条件 | 交互 | 二次确认 |
| --- | --- | --- | --- | --- |
| 详情 | `oms:outbound:detail` | - | 打开详情抽屉 | 否 |
| 重新下发 | `oms:outbound:resend` | `orderStatus = WMS_FAILED` | 确认后调用接口 | 是 |
| 取消 | `oms:outbound:cancel` | 未出库且未取消 | 填写取消原因 | 是 |
| 查看轨迹 | `oms:outbound:track` | 有 trackingNo | 打开轨迹详情 | 否 |
| 下载面单 | `oms:outbound:download-label` | 面单已生成 | 下载文件 | 否 |

## 9. 详情结构

| 区块 | 内容 |
| --- | --- |
| 顶部摘要 | 出库单号、状态、客户、仓库、物流产品、创建时间 |
| 订单信息 | 客户单号、平台、店铺、参考单号、销售、客服 |
| 收件人 | 姓名、国家、省州、城市、邮编、地址、电话、地址类型 |
| 商品明细 | SKU、商品名称、数量、申报信息、属性标签 |
| 包裹信息 | 包裹号、尺寸、重量、跟踪号、WMS 下发状态 |
| 费用信息 | 出库费、尾程费、附加费、币种、计费规则快照入口 |
| 异常处理 | 异常类型、内部原因、处理建议、处理记录 |
| 操作日志 | 操作人、动作、时间、结果、traceId |

## 10. API 映射

| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| 分页查询 | POST | `/admin/oms/outbound/page` | `OutboundPageReqVO` | `PageResult<OutboundRespVO>` | `oms:outbound:query` |
| 详情 | GET | `/admin/oms/outbound/{id}` | - | `OutboundDetailRespVO` | `oms:outbound:detail` |
| 重新下发 | POST | `/admin/oms/outbound/{id}/resend` | `OutboundResendReqVO` | `Boolean` | `oms:outbound:resend` |
| 批量重新下发 | POST | `/admin/oms/outbound/batch-resend` | `OutboundBatchResendReqVO` | `BatchResultRespVO` | `oms:outbound:resend` |
| 取消 | POST | `/admin/oms/outbound/{id}/cancel` | `OutboundCancelReqVO` | `Boolean` | `oms:outbound:cancel` |
| 导出 | POST | `/admin/oms/outbound/export` | `OutboundPageReqVO` | `File` | `oms:outbound:export` |

## 11. 空态 / 异常态

| 场景 | 展示规则 | 动作 |
| --- | --- | --- |
| 无数据 | 当前筛选条件下没有出库订单 | 重置筛选 |
| 接口失败 | 展示错误信息和 traceId | 重试 |
| 无权限 | 页面 403 或隐藏按钮 | 联系管理员 |
| 部分包裹下发失败 | 详情展示失败包裹和原因 | 重新下发失败包裹 |

## 12. 验收标准

- 单号搜索支持出库单号、客户单号、参考单号、跟踪号。
- 查询和重置后分页行为正确。
- WMS_FAILED 才允许重新下发。
- 已出库订单不允许取消。
- 多包裹订单可查看每个包裹的 WMS 下发状态和跟踪号。
- 客户敏感信息按字段权限脱敏。
