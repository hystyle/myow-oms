# 客户端页面：出库订单

## 1. 页面定位

用于客户自助创建、查询、追踪和处理出库订单。页面语言必须面向客户，避免暴露内部处理细节。

## 2. 路由与文件

| 项 | 值 |
| --- | --- |
| 端 | 客户端 |
| 列表路由 | `/client/outbound` |
| 创建路由 | `/client/outbound/create` |
| 页面组件 | `src/views/client/outbound/index.vue` |
| API 文件 | `src/api/client/outbound.ts` |
| 权限前缀 | `client:outbound` |
| 优先级 | P0 |

## 3. 查询条件

| 字段 | Label | 类型 | 组件 | 默认展示 | 说明 |
| --- | --- | --- | --- | --- | --- |
| keyword | 单号 | string | input | 是 | 出库单号、客户单号、跟踪号 |
| recipientName | 收件人 | string | input | 是 | - |
| skuCode | SKU | string | input | 是 | - |
| status | 状态 | enum | select | 是 | 客户侧状态 |
| warehouseId | 仓库 | string | select | 是 | 当前客户可用仓库 |
| createdTime | 创建时间 | dateRange | date-picker | 是 | 默认最近 30 天 |

## 4. 表格列

| 列名 | 字段 | 宽度 | 展示规则 |
| --- | --- | --- | --- |
| 出库单号 | outboundNo | 180 | 支持复制，点击详情 |
| 客户单号 | customerOrderNo | 180 | 支持复制 |
| 收件人 | recipientSummary | 220 | 姓名、国家、省州、邮编 |
| 商品 | skuSummary | 220 | SKU 数、总数量，点击查看明细 |
| 物流服务 | logisticsProductName | 180 | 不展示内部渠道异常 |
| 状态 | clientStatus | 130 | 客户侧状态标签 |
| 当前进展 | clientProgress | 260 | 温和说明 |
| 是否需要操作 | actionRequired | 120 | 是 / 否 |
| 创建时间 | createdTime | 170 | yyyy-MM-dd HH:mm:ss |
| 操作 | actions | 220 | 详情、取消、修改地址、下载面单、查看轨迹 |

## 5. 客户侧状态枚举

| 值 | 中文 | 颜色 | 客户说明 | 允许操作 |
| --- | --- | --- | --- | --- |
| DRAFT | 草稿 | gray | 订单尚未提交 | 编辑、删除 |
| PENDING | 处理中 | info | 系统正在处理订单 | 详情 |
| NEED_ADDRESS_CONFIRM | 地址待确认 | warning | 收件地址需要确认 | 修改地址 |
| NEED_PAYMENT | 余额不足 | danger | 账户余额不足，订单暂无法继续履约 | 去充值、取消 |
| OUT_OF_STOCK | 库存不足 | danger | 库存不足，订单暂无法发货 | 查看库存、取消 |
| WAITING_WAREHOUSE | 仓库处理中 | info | 仓库正在处理订单 | 详情 |
| SHIPPED | 已发货 | success | 包裹已发出 | 查看轨迹、下载面单 |
| CANCELLED | 已取消 | gray | 订单已取消 | 详情 |

## 6. 内部状态到客户状态映射

| 内部状态 / 异常 | 客户状态 | 客户文案 |
| --- | --- | --- |
| `WMS_PENDING` | `WAITING_WAREHOUSE` | 仓库正在处理订单 |
| `WMS_FAILED` | `PENDING` | 系统正在处理订单状态，如需帮助可联系客服 |
| `LABEL_PURCHASE_FAILED` | `PENDING` | 物流面单暂未生成成功，系统会继续处理 |
| `ADDRESS_VERIFY_FAILED` | `NEED_ADDRESS_CONFIRM` | 收件地址需要确认 |
| `BALANCE_HOLD` | `NEED_PAYMENT` | 账户余额不足，订单暂无法继续履约 |
| `OUT_OF_STOCK` | `OUT_OF_STOCK` | 库存不足，订单暂无法发货 |
| `SHIPPED` | `SHIPPED` | 包裹已发出 |

## 7. 行操作

| 操作 | 权限码 | 状态条件 | 说明 |
| --- | --- | --- | --- |
| 详情 | `client:outbound:detail` | - | 查看订单详情 |
| 取消 | `client:outbound:cancel` | 未发货、未取消 | 二次确认 |
| 修改地址 | `client:outbound:update-address` | 地址待确认或未下发仓库 | 修改收件信息 |
| 下载面单 | `client:outbound:download-label` | 面单已生成 | 下载文件 |
| 查看轨迹 | `client:outbound:track` | 有跟踪号 | 查看物流轨迹 |

## 8. 创建出库单表单

| 分组 | 字段 | Label | 类型 | 必填 | 说明 |
| --- | --- | --- | --- | --- | --- |
| 基本信息 | customerOrderNo | 客户单号 | input | 是 | 客户自有订单号 |
| 仓库物流 | warehouseId | 发货仓库 | select | 是 | 当前客户可用仓库 |
| 仓库物流 | logisticsProductId | 物流服务 | select | 是 | 展示客户可选服务 |
| 收件人 | recipientName | 收件人 | input | 是 | - |
| 收件人 | countryCode | 国家 | select | 是 | ISO 国家码 |
| 收件人 | postalCode | 邮编 | input | 是 | - |
| 收件人 | address1 | 地址1 | input | 是 | - |
| 商品 | items | 商品明细 | editable-table | 是 | SKU、数量 |
| 增值服务 | signatureRequired | 签名服务 | switch | 否 | 费用提示 |
| 增值服务 | insuranceRequired | 保险服务 | switch | 否 | 费用提示 |

## 9. API 映射

| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| 分页查询 | POST | `/client/outbound/page` | `ClientOutboundPageReqVO` | `PageResult<ClientOutboundRespVO>` | `client:outbound:query` |
| 创建 | POST | `/client/outbound` | `ClientOutboundCreateReqVO` | `Long` | `client:outbound:add` |
| 详情 | GET | `/client/outbound/{id}` | - | `ClientOutboundDetailRespVO` | `client:outbound:detail` |
| 取消 | POST | `/client/outbound/{id}/cancel` | `ClientCancelReqVO` | `Boolean` | `client:outbound:cancel` |
| 修改地址 | PUT | `/client/outbound/{id}/address` | `ClientAddressUpdateReqVO` | `Boolean` | `client:outbound:update-address` |
| 下载面单 | GET | `/client/outbound/{id}/label` | - | `File` | `client:outbound:download-label` |

## 10. 验收标准

- 客户端不展示 WMS、API、任务失败等内部词。
- 每个异常状态都说明客户是否需要操作。
- 取消、修改地址必须受状态限制。
- 创建订单时 SKU 数量不得超过客户可用库存。
