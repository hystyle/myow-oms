# 管理端模块：海外仓 OMS

海外仓 OMS 是管理端核心业务模块，覆盖 SKU、库存、入库、出库、异常、包裹跟踪、仓库、物流产品。

## 页面清单

| 页面 | 路由 | 优先级 |
| --- | --- | --- |
| SKU 管理 | /admin/oms/sku | P0 |
| 库存管理 | /admin/oms/inventory | P0 |
| 入库预报 | /admin/oms/inbound-asn | P0 |
| 出库订单 | /admin/oms/outbound | P0 |
| 出库异常 | /admin/oms/outbound-exception | P0 |
| 包裹跟踪 | /admin/oms/package-tracking | P1 |
| 仓库管理 | /admin/oms/warehouse | P1 |
| 物流产品 | /admin/oms/logistics-product | P1 |

## 实现要求

- 必须遵守 `../../../00-global/` 下的全局约束。
- 具体页面必须声明：页面目标、路由、权限码、查询条件、表格列、状态枚举、API 映射、空态/异常态、验收标准。
- 管理端采用统一浅色产品视觉，但保留高密度查询、表格、批量操作、内部异常和审计追踪能力。
