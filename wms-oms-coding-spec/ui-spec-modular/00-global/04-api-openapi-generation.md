# 08. API、OpenAPI 与 UI 生成规范

## 1. 目标

MYOW 页面可以由 coding agent 辅助生成，但前提是接口说明、DTO 字段、枚举、权限码、错误码足够明确。本规范用于约束后端接口和前端页面之间的契约。

## 2. Controller 注解要求

每个接口必须包含：

```java
@Operation(
  summary = "分页查询出库订单",
  description = "用于管理端查询出库订单列表。受数据权限控制，支持按客户、仓库、状态、时间范围筛选。"
)
```

规则：

- `summary` 写清动作，不写泛泛的“查询”。
- `description` 写清使用端、业务影响、权限和状态限制。
- 状态变更接口必须写清变更前置条件和影响范围。
- 危险操作接口必须写清是否可恢复。

## 3. DTO 字段注解要求

```java
@Schema(description = "出库单号", example = "OB202607030001", requiredMode = Schema.RequiredMode.REQUIRED)
private String outboundNo;
```

字段必须声明：

| 信息 | 用途 |
| --- | --- |
| description | UI label / tooltip |
| example | placeholder / mock |
| required | 是否必填 |
| enum 说明 | 下拉、标签、状态流 |
| format | 日期、金额、重量、体积 |
| unit | kg、cm、USD、CNY 等 |

## 4. 统一分页结构

分页请求字段统一：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| pageNo | integer | 当前页，从 1 开始 |
| pageSize | integer | 每页条数 |

分页响应统一：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| list | array | 当前页数据 |
| total | long | 总条数 |

## 5. UI 类型映射

| DTO 信息 | 默认 UI |
| --- | --- |
| `String` | 输入框 |
| `String` + `date-time` | 日期时间展示 / 日期时间选择器 |
| `Boolean` | 开关 / 是否标签 |
| `Enum` | 下拉 / 单选 / 状态标签 |
| `BigDecimal` + currency | 金额组件 |
| `BigDecimal` + unit | 数值 + 单位 |
| `List<String>` | 多选 / 标签组 |
| `Long id` | 字符串处理，不使用数字输入框 |

## 6. 枚举规范

后端枚举必须输出：

| 字段 | 说明 |
| --- | --- |
| value | 枚举值 |
| label | 中文展示 |
| colorType | success / warning / danger / info / gray |
| description | 业务说明 |
| allowedActions | 当前状态允许的动作，可选 |

示例：

```json
{
  "value": "WMS_FAILED",
  "label": "WMS下发失败",
  "colorType": "danger",
  "description": "订单下发仓库失败，需要运营处理",
  "allowedActions": ["detail", "resend", "cancel"]
}
```

## 7. 错误码规范

错误响应必须包含：

| 字段 | 说明 |
| --- | --- |
| code | 错误码 |
| message | 可读错误消息 |
| traceId | 追踪 ID |
| fieldErrors | 字段级校验错误，可选 |

管理端可以展示较完整的业务原因；客户端必须按 `03-permission-data-visibility-spec.md` 做友好映射。

## 8. 页面 API 映射表要求

每个页面规格必须包含：

```md
| 动作 | 方法 | API | 请求 DTO | 响应 DTO | 权限码 |
| --- | --- | --- | --- | --- | --- |
| 分页查询 | POST | /admin/oms/outbound/page | OutboundPageReqVO | PageResult<OutboundRespVO> | oms:outbound:query |
```

## 9. 生成前置条件

coding agent 生成页面前必须确认：

- 页面 spec 已存在。
- API 路径、请求 DTO、响应 DTO 已明确。
- 枚举值和中文文案已明确。
- 权限码已明确。
- 需要隐藏、置灰、确认的操作已明确。
- 客户端文案已完成内部状态到客户展示的转换。
