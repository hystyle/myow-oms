# 客户端页面：SKU 与库存看板

## 1. 页面定位

用于客户维护商品资料、查看库存状态、查询库存流水。客户端只展示客户自身数据，不展示内部库存成本、供应商报价、其他客户库存。

## 2. 页面清单

| 页面 | 路由 | 权限前缀 | 优先级 |
| --- | --- | --- | --- |
| SKU 管理 | `/client/sku` | `client:sku` | P0 |
| 库存看板 | `/client/inventory` | `client:inventory` | P0 |
| 库存流水 | `/client/inventory-flow` | `client:inventory-flow` | P1 |

## 3. SKU 管理

### 查询条件

| 字段 | Label | 类型 | 默认展示 |
| --- | --- | --- | --- |
| skuCode | SKU | input | 是 |
| sellerSku | Seller SKU | input | 是 |
| productName | 商品名称 | input | 是 |
| status | 状态 | select | 是 |
| createdTime | 创建时间 | dateRange | 是 |

### 表格列

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| 商品信息 | productInfo | SKU、Seller SKU、商品名称，SKU 支持复制 |
| 分类 | categoryName | - |
| 申报价值 | declaredValue | 金额 + 币种 |
| HS Code | hsCode | 支持复制 |
| 尺重 | sizeWeight | 长宽高 + 重量 |
| 敏感属性 | sensitiveTags | 标签组 |
| 状态 | status | 状态标签 |
| 操作 | actions | 详情、编辑、同步、查看复测记录 |

## 4. 库存看板

### 查询条件

| 字段 | Label | 类型 | 默认展示 |
| --- | --- | --- | --- |
| warehouseId | 仓库 | select | 是 |
| skuCode | SKU | input | 是 |
| productName | 商品名称 | input | 是 |
| inventoryStatus | 库存状态 | select | 是 |

### 库存字段

| 字段 | 中文 | 说明 |
| --- | --- | --- |
| inboundQty | 在途库存 | 已创建入库但未上架 |
| availableQty | 在库可用 | 可用于创建出库单 |
| allocatedQty | 待出库 | 已被订单占用 |
| frozenQty | 冻结/锁定 | 不可用库存 |
| defectiveQty | 坏品/瑕疵 | 不可正常销售 |
| shippedQty | 已出库 | 历史累计或筛选期内已出库 |

可用库存口径必须在页面提示：

```text
可用库存 = 在库良品库存 - 已锁定库存 - 冻结库存。具体以仓库最新同步结果为准。
```

## 5. API 映射

| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| SKU 分页 | POST | `/client/sku/page` | `ClientSkuPageReqVO` | `PageResult<ClientSkuRespVO>` | `client:sku:query` |
| SKU 创建 | POST | `/client/sku` | `ClientSkuCreateReqVO` | `Long` | `client:sku:add` |
| SKU 编辑 | PUT | `/client/sku/{id}` | `ClientSkuUpdateReqVO` | `Boolean` | `client:sku:update` |
| 库存分页 | POST | `/client/inventory/page` | `ClientInventoryPageReqVO` | `PageResult<ClientInventoryRespVO>` | `client:inventory:query` |
| 库存流水 | POST | `/client/inventory-flow/page` | `ClientInventoryFlowPageReqVO` | `PageResult<ClientInventoryFlowRespVO>` | `client:inventory-flow:query` |

## 6. 验收标准

- 客户端不展示内部成本价、供应商报价、其他客户库存。
- 库存数量必须解释口径。
- 缺货和低库存需要明确提示下一步：补货、调整订单、联系客服。
- SKU、Seller SKU、HS Code 支持复制。
