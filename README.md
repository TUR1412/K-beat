# K-beat

一个“带节拍感”的待办应用：在保持 Spring Boot + FreeMarker 轻量服务器渲染的同时，提供现代化玻璃拟态 UI、暗色模式、筛选统计，以及（可选的）无刷新交互。

## 功能亮点

- **数据持久化**：使用 H2 文件库，**重启不丢**（默认写入 `./data/`）。
- **现代 UI**：Aurora 背景 + Glassmorphism 卡片 + Bento 布局，支持 **Dark / Light** 一键切换。
- **体验优化**：筛选（全部 / 进行中 / 已完成）、统计面板、轻量 Toast 提示、空状态引导。
- **到期提示**：待办若 **逾期 / 今天到期** 会自动高亮（完成后自动消失）。
- **可撤销完成**：已完成条目支持一键“撤销”。
- **渐进增强**：默认表单提交可用；启用 JS 时自动升级为无刷新操作（不影响可访问性）。
- **API 提供**：`/api/todos` 提供 JSON API（页面不会直接裸露 JSON）。

## 本地运行（推荐）

> 本项目默认端口为 **8080**（Spring Boot 默认）。

1) 运行测试（有限任务，不启动服务）：

```bash
./mvnw test
```

2) 启动应用：

```bash
./mvnw spring-boot:run
```

启动后访问：

- 页面：`http://localhost:8080/todos`
- API：`http://localhost:8080/api/todos`

## 构建 Jar 并运行

```bash
./mvnw -DskipTests package
java -jar target/k-beat-0.0.1-SNAPSHOT.jar
```

## 数据存储说明（H2）

- 默认数据库文件位置：`./data/kbeat`
- 想“清库重来”：删除 `data/` 目录即可（已加入 `.gitignore`）

## API 简表

- `GET /api/todos`：获取列表
- `POST /api/todos`：创建
  - body：`{ "description": "...", "priority": "HIGH|MEDIUM|LOW", "dueDate": "YYYY-MM-DD" }`
- `POST /api/todos/{id}/complete`：标记完成
- `POST /api/todos/{id}/reopen`：撤销完成
- `DELETE /api/todos/{id}`：删除

## Docker（可选）

构建镜像：

```bash
docker build -t k-beat:latest .
```

运行（建议挂载 `data/` 做持久化）：

```bash
docker run --rm -p 8080:8080 -v "$(pwd)/data:/app/data" k-beat:latest
```
