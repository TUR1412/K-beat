(() => {
  const THEME_KEY = "kbeat:theme";
  const FILTER_KEY = "kbeat:filter";

  const toastHost = document.getElementById("toastHost");
  const todoList = document.getElementById("todoList");
  const emptyState = document.getElementById("emptyState");

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

  function renderTodoItem(todo) {
    const li = document.createElement("li");
    li.className = `todo-item ${todo.completed ? "is-completed" : ""}`.trim();
    li.dataset.id = String(todo.id);
    li.dataset.completed = todo.completed ? "true" : "false";
    li.dataset.priority = String(todo.priority);
    li.dataset.due = String(todo.dueDate);

    const completeForm = todo.completed
      ? ""
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
          <span class="meta">截止 <time datetime="${todo.dueDate}">${todo.dueDate}</time></span>
        </div>
      </div>
      <div class="todo-actions">
        ${completeForm}
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
    const triggers = Array.from(document.querySelectorAll("[data-focus-input]"));
    for (const btn of triggers) {
      btn.addEventListener("click", () => {
        if (!input) {
          return;
        }
        input.focus();
        input.scrollIntoView({ block: "center", behavior: "smooth" });
      });
    }
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

      if (emptyState) {
        emptyState.remove();
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

        updateStats();
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
          showToast(err instanceof Error ? err.message : "网络异常，请稍后重试", "error");
        } finally {
          window.setTimeout(() => item.classList.remove("celebrate"), 900);
          updateStats();
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
          window.setTimeout(() => item.remove(), 200);
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
          applyFilter(safeStorageGet(FILTER_KEY) || "all");
        }
      }
    });
  }

  initTheme();
  bindThemeToggle();
  bindFilterChips();
  bindFocusInput();
  bindAjaxAdd();
  bindAjaxActions();
  removeServerToasts();
  updateStats();
})();
