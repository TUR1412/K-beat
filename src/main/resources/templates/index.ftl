<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="color-scheme" content="light dark">
    <title>K-beat · 待办清单</title>
    <link rel="stylesheet" href="/assets/app.css?v=${assetsVersion}">
    <script src="/assets/app.js?v=${assetsVersion}" defer></script>
</head>
<body>
<header class="topbar" role="banner">
    <div class="brand">
        <div class="brand-mark" aria-hidden="true">K</div>
        <div class="brand-text">
            <div class="brand-title">K-beat</div>
            <div class="brand-subtitle">把每件事都打在节拍上</div>
        </div>
    </div>
    <div class="topbar-actions">
        <button id="themeToggle" class="icon-button" type="button" aria-label="切换主题" title="切换主题">
            <span class="icon-button__dot" aria-hidden="true"></span>
        </button>
    </div>
</header>

<main class="container" role="main">
    <section class="bento" aria-label="K-beat 主面板">
        <section class="card card--form" style="--i: 0" aria-label="新增待办">
            <h2 class="card-title">新增待办</h2>
            <form id="addTodoForm" class="form js-ajax-add" action="/todos" method="post" autocomplete="off">
                <label class="field">
                    <span class="field-label">内容</span>
                    <input
                            id="descriptionInput"
                            class="input"
                            type="text"
                            name="description"
                            placeholder="比如：整理简历、跑 5km、写一段代码…"
                            maxlength="200"
                            required>
                </label>

                <div class="field-row">
                    <label class="field">
                        <span class="field-label">优先级</span>
                        <select class="select" name="priority" required>
                            <#list priorities as p>
                                <option value="${p}">${p.label}</option>
                            </#list>
                        </select>
                    </label>

                    <label class="field">
                        <span class="field-label">截止日期</span>
                        <input class="input" type="date" name="dueDate" value="${today?string('yyyy-MM-dd')}" required>
                    </label>
                </div>

                <button class="btn btn--primary" type="submit">
                    <span class="btn__text">添加</span>
                    <span class="btn__pulse" aria-hidden="true"></span>
                </button>

                <p class="hint">提示：支持无刷新操作；即使关闭 JS 也能正常使用（表单回退）。</p>
            </form>
        </section>

        <section class="card card--stats" style="--i: 1" aria-label="概览与筛选">
            <h2 class="card-title">概览</h2>
            <div class="stats" id="stats">
                <div class="stat">
                    <div class="stat-value" id="statTotal">${todos?size}</div>
                    <div class="stat-label">总计</div>
                </div>
                <div class="stat">
                    <div class="stat-value" id="statActive">-</div>
                    <div class="stat-label">进行中</div>
                </div>
                <div class="stat">
                    <div class="stat-value" id="statDone">-</div>
                    <div class="stat-label">已完成</div>
                </div>
            </div>

            <div class="filters" id="filters" role="tablist" aria-label="筛选待办">
                <button class="chip is-active" type="button" data-filter="all" role="tab" aria-selected="true">全部</button>
                <button class="chip" type="button" data-filter="active" role="tab" aria-selected="false">进行中</button>
                <button class="chip" type="button" data-filter="completed" role="tab" aria-selected="false">已完成</button>
            </div>

            <div class="small-muted">
                <span>今天：</span>
                <time datetime="${today?string('yyyy-MM-dd')}">${today?string('yyyy-MM-dd')}</time>
            </div>
        </section>

        <section class="card card--list" style="--i: 2" aria-label="待办列表">
            <div class="card-head">
                <h2 class="card-title">待办列表</h2>
                <div class="small-muted">点击“完成”会触发轻微庆祝反馈</div>
            </div>

            <#if toastMessage??>
                <div class="toast toast--${toastType!'info'}" role="status" aria-live="polite" data-toast>
                    ${toastMessage?html}
                </div>
            </#if>

            <ul id="todoList" class="todo-list" aria-label="待办事项列表">
                <#if todos?size == 0>
                    <li id="emptyState" class="empty-state">
                        <div class="empty-visual" aria-hidden="true">
                            <svg viewBox="0 0 220 140" width="220" height="140" focusable="false">
                                <defs>
                                    <linearGradient id="g" x1="0" y1="0" x2="1" y2="1">
                                        <stop offset="0%" stop-color="currentColor" stop-opacity="0.35"/>
                                        <stop offset="100%" stop-color="currentColor" stop-opacity="0.05"/>
                                    </linearGradient>
                                </defs>
                                <rect x="10" y="18" width="200" height="110" rx="18" fill="url(#g)"/>
                                <path d="M38 52h112" stroke="currentColor" stroke-opacity="0.35" stroke-width="10" stroke-linecap="round"/>
                                <path d="M38 82h144" stroke="currentColor" stroke-opacity="0.2" stroke-width="10" stroke-linecap="round"/>
                                <path d="M38 110h92" stroke="currentColor" stroke-opacity="0.12" stroke-width="10" stroke-linecap="round"/>
                            </svg>
                        </div>
                        <div class="empty-title">空空如也</div>
                        <div class="empty-subtitle">先写下今天第一拍，让节奏开始。</div>
                        <button class="btn btn--ghost" type="button" data-focus-input>立即添加</button>
                    </li>
                <#else>
                    <#list todos as todo>
                        <li class="todo-item ${todo.isCompleted()?string('is-completed','')}" data-id="${todo.id}"
                            data-completed="${todo.isCompleted()?c}" data-priority="${todo.priority}" data-due="${todo.dueDate?string('yyyy-MM-dd')}">
                            <div class="todo-main">
                                <div class="todo-title">${todo.description?html}</div>
                                <div class="todo-meta">
                                    <span class="badge ${todo.priority.cssClass}">${todo.priority.label}</span>
                                    <span class="meta">截止 <time datetime="${todo.dueDate?string('yyyy-MM-dd')}">${todo.dueDate?string('yyyy-MM-dd')}</time></span>
                                </div>
                            </div>
                            <div class="todo-actions">
                                <#if !todo.isCompleted()>
                                    <form class="inline-form js-ajax-complete" action="/todos/complete/${todo.id}" method="post">
                                        <button class="btn btn--ghost" type="submit">完成</button>
                                    </form>
                                </#if>
                                <form class="inline-form js-ajax-delete" action="/todos/delete/${todo.id}" method="post">
                                    <button class="btn btn--ghost btn--danger" type="submit">删除</button>
                                </form>
                            </div>
                        </li>
                    </#list>
                </#if>
            </ul>
        </section>
    </section>
</main>

<div id="toastHost" class="toast-host" aria-live="polite" aria-atomic="true"></div>
</body>
</html>
