# myow-oms
M.Y

### 模块划分：
- myow-infrastructure 层，负责数据库访问、MyBatis-Plus 配置、缓存、消息队列等基础设施。
- myow-common 层，负责公共代码、工具类、枚举、异常、自定义框架等。
- myow-system 层，负责系统设置、菜单管理、数据字典、单号管理、文件管理、定时任务、系统层（用户、角色、权限、Tenant、日志、通知、国际化）等。
- myow-business-core 层，负责业务逻辑的核心模块，如地址库、公海客户管理、客户管理、供应商管理、商品管理、仓库管理、物流基础、第三方系统配置、计费科目 / 费用项基础等。
- myow-business-oms 层，负责订单管理系统（Oms）的业务逻辑模块，包括订单创建、查询、更新、取消、状态转换等。
- myow-fms 层，负责财务相关的业务逻辑模块，包括订单支付、退款、对账、发票管理等。
- myow-openapi 层，负责开放接口的业务逻辑模块，包括用户注册、登录、权限校验、接口文档等。
- myow-app 层，启动入口 + 模块组装 + @SpringBootApplication。

## 四层架构 + 核心领域（Domain）高度纯净：
````
com.myow.{模块名}    （模块根包，例如 com.company.myow.business.core）
├── application               （应用层 - 协调、用例、事务边界）
│   ├── dto                   （入参/出参DTO，接收/返回给外层）
│   ├── vo                    （视图对象，可选，用于复杂展示）
│   ├── query                 （查询条件对象）
│   ├── converter             （DTO ↔ Domain 转换器，可选）
│   ├── service               （应用服务接口 + impl）
│   └── executor / usecase    （可选，如果想更细粒度，可放命令/查询执行器）
├── domain                    （领域层 - 核心业务规则，纯POJO，零框架依赖）
│   ├── entity                （实体，有唯一标识）
│   ├── valueobject           （值对象，无标识，不可变）
│   ├── aggregate             （聚合根，通常和主实体同包或单独放root）
│   ├── repository            （仓储接口 - 纯领域语言，无任何框架）
│   ├── service               （领域服务 - 跨聚合协作的业务逻辑）
│   └── policy / rule         （可选，业务规则/策略/规格）
├── infrastructure            （基础设施层 - 技术实现）
│   ├── repository            （仓储接口的实现，mybatis-plus mapper）
│   ├── persistence           （mybatis-plus Mapper 接口）
│   ├── converter             （DO ↔ Entity 转换，可选）
│   ├── config                （模块内特殊配置类）
│   └── client                （调用第三方、rpc、消息等适配器）
└── interfaces                （用户接口层 / 展示层 / 适配层）
    ├── controller            （RestController）
    ├── facade                （可选，内部模块对外门面）
    ├── assembler             （可选，VO/DTO 组装）
    └── advice / filter       （模块内异常处理、拦截器等，可选）