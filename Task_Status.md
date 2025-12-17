# [GKB-NUKE-20251217] 任务看板
> **环境**: Windows 11 (pwsh -NoLogo -NoProfile -Command '...') | **框架**: 纯静态站点 (HTML/CSS/JS) | **档位**: 4档 (Architect)
> **已激活矩阵**: [模块 A: 视觉矫正] + [模块 E: 幽灵防御] + [模块 B: 奥卡姆直通]

## 1. 需求镜像 (Requirement Mirroring)
> **我的理解**: 对 `https://github.com/TUR1412/GameKnowledge-Base.git` 做“核弹级”优化与美化（UI/UX 统一、链接与资源修复、性能与可维护性增强），完成后推送到远程仓库；然后删除 `C:\wooK\GameKnowledge-Base` 本地克隆目录。
> **不做什么**: 不会后台启动任何长期服务进程（不抢占端口）；不引入重型框架/构建链强行改变部署方式（保持静态可直接托管）。

## 2. 进化知识库 (Evolutionary Knowledge - Ω)
- [!] (发现) 现有页面存在大量指向不存在的 `.html` 页面链接，导致导航断裂。
- [!] (发现) 多个 SVG 图标资源在页面中被引用但仓库中不存在，出现破图。
- [!] (发现) `scripts.js` 粒子效果为每个粒子创建 `setInterval`，有明显 CPU/内存浪费空间。
- [!] (发现) `styles.css` 内存在变量命名不一致（如 `--text-primary/--text-secondary` 未定义），以及重复/覆盖样式段落，导致维护成本高。

## 3. 执行清单 (Execution)
- [ ] 1. 统一页面布局与导航（含移动端与主题切换）
- [ ] 2. 修复断链与破图（用动态详情页承接缺失内容）
- [ ] 3. 优化交互动效与性能（粒子/动画/hover 去 JS 化）
- [ ] 4. 增强可访问性与 SEO（skip link、meta、focus、空状态）
- [ ] 5. 有限验证（`node --check` / 链接与资源自检脚本）
- [ ] 6. 提交并推送到远程
- [ ] 7. 删除本地克隆目录（仅限 `C:\wooK\GameKnowledge-Base`）

