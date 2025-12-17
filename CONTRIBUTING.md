# 贡献指南

感谢你愿意让 K-beat 变得更好。

本项目的目标是：保持“轻量可运行”的默认体验，同时提供现代化 UI 与渐进增强能力。

## 开发原则

- 不引入重型前端构建链作为强依赖（除非是可选并且有明确收益）
- UI 不在主要区域渲染原始 JSON/异常堆栈；错误信息必须“讲人话”
- 任何新增能力都要考虑“无 JS 仍可用”的回退路径
- 变更需可测试：尽量为 API/MVC 新增或调整补充测试

## 开发与验证

Windows：

```bash
.\mvnw.cmd test
```

macOS / Linux：

```bash
./mvnw test
```

如果你改动了前端资源（`src/main/resources/static/assets/`），请同步递增：

- `src/main/resources/application.properties` 中的 `app.assets.version`

## 提交信息建议

建议使用简洁、可读的提交信息，例如：

- `feat: xxx`
- `fix: xxx`
- `docs: xxx`
- `refactor: xxx`

## PR 清单

提交 PR 前请自查：

- [ ] `./mvnw test` 通过
- [ ] UI 文案/错误提示为中文且可执行（包含下一步建议）
- [ ] 新增/变更接口已更新到 `docs/API.md`
- [ ] 如修改静态资源，已更新 `app.assets.version`
