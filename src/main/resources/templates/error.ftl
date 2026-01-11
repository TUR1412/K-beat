<#assign pageTitle = "K-beat · 出错了">
<#assign pageDescription = "K-beat 错误页：提供友好提示与请求ID，便于快速定位问题。">
<#assign topbarTitle = "K-beat">
<#assign topbarSubtitle = "服务器开小差了，我们马上回来">
<#include "partials/_head.ftl">
<#include "partials/_topbar.ftl">

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
                <div class="stat">
                    <div class="stat-value">${requestId!'-'}</div>
                    <div class="stat-label">请求ID</div>
                </div>
            </div>

            <a class="btn btn--primary" href="/todos" style="margin-top: 14px; text-decoration: none;">返回待办列表</a>
        </div>
    </section>
</main>
<#include "partials/_foot.ftl">
