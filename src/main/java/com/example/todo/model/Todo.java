package com.example.todo.model;

import java.time.LocalDate;
import java.util.Objects;

public class Todo {
    private Long id;
    private String description;
    private boolean isCompleted;
    private String priority;
    private LocalDate dueDate;

    // 构造函数
    public Todo(Long id, String description, String priority, LocalDate dueDate) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isCompleted = false;
    }
    // 新增的构造函数，包含 isCompleted 参数
    public Todo(Long id, String description, String priority, LocalDate dueDate, boolean isCompleted) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }
    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // 重写 equals() 和 hashCode() 方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return isCompleted == todo.isCompleted &&
                Objects.equals(id, todo.id) &&
                Objects.equals(description, todo.description) &&
                Objects.equals(priority, todo.priority) &&
                Objects.equals(dueDate, todo.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, priority, dueDate, isCompleted);
    }
}
