# 客户端页面：工单中心与公告

## 5. 工单中心

### 查询条件

| 字段 | Label | 类型 | 默认展示 |
| --- | --- | --- | --- |
| ticketNo | 工单号 | input | 是 |
| relatedNo | 关联单号 | input | 是 |
| ticketType | 类型 | select | 是 |
| status | 状态 | select | 是 |
| createdTime | 创建时间 | dateRange | 是 |

### 表格列

| 列名 | 字段 | 展示规则 |
| --- | --- | --- |
| 工单号 | ticketNo | 支持复制，点击详情 |
| 类型 | ticketType | 订单 / 库存 / 账单 / API / 其他 |
| 关联单据 | relatedNo | 支持复制 |
| 状态 | status | 待处理 / 处理中 / 待客户回复 / 已解决 / 已关闭 |
| 优先级 | priority | 普通 / 紧急 |
| 最近回复 | lastReplySummary | 超长省略 |
| 创建时间 | createdTime | - |
| 操作 | actions | 查看、回复、关闭 |

## 6. API 映射

| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| 凭证分页 | POST | `/client/developer/api-credential/page` | `ClientApiCredentialPageReqVO` | `PageResult<ClientApiCredentialRespVO>` | `client:api-credential:query` |
| 创建凭证 | POST | `/client/developer/api-credential` | `ClientApiCredentialCreateReqVO` | `ClientApiCredentialCreateRespVO` | `client:api-credential:add` |
| 重置 Secret | POST | `/client/developer/api-credential/{id}/reset-secret` | - | `ClientApiSecretRespVO` | `client:api-credential:reset-secret` |
| 调用日志 | POST | `/client/developer/api-log/page` | `ClientApiLogPageReqVO` | `PageResult<ClientApiLogRespVO>` | `client:api-log:query` |
| 工单分页 | POST | `/client/support/ticket/page` | `ClientTicketPageReqVO` | `PageResult<ClientTicketRespVO>` | `client:ticket:query` |
| 创建工单 | POST | `/client/support/ticket` | `ClientTicketCreateReqVO` | `Long` | `client:ticket:add` |
| 回复工单 | POST | `/client/support/ticket/{id}/reply` | `ClientTicketReplyReqVO` | `Boolean` | `client:ticket:reply` |

## 7. 验收标准

- Secret 不可在列表完整展示。
- 客户 API 错误信息必须可理解，不能只显示内部异常堆栈。
- 从异常页创建工单时必须自动带入关联单号和上下文。
- 客服回复后工作台产生待办。
