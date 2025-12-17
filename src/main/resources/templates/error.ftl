<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="color-scheme" content="light dark">
    <title>K-beat · 出错了</title>
    <link rel="stylesheet" href="/assets/app.css?v=${assetsVersion!'dev'}">
    <script src="/assets/app.js?v=${assetsVersion!'dev'}" defer></script>
</head>
<body>
<header class="topbar" role="banner">
    <div class="brand">
        <div class="brand-mark" aria-hidden="true">K</div>
        <div class="brand-text">
            <div class="brand-title">K-beat</div>
            <div class="brand-subtitle">服务器开小差了，我们马上回来</div>
        </div>
    </div>
    <div class="topbar-actions">
        <button id="themeToggle" class="icon-button" type="button" aria-label="切换主题" title="切换主题">
            <span class="icon-button__dot" aria-hidden="true"></span>
        </button>
    </div>
</header>

<main class="container" role="main">
    <section class="card card--list" style="--i: 0" aria-label="错误信息">
        <h2 class="card-title">出了点小问题</h2>

        <div class="empty-state" style="margin-top: 12px;">
            <div class="empty-title">抱歉，页面暂时不可用</div>
            <div class="empty-subtitle">你可以返回待办列表，或者稍后再试。</div>
            <div class="stats" style="margin-top: 14px;">
                <div class="stat">
                    <div class="stat-value">${status!'-'}</div>
                    <div class="stat-label">状态码</div>
                </div>
                <div class="stat">
                    <div class="stat-value">${error!'Unknown'}</div>
                    <div class="stat-label">类型</div>
                </div>
                <div class="stat">
                    <div class="stat-value">${path!'-'}</div>
                    <div class="stat-label">路径</div>
                </div>
            </div>

            <a class="btn btn--primary" href="/todos" style="margin-top: 14px; text-decoration: none;">返回待办列表</a>
        </div>
    </section>
</main>
</body>
</html>
