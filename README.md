# MyOW 项目架构规范（V1.0）

# 一、设计目标

MyOW（My Overseas Warehouse）采用 **模块化单体（Modular Monolith）** 架构。

目标：

* 业务边界清晰
* 模块低耦合
* 公共能力统一管理
* 后续可平滑演进到微服务
* 避免过度拆分

当前阶段 **不设计微服务**，也 **不提前拆分 API 服务**。

整个项目按照 **业务域（Bounded Context）** 拆分，而不是按照菜单、页面或数据库表拆分。

---

# 二、整体项目结构

```text
myow-parent (pom)

│
├── myow-overseas-app          // 海外仓系统（SpringBoot）
├── myow-firstmile-app         // 头程系统（SpringBoot）
│
├── myow-user                  // 用户中心
├── myow-overseas              // 海外仓业务中心
├── myow-finance               // 财务中心
├── myow-distribution          // 分销中心（后续）
│
├── myow-common-parent         // Common父工程（pom）
│
├── myow-common-core
├── myow-common-web
├── myow-common-security
├── myow-common-mybatis
├── myow-common-redis
├── myow-common-excel
└── ...
```

命名规范：

* 所有模块统一使用 **myow-** 开头
* SpringBoot 启动项目统一使用 **-app** 结尾
* 业务模块不使用 module 后缀
* Common 模块统一使用 **myow-common-*** 命名

---

# 三、启动项目职责

启动项目：

```text
myow-overseas-app

myow-firstmile-app
```

职责：

* SpringBoot 启动
* 系统配置
* Bean 装配
* 当前系统专属接口
* 聚合多个业务模块能力

例如：

```text
海外仓首页

海外仓工作台

海外仓统计

海外仓聚合查询
```

如果某个接口需要同时调用：

* 用户
* 财务
* 分销

那么该接口建议放启动项目。

启动项目原则：

不编写核心业务逻辑。

---

# 四、业务模块职责

业务模块按业务域拆分。

## myow-user

负责：

* 登录
* 用户
* 角色
* 权限
* 菜单
* Token 登录
* 用户相关接口

例如：

```text
/auth/login

/auth/logout

/auth/profile

/user/list
```

---

## myow-overseas

负责：

* 仓库
* 库区/库位
* 库存
* 入库
* 出库
* 物流渠道/承运渠道
* 海外仓业务单据
* 海外仓相关配置

提供自己的 Controller。

例如：

```text
/overseas/warehouses

/overseas/channels

/overseas/inbounds

/overseas/outbounds
```

---

## myow-finance

负责：

* 财务
* 对账
* 账单
* 扣费
* 应收
* 应付

提供自己的 Controller。

例如：

```text
/finance/bill

/finance/reconcile
```

---

## myow-distribution

负责：

* 分销商品
* 分销库存
* 分销订单
* 分销关系

提供自己的 Controller。

---

## 为什么没有仓库模块、物流模块、合同模块？

当前：

海外仓业务已归入：

```text
myow-overseas
```

当前海外仓领域包含：

* 仓库
* 库存
* 入库
* 出库
* 合同
* 物流
* 索赔

属于同一个业务域。

因此：

不继续拆多个 Module。

以后如果业务规模扩大，再继续拆分。

原则：

> 一个业务域 = 一个业务模块

而不是：

一个菜单 = 一个模块。

---

# 五、Controller 放置原则

业务模块：

可以放自己的 Controller。

例如：

```text
myow-user

myow-overseas

myow-finance

myow-distribution
```

都可以提供自己的接口。

启动项目：

只放：

* 系统专属接口
* 聚合接口
* 工作台接口
* 首页接口

例如：

```text
OverseasDashboardController

OverseasWorkbenchController
```

这样：

登录接口无需重复开发。

两个启动项目引用：

```text
myow-user

myow-overseas
```

即可自动拥有：

```text
/auth/login

/auth/logout
```

---

# 六、Common 模块职责

Common 只放基础设施。

禁止放业务。

---

## myow-common-parent

packaging = pom

职责：

* dependencyManagement
* pluginManagement
* Maven 版本统一管理

不生成 Jar。

---

## myow-common-core

负责：

* Result
* Exception
* ErrorCode
* PageResult
* Enum
* 常量
* 工具类

---

## myow-common-web

负责：

* 全局异常
* Jackson
* MVC
* Swagger
* Filter
* Interceptor
* Web 日志

---

## myow-common-security

负责：

* Sa-Token
* 登录上下文
* SecurityUtils
* UserContext
* Token 工具
* 权限公共能力

账号密码校验：

仍然属于：

```text
myow-user
```

---

## myow-common-mybatis

负责：

* MyBatisPlus
* BaseDO
* 自动填充
* 分页插件
* TypeHandler

禁止放 Mapper。

---

## myow-common-redis

负责：

* RedisConfig
* RedisCache
* RedisLock
* CacheKey
* Redis 工具

业务尽量不要直接使用 RedisTemplate。

---

## myow-common-excel

负责：

* Excel 导入
* Excel 导出
* Excel 工具

---

# 七、依赖规则

允许：

```text
myow-overseas-app
    ↓
myow-user

myow-overseas-app
    ↓
myow-finance

myow-overseas-app
    ↓
myow-distribution

业务模块
    ↓
Common
```

禁止：

```text
Common
    ↓
业务模块
```

禁止：

```text
业务模块
    ↓
启动项目
```

Common 永远不能依赖业务。

---

# 八、业务模块之间调用

业务模块之间：

禁止依赖实现类。

例如：

错误：

```java
@Autowired
DistributionServiceImpl
```

正确：

```java
@Autowired
DistributionService
```

当前阶段：

不提前拆 API Module。

以后真正拆微服务时，再抽：

```text
distribution-api
```

---

# 九、登录设计

登录接口：

```text
myow-user
```

登录流程：

```text
LoginController

↓

LoginService

↓

生成 Token

↓

Redis 保存 LoginUser

↓

返回 Token
```

后续请求：

```text
Controller

↓

Sa-Token

↓

UserContext

↓

业务 Service

↓

MyBatis 自动填充
```

业务统一通过：

```java
UserContext.getUserId()
```

获取当前用户。

不得直接依赖 Sa-Token。

---

# 十、模块拆分原则

拆分依据：

**业务域（Bounded Context）**

不是：

* 数据库表
* 页面
* 菜单
* Controller 数量

只有满足下面条件之一时，才继续拆 Module：

* 独立业务闭环
* 独立团队维护
* 独立生命周期
* 多系统复用
* 后续可能独立部署

否则保持当前模块即可。

---

# 十一、总体设计原则

整个项目遵循以下原则：

* 高内聚
* 低耦合
* Common 负责基础设施
* Business 负责业务能力
* App 负责系统启动和聚合能力
* Controller 可以存在于业务模块
* 系统专属 Controller 放 App
* 当前保持模块化单体
* 后续可平滑演进微服务
* 不进行过度设计
* 不提前拆分不存在的模块

最高原则：
模块拆分以“业务域（Bounded Context）”为边界，而不是以数据库表、菜单、页面或功能点为边界。

---

# 十二、数据库迁移与 Flyway 约定

项目使用 Flyway 管理数据库结构补丁，避免手工执行零散 SQL。

## 1. 迁移脚本位置

业务模块负责提供迁移脚本：

```text
myow-user/src/main/resources/db/migration
```

启动模块负责引入业务模块并执行迁移：

```text
myow-overseas-app
myow-firstmile-app
```

## 2. 命名规范

Flyway 版本脚本统一使用：

```text
V{版本号}__{说明}.sql
```

示例：

```text
V1__myow_user_base_schema.sql
V2__myow_user_phase1_schema.sql
V3__myow_user_default_seed.sql
V4__myow_user_management_permissions.sql
V5__myow_user_remaining_management_menus.sql
```

已发布的版本脚本不得修改、删除或复用版本号；后续变更必须新增更高版本脚本。

## 3. 基线策略

当前项目支持既有库接入 Flyway：

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    baseline-version: 1
```

空库从 `V1__myow_user_base_schema.sql` 基础建表开始迁移；既有非空库可通过 `baseline-on-migrate` 将历史基础结构视为版本 1，后续阶段补丁从 `V2` 开始执行。

## 4. SQL 编写要求

迁移脚本应尽量保持幂等和可重复部署友好：

* 新增表使用 `CREATE TABLE IF NOT EXISTS`
* 新增字段使用 `ADD COLUMN IF NOT EXISTS`
* 初始化数据使用 `ON CONFLICT`
* 字段重命名使用条件判断
* 不在业务运行代码中临时创建或修改表结构

## 5. 当前初始化数据

`V3__myow_user_default_seed.sql` 初始化用户中心本地可启动所需的默认数据：

* 默认租户套餐、默认租户
* 默认部门、岗位、角色
* 默认管理员账号
* 系统管理菜单、用户管理菜单与基础按钮权限
* 角色菜单授权、用户角色授权

`V4__myow_user_management_permissions.sql` 补齐第一阶段系统管理权限：

* 角色、菜单、部门、岗位、字典、配置的新增/修改/删除按钮权限
* 字典管理菜单
* 默认超级管理员角色的菜单权限授权

`V5__myow_user_remaining_management_menus.sql` 补齐剩余用户中心管理菜单：

* 租户、租户套餐、登录日志、操作日志
* I18n Key、I18n Message
* 流水号配置、流水号记录
* 默认超级管理员角色的菜单权限授权

本地默认账号为：

```text
admin / MyowAdmin2026!
```

该账号仅用于本地开发和初始部署验证；生产环境部署后必须立即修改默认密码或替换初始化脚本中的默认账号策略。
