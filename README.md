# myow-oms
M.Y

### 模块划分：
myow-infrastructure 层，负责数据库访问、MyBatis-Plus 配置、缓存、消息队列等基础设施。
myow-common 层，负责公共代码、工具类、枚举、异常、自定义框架等。
myow-system 层，负责系统设置、菜单管理、数据字典、单号管理、文件管理、定时任务、系统层（用户、角色、权限、Tenant、日志、通知、国际化）等。
myow-business-core 层，负责业务逻辑的核心模块，如地址库、公海客户管理、客户管理、供应商管理、商品管理、仓库管理、物流基础、第三方系统配置、计费科目 / 费用项基础等。
myow-business-oms 层，负责订单管理系统（Oms）的业务逻辑模块，包括订单创建、查询、更新、取消、状态转换等。
myow-fms 层，负责财务相关的业务逻辑模块，包括订单支付、退款、对账、发票管理等。
myow-openapi 层，负责开放接口的业务逻辑模块，包括用户注册、登录、权限校验、接口文档等。
myow-app 层，启动入口 + 模块组装 + @SpringBootApplication。
