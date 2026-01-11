# API 文档（K-beat）

Base URL（本地默认）：

- `http://localhost:8080`

## 数据模型

### TodoPriority

| 值 | 含义 |
|---|---|
| `HIGH` | 高 |
| `MEDIUM` | 中 |
| `LOW` | 低 |

### Todo（响应）

字段含义：

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | number | 主键 |
| `description` | string | 内容（最多 200 字符） |
| `completed` | boolean | 是否完成 |
| `priority` | string | `HIGH/MEDIUM/LOW` |
| `dueDate` | string | `YYYY-MM-DD` |
| `priorityLabel` | string | 中文标签 |
| `priorityCssClass` | string | 前端徽章样式 class |

## 接口

### 1) 获取列表

`GET /api/todos`

返回：`200 OK` + `Todo[]`

### 2) 创建

`POST /api/todos`

Body（JSON）：

```json
{
  "description": "Write tests",
  "priority": "HIGH",
  "dueDate": "2025-12-17"
}
```

返回：`201 Created` + `Todo`

### 3) 标记完成

`POST /api/todos/{id}/complete`

返回：`200 OK` + `Todo`

### 4) 撤销完成

`POST /api/todos/{id}/reopen`

返回：`200 OK` + `Todo`

### 5) 删除

`DELETE /api/todos/{id}`

返回：`204 No Content`

## 错误格式（统一返回）

当发生可预期错误时（参数不合法、找不到 id 等），返回：

```json
{
  "message": "未找到待办事项（id=123）",
  "actionable_suggestion": "请刷新列表或确认该待办是否已被删除",
  "request_id": "3d5f5b65-4c1e-4b9d-9d1f-0f0ccbc98c37"
}
```

说明：

- `request_id` 与响应头 `X-Request-Id` 一致，可用于日志关联与问题定位。
- 这些字段用于前端将错误翻译为“人话提示”，避免把异常堆栈暴露给用户。

