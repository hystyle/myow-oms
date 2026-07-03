# 管理端模块：集成中心

集成中心负责 ERP/WMS/TMS/API 凭证、映射、同步日志和失败重试。

## 页面清单

| 页面 | 路由 | 优先级 |
| --- | --- | --- |
| API 凭证 | /admin/integration/api-credential | P1 |
| 同步日志 | /admin/integration/sync-log | P1 |
| 映射管理 | /admin/integration/mapping | P1 |

## 实现要求

- 必须遵守 `../../../00-global/` 下的全局约束。
- 具体页面必须声明：页面目标、路由、权限码、查询条件、表格列、状态枚举、API 映射、空态/异常态、验收标准。
- 管理端采用统一浅色产品视觉，但保留高密度查询、表格、批量操作、内部异常和审计追踪能力。
