# 部署与运行

本项目不强制引入额外前端构建链，默认直接运行 Spring Boot 即可。

## 本地运行

Windows：

```bash
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

macOS / Linux：

```bash
./mvnw test
./mvnw spring-boot:run
```

访问：

- 页面：`/todos`
- API：`/api/todos`

## Jar 运行

```bash
./mvnw -DskipTests package
java -jar target/k-beat-0.0.1-SNAPSHOT.jar
```

## Docker

构建：

```bash
docker build -t k-beat:latest .
```

运行（建议把 `data/` 挂载出来做持久化）：

macOS / Linux（bash/zsh）：

```bash
docker run --rm -p 8080:8080 -v "$(pwd)/data:/app/data" k-beat:latest
```

Windows PowerShell：

```powershell
docker run --rm -p 8080:8080 -v "${PWD}/data:/app/data" k-beat:latest
```

## 数据与清理

默认使用 H2 文件库（`./data/kbeat`）：

- 要“清库重来”：删除 `data/` 目录即可（已在 `.gitignore` 中忽略）
