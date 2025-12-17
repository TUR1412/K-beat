package com.example.todo.service;

public class TodoNotFoundException extends RuntimeException {

    private final long id;

    public TodoNotFoundException(long id) {
        super("未找到待办事项（id=" + id + "）");
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
