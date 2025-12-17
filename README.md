<div align="center">
  <img src="docs/hero.svg" alt="K-beat" width="100%" />
  <h1>K-beat</h1>
  <p>把每件事都打在节拍上：服务端渲染为主 + 渐进增强 + 持久化的待办应用</p>

  <p>
    <a href="https://github.com/TUR1412/K-beat/actions/workflows/main.yml">
      <img alt="CI" src="https://github.com/TUR1412/K-beat/actions/workflows/main.yml/badge.svg?branch=main" />
    </a>
    <img alt="Java" src="https://img.shields.io/badge/Java-17-informational" />
    <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.3.x-success" />
  </p>
</div>

## 一句话定位

K-beat 是一个“看起来像现代前端、但运行起来像传统 Web”的待办应用：默认纯表单即可用，开启 JS 后自动升级为无刷新交互；同时使用 H2 文件库持久化数据，重启不丢。

## 特性概览

- 持久化：H2 文件库（默认写入 `./data/`）
- 现代 UI：Aurora 背景 + Glassmorphism + Bento 布局，支持暗色/亮色切换
- 体验增强：筛选（全部/进行中/已完成）、统计、Toast 提示、空状态引导
- 到期提示：逾期/今日到期自动高亮（完成后消失）
- 可撤销：已完成条目支持一键撤销
- API：`/api/todos` 提供 JSON API（页面不直接渲染原始 JSON）
- 可靠兜底：友好错误页，API 错误统一格式并提供 `actionable_suggestion`

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

## 文档导航

- 架构说明：`docs/ARCHITECTURE.md`
- API 文档：`docs/API.md`
- 部署与运行：`docs/DEPLOYMENT.md`

## 构建 Jar 并运行

```bash
./mvnw -DskipTests package
java -jar target/k-beat-0.0.1-SNAPSHOT.jar
```

## 配置

常用配置（`src/main/resources/application.properties`）：

- `app.assets.version`：静态资源版本号（用于 Cache Busting）
- `spring.datasource.url`：H2 数据库存储位置（默认 `jdbc:h2:file:./data/kbeat`）

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
