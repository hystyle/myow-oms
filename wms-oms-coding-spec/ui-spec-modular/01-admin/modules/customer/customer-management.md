# 管理端页面：客户管理

## 2. 客户管理

### 路由

| 项 | 值 |
| --- | --- |
| 路由 | `/admin/customer/customer` |
| 权限前缀 | `customer:customer` |
| 优先级 | P1 |

### 查询条件

| 字段 | Label | 类型 | 默认展示 |
| --- | --- | --- | --- |
| customerCode | 客户编码 | input | 是 |
| customerName | 客户名称 | input | 是 |
| status | 状态 | select | 是 |
| salesName | 销售 | input | 是 |
| serviceName | 客服 | input | 是 |
| createdTime | 创建时间 | dateRange | 是 |

### 表格列

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| 客户编码 | customerCode | 支持复制，点击详情 |
| 客户名称 | customerName | - |
| 联系人 | contactSummary | 字段权限脱敏 |
| 销售 | salesName | - |
| 客服 | serviceName | - |
| 账期/额度 | creditSummary | 需财务字段权限 |
| 状态 | status | 启用 / 禁用 / 黑名单 |
| 创建时间 | createdTime | - |
| 操作 | actions | 详情、编辑、启用/禁用 |

### 验收标准

- 客户联系人、电话、邮箱按字段权限脱敏。
- 黑名单客户必须限制创建订单和 API 凭证。
- 客户详情需要展示联系人、地址、合同、账单、账号权限、操作日志。
