package com.example.todo.model;
import java.time.LocalDate;
public class Todo {
    private Long id;
    private String description;
    private boolean isCompleted;
    private LocalDate dueDate; // 新增：任务截止日期
    // 构造函数、getter和setter省略
    public Todo(Long id, String description, LocalDate dueDate) {
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = false;
    }
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
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}