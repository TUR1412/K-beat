<div align="center">
  <img src="docs/hero.svg" alt="K-beat" width="100%" />
  <h1>K-beat</h1>
  <p>把每件事都打在节拍上：服务端渲染为主 + 渐进增强 + 持久化的待办应用</p>
  <p>
    <a href="./README.md">English</a> ·
    <a href="./README.zh-CN.md">简体中文</a>
  </p>
  <p>
    <a href="https://github.com/TUR1412/K-beat/actions/workflows/main.yml">
      <img alt="CI" src="https://github.com/TUR1412/K-beat/actions/workflows/main.yml/badge.svg?branch=main" />
    </a>
    <img alt="Java" src="https://img.shields.io/badge/Java-17-informational" />
    <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.3.x-success" />
  </p>
</div>

## 一句话定位

K-beat 是一个“看起来像现代前端、但运行起来像传统 Web”的待办应用：

- 默认纯表单即可用（SSR + Redirect），不依赖 JS。
- 启用 JS 后自动升级为无刷新交互（fetch + 乐观 UI）。
- 使用 H2 文件库持久化数据，重启不丢。

## 特性概览

- 持久化：H2 文件库（默认写入 `./data/`）
- 现代 UI：Aurora 背景 + Glassmorphism + Bento 布局，支持暗色/亮色切换
- 渐进增强：无 JS 也可完成所有关键操作；有 JS 则提升交互效率
- API：`/api/todos` 提供 JSON API（页面不直接渲染原始 JSON）
- 可靠兜底：
  - 友好错误页（包含 `requestId`，便于定位）
  - API 统一错误格式：`message` / `actionable_suggestion` / `request_id`
  - 所有响应返回 `X-Request-Id`（用于日志关联与支持排查）
- 观测与性能：
  - Actuator：`/actuator/health` / `/actuator/metrics` / `/actuator/prometheus`
  - `Server-Timing` 响应头 + 慢请求日志
  - Gzip 压缩 + 静态资源强缓存
- 安全最佳实践：默认启用常见安全响应头（CSP、nosniff 等）

## 快速开始

本项目默认端口为 `8080`（Spring Boot 默认）。

### Windows

```bash
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

### macOS / Linux

```bash
./mvnw test
./mvnw spring-boot:run
```

启动后访问：

- 页面：`http://localhost:8080/todos`
- API：`http://localhost:8080/api/todos`
- 健康检查：`http://localhost:8080/actuator/health`

## 构建 Jar 并运行

```bash
./mvnw -DskipTests package
java -jar target/k-beat-0.0.1-SNAPSHOT.jar
```

## 配置

常用配置（`src/main/resources/application.properties`）：

- `app.assets.version`：静态资源版本号（用于 Cache Busting）
- `spring.datasource.url`：H2 数据库存储位置（默认 `jdbc:h2:file:./data/kbeat`）
- `app.http.slowRequestThresholdMs`：慢请求阈值（毫秒）
- `management.endpoints.web.exposure.include`：Actuator 暴露范围（生产建议走内网或加鉴权）

## 文档导航

- 架构说明：`docs/ARCHITECTURE.md`
- API 文档：`docs/API.md`
- 部署与运行：`docs/DEPLOYMENT.md`

## Docker（可选）

构建镜像：

```bash
docker build -t k-beat:latest .
```

运行（建议挂载 `data/` 做持久化）：

macOS / Linux（bash/zsh）：

```bash
docker run --rm -p 8080:8080 -v "$(pwd)/data:/app/data" k-beat:latest
```

Windows PowerShell：

```powershell
docker run --rm -p 8080:8080 -v "${PWD}/data:/app/data" k-beat:latest
```

## 贡献指南

欢迎 PR：请先阅读 `CONTRIBUTING.md`，并确保 CI 通过。

