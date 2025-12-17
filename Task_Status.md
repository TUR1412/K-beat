# [KBEAT-NUKE-20251217] 任务看板
> **环境**: Windows 11 (`pwsh -NoLogo -NoProfile -Command '...'`) | **框架**: Spring Boot 3 + FreeMarker | **档位**: 4档 (Architect)
> **已激活矩阵**: [模块 A: 视觉矫正] + [模块 B: 逻辑直通] + [模块 E: 幽灵防御]

## 1. 需求镜像 (Requirement Mirroring)
> **我的理解**: 对 `TUR1412/K-beat` 做“原子弹级”优化升级（体验/稳健性/工程化），完成 **原子级审查** 后推送到远程仓库；最后删除本地克隆目录 `C:\wooK\K-beat`。
> **不做什么**: 不在后台启动任何长期服务进程；不抢占端口；不把 JSON/异常堆栈裸露在 UI 主区域。

## 2. 进化知识库 (Evolutionary Knowledge - Ω)
- [!] (体验) 已完成支持“撤销”，避免误触造成不可逆。
- [!] (体验) 逾期/今天到期需要显式提醒（不依赖 JS 也可见）。
- [!] (稳健) H2 file 模式依赖 `data/` 目录存在，需要启动时预创建。
- [!] (安全/体验) API 统一返回“人话错误”，并提供 `actionable_suggestion`；避免非预期异常裸露。
- [!] (工程) CI 增加并发治理与自动依赖更新（Dependabot），减少维护摩擦。

## 3. 执行清单 (Execution)
- [x] 1. 原子级审查（UI/逻辑/工程/CI）并列出清单
- [x] 2. 增加“撤销完成”（UI + API + Service + Tests）
- [x] 3. 增加“逾期/今日到期”高亮（SSR + JS 渐进增强）
- [x] 4. 增加友好错误页（避免 Whitelabel/StackTrace）
- [x] 5. 增加 H2 `data/` 目录预创建（仅 file 模式触发）
- [x] 6. 工程化增强（CI 并发/权限、Dependabot）
- [x] 7. 有限验证（`./mvnw test` / `./mvnw -DskipTests package`）
- [x] 8. 提交并推送到远程
- [x] 9. 删除本地克隆目录（仅限 `C:\wooK\K-beat`）

