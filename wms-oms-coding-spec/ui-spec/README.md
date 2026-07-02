# MYOW UI/UE 设计规范包

> 本目录用于承载 MYOW Platform 的前端 UI/UE 设计规范。

## 文档结构

| 文档 | 用途 |
| --- | --- |
| [00-design-system.md](./00-design-system.md) | 全局设计系统、布局、主题、组件使用原则 |
| [01-admin-ui-ue-spec.md](./01-admin-ui-ue-spec.md) | 管理端整体 UI/UE、信息架构、导航、权限可见性 |
| [02-admin-page-spec.md](./02-admin-page-spec.md) | 管理端用户中心、系统中心页面级设计 |
| [03-client-portal-ui-ue-spec.md](./03-client-portal-ui-ue-spec.md) | 客户端门户 UI/UE 设计 |
| [04-component-interaction-spec.md](./04-component-interaction-spec.md) | 通用页面范式、组件、反馈、异常、响应式规则 |

## 设计定位

MYOW 是面向海外仓、货代 OMS、客户门户和内部运营的业务平台。UI 不做营销型大屏，也不做装饰性后台模板，而是强调：

- 高频操作效率：列表、筛选、批量动作、详情追踪必须清晰稳定。
- 数据可信：状态、审计、权限、异常原因必须可解释。
- 双端隔离：管理端服务内部人员，客户端服务外部客户，登录、导航、用词和数据可见性必须区分。
- 规则可生成：接口、权限码、OpenAPI 注解、页面字段应支持后续生成前端页面或辅助前端开发。

## 与旧文档关系

旧文档包括：

- `../myow-admin-ui-ue-design.html`
- `../myow-admin-ui-page-design.html`
- `../myow-client-portal-ui-ue-design.html`
- `../UI-UE-Spec.md`

本目录是这些文档的重构版本。后续新增 UI/UE 设计应优先写入 `ui-spec`，旧 HTML 文档只作为历史参考。

## 使用规则

1. 新页面设计必须先确认所属端：管理端或客户端。
2. 管理端页面优先使用“查询区 + 操作区 + 数据表格 + 抽屉/弹窗”的密集业务结构。
3. 客户端页面优先使用“业务工作台 + 自助流程 + 明确状态解释”的任务结构。
4. 所有接口实现必须配套 springdoc 注解，接口说明要能支撑 API 文档和前端页面生成。
5. 前端代码实现时必须同时遵守 `../../myow-frontend/spec/frontend-standard.md`。

