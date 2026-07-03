# 管理端页面：入库预报

# 管理端页面：入库预报与客户管理

## 1. 入库预报

### 路由

| 项 | 值 |
| --- | --- |
| 路由 | `/admin/oms/inbound-asn` |
| 权限前缀 | `oms:inbound-asn` |
| 优先级 | P0 |

### 查询条件

| 字段 | Label | 类型 | 默认展示 |
| --- | --- | --- | --- |
| asnNo | ASN 单号 | input | 是 |
| customerName | 客户 | input | 是 |
| warehouseId | 仓库 | select | 是 |
| transportMode | 运输方式 | select | 是 |
| status | 状态 | select | 是 |
| createdTime | 创建时间 | dateRange | 是 |

### 表格列

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| ASN 单号 | asnNo | 支持复制，点击详情 |
| 客户 | customerName | - |
| 仓库 | warehouseName | - |
| 运输方式 | transportMode | 快递 / 卡车 / 海运 / 空运 |
| 箱数 | cartonCount | 数字右对齐 |
| SKU 数 | skuCount | 数字右对齐 |
| 状态 | status | 状态标签 |
| 到货差异 | differenceStatus | 无差异 / 有差异 / 待确认 |
| 创建时间 | createdTime | - |
| 操作 | actions | 详情、确认差异、取消 |

### 状态枚举

| 值 | 中文 | 颜色 | 允许操作 |
| --- | --- | --- | --- |
| DRAFT | 草稿 | gray | 编辑、提交、取消 |
| SUBMITTED | 已提交 | info | 详情、取消 |
| ARRIVED | 已到仓 | info | 详情 |
| COUNTING | 清点中 | warning | 详情 |
| DIFFERENCE_PENDING | 差异待确认 | danger | 确认差异 |
| PUTAWAY | 已上架 | success | 详情 |
| CANCELLED | 已取消 | gray | 详情 |

### API 映射

| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| 分页查询 | POST | `/admin/oms/inbound-asn/page` | `InboundAsnPageReqVO` | `PageResult<InboundAsnRespVO>` | `oms:inbound-asn:query` |
| 详情 | GET | `/admin/oms/inbound-asn/{id}` | - | `InboundAsnDetailRespVO` | `oms:inbound-asn:detail` |
| 确认差异 | POST | `/admin/oms/inbound-asn/{id}/confirm-difference` | `ConfirmDifferenceReqVO` | `Boolean` | `oms:inbound-asn:confirm-difference` |
