# MYOW UI/UE 设计规范包（模块化版）

本目录用于承载 MYOW Platform 的前端 UI/UE 设计规范，按“公共约束 → 分端 → 模块 → 页面”组织，方便产品、设计、后端、前端和 coding agent 分层读取。

## 读取顺序

coding agent 或开发同学实现页面时，必须按以下顺序阅读：

1. `00-global/`：全局设计约束、双端统一风格、组件交互、权限、OpenAPI、coding agent 执行规则。
2. `01-admin/` 或 `02-client/`：确认当前页面属于管理端还是客户端。
3. 对应端下的 `modules/<module>/`：读取业务模块级规范。
4. 对应页面文件：读取页面字段、权限、状态、API、验收标准。
5. `03-page-inventory/page-inventory.md`：确认页面是否在清单中、路由是否一致。

## 目录结构

```text
myow-ui-spec-modular/
├── 00-global/                         # 全局公共约束
│   ├── 00-design-constraints.md        # 设计系统、视觉令牌、布局原则
│   ├── 01-unified-dual-end-style.md    # 双端统一浅色风格
│   ├── 02-component-interaction.md     # 查询区、表格、表单、抽屉、异常态
│   ├── 03-permission-data-visibility.md# 权限、数据隔离、字段脱敏
│   ├── 04-api-openapi-generation.md    # OpenAPI、DTO、枚举、错误码
│   ├── 05-coding-agent-rules.md        # coding agent 执行规则与禁止项
│   └── 06-page-template.md             # 新页面规格模板
├── 01-admin/                           # 管理端规范
│   ├── 00-admin-overview.md            # 管理端整体 UI/UE 和信息架构
│   └── modules/                        # 管理端按业务模块分包
│       ├── dashboard/
│       ├── user/
│       ├── system/
│       ├── customer/
│       ├── oms/
│       ├── firstmile/
│       ├── finance/
│       └── integration/
├── 02-client/                          # 客户端规范
│   ├── 00-client-overview.md           # 客户端整体 UI/UE 和信息架构
│   └── modules/                        # 客户端按业务模块分包
│       ├── dashboard/
│       ├── fulfillment/
│       ├── inventory/
│       ├── billing/
│       ├── developer/
│       ├── account/
│       └── support/
└── 03-page-inventory/                  # 总页面清单、路由、优先级
```

## 基本原则

- 根目录只放公共说明，不混放具体页面设计。
- 公共约束放在 `00-global`，两端都必须遵守。
- 管理端页面放在 `01-admin/modules`。
- 客户端页面放在 `02-client/modules`。
- 每个业务模块必须有自己的 `README.md`，说明模块定位、页面清单、实现注意事项。
- 具体页面文件应尽量按 `00-global/06-page-template.md` 的结构补齐。
- 管理端与客户端采用统一浅色产品视觉；差异体现在信息密度、权限、文案和操作能力上。
