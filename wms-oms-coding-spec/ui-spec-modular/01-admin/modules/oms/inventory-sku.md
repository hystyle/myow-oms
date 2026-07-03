# 管理端页面：SKU 与库存管理

## 1. 页面定位

用于内部运营维护 SKU 基础资料、查看库存聚合、追踪库存流水和处理库存异常。SKU 与库存可以拆成两个菜单，但设计规则保持一致。

## 2. 页面清单

| 页面 | 路由 | 权限前缀 | 优先级 |
| --- | --- | --- | --- |
| SKU 管理 | `/admin/oms/sku` | `oms:sku` | P0 |
| 库存管理 | `/admin/oms/inventory` | `oms:inventory` | P0 |
| 库存流水 | `/admin/oms/inventory-flow` | `oms:inventory-flow` | P1 |

## 3. SKU 查询条件

| 字段 | Label | 类型 | 组件 | 默认展示 |
| --- | --- | --- | --- | --- |
| customerName | 客户 | string | input | 是 |
| skuCode | SKU | string | input | 是 |
| sellerSku | Seller SKU | string | input | 是 |
| productName | 商品名称 | string | input | 是 |
| categoryId | 商品分类 | string | cascader | 是 |
| status | 状态 | enum | select | 是 |
| sensitiveType | 敏感属性 | enum | select | 否 |
| createdTime | 创建时间 | dateRange | date-picker | 是 |

## 4. SKU 表格列

| 列名 | 字段 | 宽度 | 展示规则 |
| --- | --- | --- | --- |
| 商品信息 | productInfo | 260 | SKU、Seller SKU、商品名称，SKU 支持复制 |
| 客户 | customerName | 160 | 超长省略 |
| 分类 | categoryName | 140 | - |
| 申报价值 | declaredValue | 120 | 金额 + 币种 |
| HS Code | hsCode | 120 | 支持复制 |
| 尺重 | sizeWeight | 180 | 长宽高 + 重量 |
| 敏感属性 | sensitiveTags | 160 | 标签组 |
| 状态 | status | 100 | 状态标签 |
| 更新时间 | updatedTime | 170 | yyyy-MM-dd HH:mm:ss |
| 操作 | actions | 180 | 详情、编辑、同步 WMS、冻结 |

## 5. SKU 状态枚举

| 值 | 中文 | 颜色 | 允许操作 |
| --- | --- | --- | --- |
| DRAFT | 草稿 | gray | 编辑、提交 |
| AVAILABLE | 可用 | success | 编辑、同步 WMS、冻结 |
| MEASURED | 已实测 | success | 编辑、同步 WMS、冻结 |
| FROZEN | 冻结 | danger | 解冻、详情 |

## 6. 库存查询条件

| 字段 | Label | 类型 | 组件 | 默认展示 |
| --- | --- | --- | --- | --- |
| customerName | 客户 | string | input | 是 |
| warehouseId | 仓库 | string | select | 是 |
| skuCode | SKU | string | input | 是 |
| productName | 商品名称 | string | input | 是 |
| inventoryStatus | 库存状态 | enum | select | 是 |
| batchNo | 批次号 | string | input | 否 |

## 7. 库存表格列

| 列名 | 字段 | 宽度 | 展示规则 |
| --- | --- | --- | --- |
| SKU | skuCode | 180 | 支持复制，点击详情 |
| 商品名称 | productName | 220 | 超长省略 |
| 客户 | customerName | 160 | - |
| 仓库 | warehouseName | 140 | - |
| 在途库存 | inboundQty | 100 | 数字右对齐 |
| 在库可用 | availableQty | 100 | 数字右对齐 |
| 待出库 | allocatedQty | 100 | 数字右对齐 |
| 冻结/锁定 | frozenQty | 110 | 数字右对齐 |
| 坏品/瑕疵 | defectiveQty | 110 | 数字右对齐 |
| 已出库 | shippedQty | 100 | 数字右对齐 |
| 库存状态 | inventoryStatus | 120 | 正常 / 低库存 / 缺货 |
| 更新时间 | updatedTime | 170 | - |
| 操作 | actions | 160 | 查看流水、查看明细 |

## 8. API 映射

| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| SKU 分页 | POST | `/admin/oms/sku/page` | `SkuPageReqVO` | `PageResult<SkuRespVO>` | `oms:sku:query` |
| SKU 详情 | GET | `/admin/oms/sku/{id}` | - | `SkuDetailRespVO` | `oms:sku:detail` |
| SKU 编辑 | PUT | `/admin/oms/sku/{id}` | `SkuUpdateReqVO` | `Boolean` | `oms:sku:update` |
| 同步 WMS | POST | `/admin/oms/sku/{id}/sync-wms` | - | `Boolean` | `oms:sku:sync-wms` |
| 库存分页 | POST | `/admin/oms/inventory/page` | `InventoryPageReqVO` | `PageResult<InventoryRespVO>` | `oms:inventory:query` |
| 库存流水 | POST | `/admin/oms/inventory-flow/page` | `InventoryFlowPageReqVO` | `PageResult<InventoryFlowRespVO>` | `oms:inventory-flow:query` |

## 9. 验收标准

- SKU、客户、仓库、批次等 ID 字段均按字符串处理。
- 库存数量右对齐，负数或异常数量必须突出。
- 可用库存必须有口径说明。
- 库存流水不可编辑，只能查询和导出。
