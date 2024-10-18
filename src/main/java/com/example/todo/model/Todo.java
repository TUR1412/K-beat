package com.example.todo.model;

import java.time.LocalDate;

public class Todo {
    private Long id;
    private String description;
    private boolean isCompleted;
    private String priority; // 任务优先级
    private LocalDate dueDate; // 任务截止日期

    // 构造函数
    public Todo(Long id, String description, String priority, LocalDate dueDate) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isCompleted = false;
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
}
