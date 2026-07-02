# MYOW 前后端契约设计

## 目录

## 01契约状态定义

本文件用于开发前确认前端页面依赖的接口是否可用。状态不等于最终质量，只表示当前是否足以支撑前端进入开发。

| 状态 | 含义 | 前端处理 |
| --- | --- | --- |
| 已具备 | 后端已有 Controller/API，前端可基于 OpenAPI 接入。 | 直接开发页面，必要时补字段对齐。 |
| 待补 | 后端模块存在，但接口或字段不满足 UI/UE 需求。 | 前端可先 mock，但进入联调前必须补齐。 |
| 可 mock | 用于原型或低风险展示，不阻塞框架开发。 | 使用 mock 数据，标记 TODO。 |
| 后续模块 | 依赖 customer / overseas / finance 等业务模块。 | 先设计路由和占位页，不做真实联调。 |

## 02通用响应契约

### 统一响应

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| code | String / Integer | 业务状态码。成功、参数错误、未登录、无权限等必须稳定。 |
| message | String | 可展示给用户的错误或成功提示。 |
| data | Object / Array / Null | 业务数据。 |
| traceId | String | 建议补充，用于 500 或联调问题排查。 |

### 分页响应

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| records | Array | 当前页数据。 |
| total | Long | 总记录数。 |
| pageNo | Integer | 当前页。 |
| pageSize | Integer | 每页条数。 |

### 前端必须识别的错误

- 401：未登录或登录过期，跳转对应登录页。

- 403：无权限，进入 403 或隐藏按钮。

- 400/参数错误：表单字段定位。

- 409/重复提交或业务冲突：展示冲突原因。

- 500：展示通用错误和 traceId。

## 03登录与会话

| 能力 | 建议接口 | 状态 | 备注 |
| --- | --- | --- | --- |
| 管理端登录 | POST /auth/login | 已具备 | 当前存在 UserLoginController。 |
| 退出登录 | POST /auth/logout | 已具备 | 当前存在 UserLoginController。 |
| 当前用户信息 | POST /profile/current | 已具备 | 当前存在 ProfileController。 |
| 当前用户菜单树 | POST /profile/menus 或 /menu/tree | 已具备 | 前端侧边栏以后端菜单为准；无菜单数据时仅使用本地开发兜底菜单。 |
| 按钮权限码 | POST /profile/permissions | 已具备 | 返回当前用户全部按钮/操作权限码集合。 |
| 修改密码 | POST /profile/password/change | 已具备 | 当前接口为 POST /profile/change-password，兼容 POST /auth/change-password。 |
| 强制首次改密 | POST /profile/change-password | 已具备 | 登录响应和 bootstrap 响应均返回 forceChangePassword，改密后会清理会话。 |
| 客户端登录 | POST /client/auth/login | 后续模块 | 依赖 customer 账号体系。 |

## 04管理端 user 契约

当前 myow-user 已有较多 Controller，可支撑管理端 P0 页面开发。仍需在前端开发前确认接口命名、字段和权限初始化接口。

| 页面/能力 | 后端 Controller | 所需能力 | 状态 |
| --- | --- | --- | --- |
| 用户管理 | UserController | 分页、详情、新增、编辑、启停、重置密码、分配角色。 | 已具备 |
| 个人资料 | ProfileController | 当前用户、修改密码、个人信息维护。 | 已具备 |
| 角色管理 | RoleController、RoleMenuController、RoleDeptController | 角色 CRUD、菜单授权、数据权限授权、关联用户。 | 已具备 |
| 菜单权限 | MenuController | 菜单树、目录/菜单/按钮维护、排序、启停。 | 已具备 |
| 部门管理 | DeptController | 部门树、详情、新增、编辑、启停、排序。 | 已具备 |
| 岗位管理 | PositionController、UserPostController | 岗位维护、用户岗位关联。 | 已具备 |
| 租户管理 | TenantController、TenantPlansController | 租户 CRUD、启停、套餐。 | 已具备 |
| 字典枚举 | DictController、DictDataController | 字典类型、字典值、状态枚举。 | 已具备 |
| 登录日志 | LoginLogController | 分页、详情、筛选。 | 已具备 |
| 操作日志 | OperLogController | 分页、详情、筛选。 | 已具备 |
| 权限初始化聚合接口 | 建议新增 ProfileController 方法 | 一次返回用户、菜单、按钮权限、数据权限、系统配置摘要。 | 已具备 |

## 05管理端 system 契约

当前 myow-system 已实现一组系统中心接口，可支撑 P0 页面开发，部分增强能力可后续补。

| 页面/能力 | 后端 Controller | 所需能力 | 状态 |
| --- | --- | --- | --- |
| 定时任务 | JobController | 分页、详情、新增、编辑、删除、启停、立即执行。 | 已具备 |
| 任务日志 | JobController / 日志查询能力 | 按任务查询执行日志、查看失败原因。 | 待确认 |
| 通知公告 | NoticeController | 分页、详情、新增、编辑、发布、下线、删除。 | 已具备 |
| 站点配置 | SiteConfigController | 分页、详情、新增、编辑、删除、启停。 | 已具备 |
| 文件管理 | FileController | 上传、分页、详情、删除、文件流下载。 | 已具备 |
| 导出任务 | ExportTaskController | 创建、分页、详情、成功后下载导出文件流。 | 已具备 |
| 敏感词 | SensitiveWordController | 分页、详情、新增、编辑、启停、删除、变量预览。 | 已具备 |
| 消息模板 | MessageTemplateController | 分页、详情、新增、编辑、启停、删除。 | 已具备 |
| 系统监控 | MonitorController | 系统摘要、健康状态、运行时信息。 | 可 mock/已具备基础 |
| 在线用户 | OnlineUserController | 分页、强退。 | 可 mock/已具备占位 |

## 06客户端门户契约

客户端门户依赖 customer、overseas、finance 等业务模块。当前可先按 UI/UE 设计做 mock 或占位，待模块开发后联调。

| 页面/能力 | 建议接口 | 依赖模块 | 状态 |
| --- | --- | --- | --- |
| 客户端登录 | POST /client/auth/login | myow-customer + user 认证底座 | 后续模块 |
| 客户账号信息 | POST /client/profile/current | myow-customer | 后续模块 |
| 客户门户菜单 | POST /client/profile/menus | myow-customer | 后续模块 |
| 客户工作台摘要 | POST /client/dashboard/summary | customer / overseas / finance | 可 mock |
| 出库订单 | POST /client/overseas/outbound/page | myow-overseas | 后续模块 |
| 入库预报 | POST /client/overseas/inbound/page | myow-overseas | 后续模块 |
| SKU 列表 | POST /client/overseas/sku/page | myow-overseas | 后续模块 |
| 库存看板 | POST /client/overseas/inventory/page | myow-overseas | 后续模块 |
| 账单余额 | POST /client/finance/wallet/detail | myow-finance | 后续模块 |
| API 凭证 | POST /client/developer/credential/page | myow-customer | 后续模块 |
| 子账号管理 | POST /client/account/page | myow-customer | 后续模块 |

强制约束：所有客户端接口后端必须从登录态解析 customer_id，禁止前端传入 customer_id 决定数据范围。必要的查询 customer_id 只能作为后端审计字段，不作为信任来源。

## 07字典枚举与状态

| 枚举 | 用途 | 来源 | 状态 |
| --- | --- | --- | --- |
| 用户状态 | 正常、停用、锁定、待改密。 | user 字典或常量 | 已具备 |
| 菜单类型 | 目录、菜单、按钮、接口。 | user 字典或常量 | 已具备 |
| 任务状态 | 启用、停用、执行成功、执行失败。 | system 字段枚举 | 已具备 |
| 公告状态 | 草稿、已发布、已下线、已过期。 | system 字段枚举 | 已具备 |
| 客户账号角色 | 主账号、操作员、财务、技术、主管。 | customer 模块 | 后续模块 |
| 订单状态 | 待处理、待出库、已出库、异常、取消。 | overseas 模块 | 后续模块 |
| 库存状态 | 在途、可用、待出库、冻结、坏品。 | overseas 模块 | 后续模块 |

## 08管理端按钮权限码

管理端按钮由 POST /profile/bootstrap 返回的 permissionList 控制。前端只负责隐藏无权限按钮，后端接口仍必须做最终权限校验。未配置权限码的只读操作默认可见；新增、编辑、删除和状态类操作必须配置权限码。

| 页面 | 操作 | 权限码 | 说明 |
| --- | --- | --- | --- |
| 用户账号 | 新增 | system:user:add | 创建内部用户。 |
| 用户账号 | 编辑 / 启停 / 重置密码 / 解锁 / 强制改密 | system:user:update | 账号维护类操作统一使用更新权限。 |
| 用户账号 | 删除 | system:user:delete | 删除内部用户。 |
| 部门组织 | 新增 | system:dept:add | 创建部门节点。 |
| 部门组织 | 编辑 | system:dept:update | 维护部门名称、上级、排序和负责人。 |
| 部门组织 | 删除 | system:dept:delete | 删除部门节点。 |
| 角色权限 | 新增 / 编辑 / 删除 | system:role:add / system:role:update / system:role:delete | 对应 RoleController 权限。 |
| 菜单权限 | 新增 / 编辑 / 删除 | system:menu:add / system:menu:update / system:menu:delete | 对应 MenuController 权限。 |
| 字典管理 | 新增 / 编辑 / 删除 | system:dict:add / system:dict:update / system:dict:delete | 对应 DictController 权限。 |
| 定时任务 | 新增 / 编辑 / 删除 / 执行 / 暂停 / 恢复 | system:job:create / system:job:update / system:job:delete / system:job:run / system:job:pause / system:job:resume | 权限码与 Flyway system seed 保持一致。 |
| 通知公告 | 新增 / 编辑 / 删除 / 发布 / 下线 | system:notice:create / system:notice:update / system:notice:delete / system:notice:publish / system:notice:withdraw | 发布和下线分离。 |
| 站点配置 | 新增 / 编辑 / 删除 / 刷新缓存 | system:site-config:create / system:site-config:update / system:site-config:delete / system:site-config:refresh | 刷新缓存按站点编码执行。 |
| 文件管理 | 删除 / 下载 | system:file:delete / system:file:download | 下载接口返回附件文件流，前端按 Content-Disposition 触发浏览器下载。 |
| 在线用户 | 踢出 | system:online-user:kick | 强制失效在线会话。 |
| 敏感词 | 新增 / 编辑 / 删除 | system:sensitive-word:create / system:sensitive-word:update / system:sensitive-word:delete | 敏感词库维护。 |
| 消息模板 | 新增 / 编辑 / 删除 / 预览 | system:message-template:create / system:message-template:update / system:message-template:delete / system:message-template:preview | 预览请求携带变量 JSON，后端返回渲染后的标题、内容和缺失变量列表，不修改数据。 |
| 导出任务 | 创建 / 删除 / 下载 | system:export-task:create / system:export-task:delete / system:export-task:download | 下载前后端需校验任务成功状态、文件存在性和任务归属；当前接口返回附件文件流。 |

## 08缺口与优先级

### P0 必须补齐

- 在前端开发前通过 OpenAPI 再核对管理端登录、退出、当前用户、菜单树、按钮权限码和 bootstrap 的字段命名。

- 根据前端真实需要继续细化 bootstrap 中的 dataScope、tenantModeEnabled、systemConfig 字段。

- 确认 user/system 所有 Controller 的 Springdoc 注解完整，保证 OpenAPI 生成质量。

- 确认分页响应结构、错误码结构和 401/403 行为。

### P1 可后续补

- 系统监控深度指标。

- 在线用户真实会话列表。

- 任务日志统计条。

- 公告阅读统计。

- 导出任务下载权限和过期清理策略。

### 可 mock 开发

- 客户端门户工作台摘要。

- 客户端订单、库存、账单、API 页面。

- 系统监控卡片。

- 在线用户页面。

MYOW Platform Frontend Backend Contract Design · Generated by Codex
