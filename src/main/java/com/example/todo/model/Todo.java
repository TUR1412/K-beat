package com.example.todo.model;
public class Todo {
    private Long id;
    private String description;
    private boolean isCompleted;
    // 构造函数、getter和setter省略
    public Todo(Long id, String description) {
        this.id = id;
        this.description = description;
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
}