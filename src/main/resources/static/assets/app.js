(() => {
  const THEME_KEY = "kbeat:theme";
  const FILTER_KEY = "kbeat:filter";

  const toastHost = document.getElementById("toastHost");
  const todoList = document.getElementById("todoList");

  function safeStorageGet(key) {
    try {
      return window.localStorage.getItem(key);
    } catch {
      return null;
    }
  }

  function safeStorageSet(key, value) {
    try {
      window.localStorage.setItem(key, value);
    } catch {
      // ignore
    }
  }

  function showToast(message, type = "info", timeoutMs = 2400) {
    if (!toastHost) {
      return;
    }

    const el = document.createElement("div");
    el.className = `toast toast--${type}`;
    el.setAttribute("role", "status");
    el.setAttribute("aria-live", "polite");
    el.textContent = message;

    toastHost.appendChild(el);
    window.setTimeout(() => {
      el.remove();
    }, timeoutMs);
  }

  async function safeReadJson(response) {
    try {
      return await response.json();
    } catch {
      return null;
    }
  }

  function bindGlobalErrorBoundary() {
    let shown = false;
    const notify = () => {
      if (shown) {
        return;
      }
      shown = true;
      showToast("页面发生错误，请刷新后重试", "error", 4200);
    };

    window.addEventListener("error", notify);
    window.addEventListener("unhandledrejection", notify);
  }

  function setTheme(theme) {
    document.body.dataset.theme = theme;
    safeStorageSet(THEME_KEY, theme);
  }

  function initTheme() {
    const saved = safeStorageGet(THEME_KEY);
    if (saved === "dark" || saved === "light") {
      setTheme(saved);
      return;
    }
    const prefersDark =
      window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches;
    setTheme(prefersDark ? "dark" : "light");
  }

  function getTodoItems() {
    if (!todoList) {
      return [];
    }
    return Array.from(todoList.querySelectorAll(".todo-item"));
  }

  function isCompletedItem(item) {
    return item.classList.contains("is-completed") || item.dataset.completed === "true";
  }

  function getTodayIso() {
    const fromDom = document.body?.dataset?.today;
    if (fromDom && /^\d{4}-\d{2}-\d{2}$/.test(fromDom)) {
      return fromDom;
    }

    const now = new Date();
    const y = String(now.getFullYear());
    const m = String(now.getMonth() + 1).padStart(2, "0");
    const d = String(now.getDate()).padStart(2, "0");
    return `${y}-${m}-${d}`;
  }

  function getDueKind(item) {
    const dueIso = item.dataset.due;
    if (!dueIso || !/^\d{4}-\d{2}-\d{2}$/.test(dueIso)) {
      return null;
    }

    if (isCompletedItem(item)) {
      return null;
    }

    const todayIso = getTodayIso();
    if (dueIso < todayIso) {
      return "overdue";
    }
    if (dueIso === todayIso) {
      return "today";
    }
    return null;
  }

  function ensureDueBadge(item, dueKind) {
    const meta = item.querySelector(".todo-meta");
    if (!(meta instanceof HTMLElement)) {
      return;
    }

    const existing = meta.querySelector("[data-due-badge]");
    if (!dueKind) {
      if (existing) {
        existing.remove();
      }
      return;
    }

    const badge = existing instanceof HTMLElement ? existing : document.createElement("span");
    badge.setAttribute("data-due-badge", "");
    badge.className = dueKind === "overdue" ? "badge badge--overdue" : "badge badge--today";
    badge.textContent = dueKind === "overdue" ? "逾期" : "今天";

    if (!existing) {
      const metaText = meta.querySelector(".meta");
      if (metaText) {
        meta.insertBefore(badge, metaText);
      } else {
        meta.appendChild(badge);
      }
    }
  }

  function applyDueState(item) {
    item.classList.remove("is-overdue", "is-today");

    const dueKind = getDueKind(item);
    if (dueKind === "overdue") {
      item.classList.add("is-overdue");
    } else if (dueKind === "today") {
      item.classList.add("is-today");
    }

    ensureDueBadge(item, dueKind);
  }

  function updateDueStates() {
    for (const item of getTodoItems()) {
      applyDueState(item);
    }
  }

  function createEmptyStateElement() {
    const li = document.createElement("li");
    li.id = "emptyState";
    li.className = "empty-state";
    li.innerHTML = `
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
    `;
    return li;
  }

  function ensureRootEmptyState() {
    if (!todoList) {
      return;
    }

    const hasTodos = getTodoItems().length > 0;
    const existing = document.getElementById("emptyState");

    if (hasTodos) {
      if (existing) {
        existing.remove();
      }
      return;
    }

    if (!existing) {
      todoList.appendChild(createEmptyStateElement());
    }
  }

  function updateStats() {
    const items = getTodoItems();
    const total = items.length;
    const done = items.filter(isCompletedItem).length;
    const active = total - done;

    const elTotal = document.getElementById("statTotal");
    const elActive = document.getElementById("statActive");
    const elDone = document.getElementById("statDone");

    if (elTotal) elTotal.textContent = String(total);
    if (elActive) elActive.textContent = String(active);
    if (elDone) elDone.textContent = String(done);
  }

  function ensureFilterEmptyState(visibleCount) {
    if (!todoList) {
      return;
    }
    const existing = document.getElementById("filterEmptyState");
    const hasTodos = getTodoItems().length > 0;

    if (!hasTodos) {
      if (existing) {
        existing.remove();
      }
      return;
    }

    if (visibleCount > 0) {
      if (existing) {
        existing.remove();
      }
      return;
    }

    if (existing) {
      return;
    }

    const li = document.createElement("li");
    li.id = "filterEmptyState";
    li.className = "empty-state";
    li.innerHTML =
      '<div class="empty-title">没有匹配的待办</div><div class="empty-subtitle">换个筛选条件，或者先添加一个新节拍。</div>';
    todoList.appendChild(li);
  }

  function applyFilter(filter) {
    const items = getTodoItems();
    let visible = 0;

    for (const item of items) {
      const completed = isCompletedItem(item);
      const shouldHide =
        (filter === "active" && completed) || (filter === "completed" && !completed);
      item.hidden = shouldHide;
      if (!shouldHide) {
        visible += 1;
      }
    }

    ensureFilterEmptyState(visible);
  }

  function setActiveFilterChip(filter) {
    const host = document.getElementById("filters");
    if (!host) {
      return;
    }
    const chips = Array.from(host.querySelectorAll("[data-filter]"));
    for (const chip of chips) {
      const isActive = chip.dataset.filter === filter;
      chip.classList.toggle("is-active", isActive);
      chip.setAttribute("aria-selected", isActive ? "true" : "false");
    }
  }

  function createCompleteForm(id) {
    const form = document.createElement("form");
    form.className = "inline-form js-ajax-complete";
    form.action = `/todos/complete/${id}`;
    form.method = "post";
    form.innerHTML = '<button class="btn btn--ghost" type="submit">完成</button>';
    return form;
  }

  function createReopenForm(id) {
    const form = document.createElement("form");
    form.className = "inline-form js-ajax-reopen";
    form.action = `/todos/reopen/${id}`;
    form.method = "post";
    form.innerHTML = '<button class="btn btn--ghost" type="submit">撤销</button>';
    return form;
  }

  function renderTodoItem(todo) {
    const li = document.createElement("li");
    const todayIso = getTodayIso();
    const dueIso = String(todo.dueDate);
    const dueKind =
      !todo.completed && /^\d{4}-\d{2}-\d{2}$/.test(dueIso) && dueIso < todayIso
        ? "overdue"
        : !todo.completed && /^\d{4}-\d{2}-\d{2}$/.test(dueIso) && dueIso === todayIso
          ? "today"
          : null;

    li.className = `todo-item ${todo.completed ? "is-completed" : ""} ${dueKind === "overdue" ? "is-overdue" : dueKind === "today" ? "is-today" : ""}`.trim();
    li.dataset.id = String(todo.id);
    li.dataset.completed = todo.completed ? "true" : "false";
    li.dataset.priority = String(todo.priority);
    li.dataset.due = dueIso;

    const dueBadge =
      dueKind === "overdue"
        ? '<span class="badge badge--overdue" data-due-badge>逾期</span>'
        : dueKind === "today"
          ? '<span class="badge badge--today" data-due-badge>今天</span>'
          : "";

    const primaryForm = todo.completed
      ? `
        <form class="inline-form js-ajax-reopen" action="/todos/reopen/${todo.id}" method="post">
          <button class="btn btn--ghost" type="submit">撤销</button>
        </form>
      `
      : `
        <form class="inline-form js-ajax-complete" action="/todos/complete/${todo.id}" method="post">
          <button class="btn btn--ghost" type="submit">完成</button>
        </form>
      `;

    li.innerHTML = `
      <div class="todo-main">
        <div class="todo-title"></div>
        <div class="todo-meta">
          <span class="badge ${todo.priorityCssClass}"></span>
          ${dueBadge}
          <span class="meta">截止 <time datetime="${todo.dueDate}">${todo.dueDate}</time></span>
        </div>
      </div>
      <div class="todo-actions">
        ${primaryForm}
        <form class="inline-form js-ajax-delete" action="/todos/delete/${todo.id}" method="post">
          <button class="btn btn--ghost btn--danger" type="submit">删除</button>
        </form>
      </div>
    `;

    li.querySelector(".todo-title").textContent = todo.description;
    li.querySelector(".badge").textContent = todo.priorityLabel;
    return li;
  }

  function removeServerToasts() {
    const serverToasts = Array.from(document.querySelectorAll("[data-toast]"));
    for (const t of serverToasts) {
      showToast(t.textContent || "完成", t.classList.contains("toast--error") ? "error" : "success");
      t.remove();
    }
  }

  function bindFocusInput() {
    const input = document.getElementById("descriptionInput");
    if (!(input instanceof HTMLInputElement)) {
      return;
    }

    document.addEventListener("click", (e) => {
      const target = e.target instanceof HTMLElement ? e.target : null;
      const trigger = target ? target.closest("[data-focus-input]") : null;
      if (!trigger) {
        return;
      }
      input.focus();
      input.scrollIntoView({ block: "center", behavior: "smooth" });
    });
  }

  function bindThemeToggle() {
    const btn = document.getElementById("themeToggle");
    if (!btn) {
      return;
    }
    btn.addEventListener("click", () => {
      const current = document.body.dataset.theme === "dark" ? "dark" : "light";
      setTheme(current === "dark" ? "light" : "dark");
    });
  }

  function bindFilterChips() {
    const host = document.getElementById("filters");
    if (!host) {
      return;
    }

    const saved = safeStorageGet(FILTER_KEY) || "all";
    const initial = ["all", "active", "completed"].includes(saved) ? saved : "all";
    setActiveFilterChip(initial);
    applyFilter(initial);

    host.addEventListener("click", (e) => {
      const target = e.target instanceof HTMLElement ? e.target : null;
      const chip = target ? target.closest("[data-filter]") : null;
      if (!(chip instanceof HTMLElement)) {
        return;
      }

      const filter = chip.dataset.filter || "all";
      safeStorageSet(FILTER_KEY, filter);
      setActiveFilterChip(filter);
      applyFilter(filter);
    });
  }

  function bindAjaxAdd() {
    const form = document.querySelector(".js-ajax-add");
    if (!(form instanceof HTMLFormElement)) {
      return;
    }
    if (!window.fetch) {
      return;
    }

    form.addEventListener("submit", async (e) => {
      if (!todoList) {
        return;
      }
      e.preventDefault();

      const fd = new FormData(form);
      const description = String(fd.get("description") || "").trim();
      const priority = String(fd.get("priority") || "");
      const dueDate = String(fd.get("dueDate") || "");

      if (!description) {
        showToast("待办事项不能为空", "error");
        return;
      }
      if (!dueDate) {
        showToast("请选择截止日期", "error");
        return;
      }

      const currentEmptyState = document.getElementById("emptyState");
      if (currentEmptyState) {
        currentEmptyState.remove();
      }

      const optimistic = document.createElement("li");
      optimistic.className = "todo-item is-loading";
      optimistic.dataset.completed = "false";
      optimistic.dataset.id = `temp-${Date.now()}`;
      optimistic.innerHTML = `
        <div class="todo-main">
          <div class="todo-title"></div>
          <div class="todo-meta">
            <span class="badge"></span>
            <span class="meta">正在添加…</span>
          </div>
        </div>
      `;
      optimistic.querySelector(".todo-title").textContent = description;
      optimistic.querySelector(".badge").textContent = "提交中";
      todoList.prepend(optimistic);

      updateStats();
      applyFilter(safeStorageGet(FILTER_KEY) || "all");

      try {
        const res = await fetch("/api/todos", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ description, priority, dueDate }),
        });

        if (!res.ok) {
          const err = await safeReadJson(res);
          throw new Error((err && err.message) || "添加失败");
        }

        const todo = await res.json();
        const real = renderTodoItem(todo);
        optimistic.replaceWith(real);
        applyDueState(real);

        form.reset();
        const dueDateInput = form.querySelector('input[name="dueDate"]');
        if (dueDateInput instanceof HTMLInputElement) {
          dueDateInput.value = dueDate;
        }
        showToast("已添加待办事项", "success");
      } catch (err) {
        optimistic.remove();
        showToast(err instanceof Error ? err.message : "网络异常，请稍后重试", "error");
      } finally {
        updateStats();
        ensureRootEmptyState();
        updateDueStates();
        applyFilter(safeStorageGet(FILTER_KEY) || "all");
      }
    });
  }

  function bindAjaxActions() {
    if (!window.fetch) {
      return;
    }

    document.addEventListener("submit", async (e) => {
      const target = e.target;
      if (!(target instanceof HTMLFormElement)) {
        return;
      }
      const item = target.closest(".todo-item");
      if (!(item instanceof HTMLElement)) {
        return;
      }

      const id = item.dataset.id;
      if (!id || id.startsWith("temp-")) {
        return;
      }

      if (target.classList.contains("js-ajax-complete")) {
        e.preventDefault();

        const previous = {
          completed: isCompletedItem(item),
          html: item.innerHTML,
        };

        item.classList.add("is-completed", "celebrate");
        item.dataset.completed = "true";
        target.remove();
        item.classList.remove("is-overdue", "is-today");
        ensureDueBadge(item, null);

        const actions = item.querySelector(".todo-actions");
        if (actions && !actions.querySelector(".js-ajax-reopen")) {
          actions.insertBefore(createReopenForm(id), actions.firstChild);
        }

        updateStats();
        updateDueStates();
        applyFilter(safeStorageGet(FILTER_KEY) || "all");

        try {
          const res = await fetch(`/api/todos/${id}/complete`, { method: "POST" });
          if (!res.ok) {
            const err = await safeReadJson(res);
            throw new Error((err && err.message) || "标记失败");
          }
          showToast("已标记完成", "success");
        } catch (err) {
          item.classList.toggle("is-completed", previous.completed);
          item.dataset.completed = previous.completed ? "true" : "false";
          item.classList.remove("celebrate");
          item.innerHTML = previous.html;
          applyDueState(item);
          showToast(err instanceof Error ? err.message : "网络异常，请稍后重试", "error");
        } finally {
          window.setTimeout(() => item.classList.remove("celebrate"), 900);
          updateStats();
          updateDueStates();
          applyFilter(safeStorageGet(FILTER_KEY) || "all");
        }
        return;
      }

      if (target.classList.contains("js-ajax-reopen")) {
        e.preventDefault();

        const previous = {
          completed: isCompletedItem(item),
          html: item.innerHTML,
        };

        item.classList.remove("is-completed");
        item.dataset.completed = "false";
        target.remove();

        const actions = item.querySelector(".todo-actions");
        if (actions && !actions.querySelector(".js-ajax-complete")) {
          actions.insertBefore(createCompleteForm(id), actions.firstChild);
        }

        applyDueState(item);
        updateStats();
        updateDueStates();
        applyFilter(safeStorageGet(FILTER_KEY) || "all");

        try {
          const res = await fetch(`/api/todos/${id}/reopen`, { method: "POST" });
          if (!res.ok) {
            const err = await safeReadJson(res);
            throw new Error((err && err.message) || "撤销失败");
          }
          showToast("已撤销完成", "success");
        } catch (err) {
          item.classList.toggle("is-completed", previous.completed);
          item.dataset.completed = previous.completed ? "true" : "false";
          item.innerHTML = previous.html;
          applyDueState(item);
          showToast(err instanceof Error ? err.message : "网络异常，请稍后重试", "error");
        } finally {
          updateStats();
          updateDueStates();
          applyFilter(safeStorageGet(FILTER_KEY) || "all");
        }
        return;
      }

      if (target.classList.contains("js-ajax-delete")) {
        e.preventDefault();

        const parent = item.parentElement;
        const next = item.nextElementSibling;
        item.classList.add("is-removing");

        try {
          const res = await fetch(`/api/todos/${id}`, { method: "DELETE" });
          if (!res.ok) {
            const err = await safeReadJson(res);
            throw new Error((err && err.message) || "删除失败");
          }
          window.setTimeout(() => {
            item.remove();
            updateStats();
            ensureRootEmptyState();
            updateDueStates();
            applyFilter(safeStorageGet(FILTER_KEY) || "all");
          }, 200);
          showToast("已删除", "success");
        } catch (err) {
          item.classList.remove("is-removing");
          if (parent) {
            if (next) {
              parent.insertBefore(item, next);
            } else {
              parent.appendChild(item);
            }
          }
          showToast(err instanceof Error ? err.message : "网络异常，请稍后重试", "error");
        } finally {
          updateStats();
          ensureRootEmptyState();
          updateDueStates();
          applyFilter(safeStorageGet(FILTER_KEY) || "all");
        }
      }
    });
  }

  initTheme();
  bindGlobalErrorBoundary();
  bindThemeToggle();
  bindFilterChips();
  bindFocusInput();
  bindAjaxAdd();
  bindAjaxActions();
  removeServerToasts();
  updateStats();
  ensureRootEmptyState();
  updateDueStates();
})();
