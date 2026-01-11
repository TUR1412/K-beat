<#assign pageTitle = "K-beat · 待办清单">
<#assign pageDescription = "K-beat 是一个带节拍感的待办清单：服务端渲染为主 + 渐进增强 + 持久化数据。">
<#assign bodyDataToday = today?string("yyyy-MM-dd")>
<#assign topbarTitle = "K-beat">
<#assign topbarSubtitle = "把每件事都打在节拍上">
<#include "partials/_head.ftl">
<#include "partials/_topbar.ftl">

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
                    <#assign todayIso = today?string('yyyy-MM-dd')>
                    <#list todos as todo>
                        <#assign dueIso = todo.dueDate?string('yyyy-MM-dd')>
                        <#assign dueClass = ''>
                        <#if !todo.isCompleted() && (dueIso < todayIso)>
                            <#assign dueClass = 'is-overdue'>
                        <#elseif !todo.isCompleted() && (dueIso == todayIso)>
                            <#assign dueClass = 'is-today'>
                        </#if>
                        <li class="todo-item ${todo.isCompleted()?string('is-completed','')} ${dueClass}" data-id="${todo.id}"
                            data-completed="${todo.isCompleted()?c}" data-priority="${todo.priority}" data-due="${dueIso}">
                            <div class="todo-main">
                                <div class="todo-title">${todo.description?html}</div>
                                <div class="todo-meta">
                                    <span class="badge ${todo.priority.cssClass}">${todo.priority.label}</span>
                                    <#if !todo.isCompleted() && (dueIso < todayIso)>
                                        <span class="badge badge--overdue" data-due-badge>逾期</span>
                                    <#elseif !todo.isCompleted() && (dueIso == todayIso)>
                                        <span class="badge badge--today" data-due-badge>今天</span>
                                    </#if>
                                    <span class="meta">截止 <time datetime="${dueIso}">${dueIso}</time></span>
                                </div>
                            </div>
                            <div class="todo-actions">
                                <#if !todo.isCompleted()>
                                    <form class="inline-form js-ajax-complete" action="/todos/complete/${todo.id}" method="post">
                                        <button class="btn btn--ghost" type="submit">完成</button>
                                    </form>
                                <#else>
                                    <form class="inline-form js-ajax-reopen" action="/todos/reopen/${todo.id}" method="post">
                                        <button class="btn btn--ghost" type="submit">撤销</button>
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

<#include "partials/_foot.ftl">
