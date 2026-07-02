# MYOW-Frontend 前端架构设计

## 目录

## 01总体原则

MYOW 前端应先满足后台管理端与客户门户的稳定开发，再逐步支持页面生成、开放平台和移动端。第一版不追求复杂微前端，优先保证权限、接口、页面范式和构建流程清晰。

- 管理端与客户端门户必须独立应用、独立路由、独立登录入口。

- 共享组件、请求工具、权限工具、OpenAPI 类型生成能力可以抽成公共包。

- 页面不直接调用裸接口，必须通过模块 service 层封装。

- 权限判断不写死角色名，统一消费后端返回的菜单、按钮和数据权限结果。

- 所有表格、表单、详情页遵守 UI/UE spec 中的加载态、空态、错误态、无权限态要求。

## 02应用拆分

前端先作为当前仓库下的单独顶层包 myow-frontend 存放，不混入 Java/Maven 模块。该目录内部采用 workspace 管理多个前端应用和共享包，后续可以整体迁出为独立前端仓库。

| 工程 | 类型 | 职责 | 是否第一版创建 |
| --- | --- | --- | --- |
| myow-admin-web | 应用 | 内部后台管理端，承载 user、system、后续业务管理页面。 | 是 |
| myow-client-web | 应用 | 客户门户，承载客户工作台、订单、库存、账单、API、账号。 | 是 |
| myow-ui | 共享包 | 业务无关组件、布局组件、表格表单封装、状态组件。 | 建议 |
| myow-api | 共享包 | OpenAPI 生成的 TypeScript 类型与请求函数。 | 建议 |
| myow-shared | 共享包 | 权限工具、请求拦截、日期金额格式化、字典枚举工具。 | 建议 |

推荐结论：第一版已按单独前端包创建：myow-frontend/apps/admin-web、myow-frontend/apps/client-web、myow-frontend/packages/ui、myow-frontend/packages/api、myow-frontend/packages/shared。

接口契约：前端开发前需按 myow-frontend-backend-contract-design.html 核对登录、权限、user/system 和客户端门户接口状态。

## 03技术栈建议

建议采用 Vue 3 生态。原因是后台管理系统中表格、表单、权限菜单、抽屉弹窗等模式成熟，团队招聘和组件生态也更稳定。

| 层级 | 建议 | 说明 |
| --- | --- | --- |
| 框架 | Vue 3 + TypeScript | 适合后台管理端与门户表单场景。 |
| 构建 | Vite | 开发速度快，配置简单。 |
| 包管理 | npm workspaces | 当前环境未安装 pnpm，先使用 npm workspaces；后续迁出独立前端项目时可平滑切换 pnpm。 |
| UI 组件库 | Element Plus 或 Ant Design Vue | 第一版建议二选一，禁止混用多个大型组件库。 |
| 路由 | Vue Router | 支持动态路由和路由守卫。 |
| 状态管理 | Pinia | 管理用户、权限、菜单、字典、系统配置。 |
| 请求 | Axios | 统一 token、错误码、登录过期、幂等提交处理。 |
| 接口生成 | openapi-typescript / openapi-generator | 从 Springdoc OpenAPI 生成类型和请求函数。 |

## 04工程目录结构

### Monorepo 结构

```
D:\workspace\myow-oms\myow-frontend/
├── apps/
│   ├── admin-web/
│   └── client-web/
├── packages/
│   ├── api/
│   ├── shared/
│   └── ui/
├── package.json
├── tsconfig.base.json
└── README.md
```

### 应用目录结构

```
src/
├── app/
│   ├── main.ts
│   ├── router.ts
│   └── bootstrap.ts
├── assets/
├── components/
├── layouts/
├── pages/
│   ├── login/
│   ├── dashboard/
│   ├── user/
│   └── system/
├── services/
├── stores/
├── permissions/
├── utils/
└── styles/
```

### 模块页面结构

```
pages/system/job/
├── JobList.vue
├── JobDetailDrawer.vue
├── JobEditDrawer.vue
├── JobLogDrawer.vue
├── components/
└── job.types.ts
```

约束：页面目录以业务模块组织，不以组件类型横向堆叠。跨页面复用组件放在模块内 components，跨模块复用才进入 packages/ui。

## 05权限与路由

### 管理端权限流程

```mermaid
flowchart LR
  Login["登录"] --> Token["保存 token"]
  Token --> Profile["拉取用户信息"]
  Profile --> Menu["拉取菜单树"]
  Menu --> Routes["生成动态路由"]
  Routes --> Buttons["保存按钮权限码"]
  Buttons --> App["进入后台"]
```

图 5-1：管理端权限初始化流程

### 客户端权限流程

```mermaid
flowchart LR
  Login["客户端登录"] --> Token["保存 token"]
  Token --> Account["拉取客户账号"]
  Account --> Scope["绑定 customer_id"]
  Scope --> PortalMenu["拉取门户菜单"]
  PortalMenu --> Portal["进入客户门户"]
```

图 5-2：客户端门户权限初始化流程

### 路由规则

| 规则 | 管理端 | 客户端门户 |
| --- | --- | --- |
| 登录路由 | /login | /client/login 或独立域名根路径 |
| 默认首页 | /dashboard 或第一个有权限菜单 | /portal/dashboard |
| 无权限 | 进入 403 | 进入 403，并提示联系客户主账号管理员 |
| 按钮权限 | 按权限码控制 | 按客户账号权限码控制 |
| 数据权限 | 后端按内部用户数据范围过滤 | 后端强制 customer_id + 子账号范围过滤 |

## 06接口生成与请求层

后端接口已经要求 Springdoc 注解。前端应从 OpenAPI 文档生成类型和基础请求函数，再由业务 service 封装页面需要的调用。

### 分层

| 层 | 职责 | 是否手写 |
| --- | --- | --- |
| generated-api | OpenAPI 生成的类型和请求函数。 | 否，自动生成。 |
| request client | Axios 实例、token、错误处理、响应解包。 | 是。 |
| module service | 封装业务语义，例如 jobService.create。 | 是。 |
| page | 页面调用 service，不直接调用 generated-api。 | 是。 |

### 错误处理

- 401：清理会话并跳转当前应用对应登录页。

- 403：进入无权限页或展示无权限提示。

- 业务错误：展示后端 message，表单错误定位字段。

- 网络错误：展示重试入口，不清空用户已填写表单。

- 重复提交：提交中禁用按钮，并可使用请求幂等 key。

## 07状态管理与缓存

| Store | 内容 | 持久化 |
| --- | --- | --- |
| authStore | token、登录状态、账号类型 | 需要，注意退出清理。 |
| userStore | 当前用户/客户账号信息 | 可持久化摘要，刷新后重新拉取。 |
| permissionStore | 菜单、按钮权限、数据权限摘要 | 可持久化，登录后刷新。 |
| dictStore | 字典、状态枚举、业务枚举 | 可缓存，支持版本刷新。 |
| appStore | 侧边栏折叠、主题、语言、页面缓存 | 可持久化。 |

缓存约束：敏感表单、API Secret、密码、账单明细不进入持久化缓存。退出登录必须清空所有用户相关缓存。

## 08组件与页面规范

### 共享组件候选

| 组件 | 职责 | 使用范围 |
| --- | --- | --- |
| PageHeader | 页面标题、说明、主操作。 | 管理端、客户端。 |
| SearchPanel | 搜索区、展开/收起、重置。 | 所有列表页。 |
| DataTable | 表格、分页、列设置、空态。 | 所有列表页。 |
| PermissionButton | 按权限码控制按钮展示和禁用。 | 所有操作按钮。 |
| StatusTag | 统一状态颜色和文案。 | 所有状态字段。 |
| ConfirmAction | 危险操作确认。 | 删除、停用、强退、发布、下线。 |

### 页面实现约束

- 列表页必须包含搜索区、工具栏、表格、分页和空态。

- 抽屉表单必须支持提交 loading、关闭确认、错误定位。

- 详情页必须区分只读信息和可操作按钮。

- 所有金额、时间、状态、枚举必须使用统一格式化工具。

- 所有权限按钮必须通过统一组件或指令实现，禁止页面内手写散落判断。

## 09构建部署

| 环境 | 配置 | 说明 |
| --- | --- | --- |
| dev | 本地 API 代理、mock 开关、调试日志 | 用于本地开发。 |
| test | 测试 API、环境标识、source map 可开启 | 用于测试验证。 |
| prod | 生产 API、关闭调试日志、资源压缩 | 不展示明显测试标识。 |

### 部署建议

- 管理端和客户端门户分别构建、分别部署，可使用不同域名或路径前缀。

- 静态资源走 CDN 或 Nginx，API 请求走后端网关。

- 前端版本号在页面 footer 或系统信息中可见，便于排查问题。

- 构建产物不包含环境密钥，所有密钥只能来自后端或部署环境。

## 10开发优先级

- P0-1：创建 monorepo、admin-web、client-web、shared/api/ui 基础包。

- P0-2：实现管理端登录、客户端登录、请求层、路由守卫、权限初始化。

- P0-3：实现管理端主框架、菜单渲染、403/404、基础页面组件。

- P0-4：实现 user/system 管理端 P0 页面。

- P1：实现客户端门户工作台、订单、库存、账号权限 P0 页面。

- P2：接入账单、API、工单、页面生成和更复杂的导入导出能力。

### 进入开发前检查

- 后端 Springdoc 文档可访问，并包含 user/system 已实现接口。

- 管理端和客户端登录接口边界明确。

- 菜单权限、按钮权限、客户账号权限的返回结构明确。

- 前端 UI 组件库最终确定。

- 是否创建独立前端目录或放在当前仓库根目录已确定。

MYOW Platform Frontend Architecture Design · Generated by Codex
