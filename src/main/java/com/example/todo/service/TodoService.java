package com.example.todo.service;

import com.example.todo.model.Todo;
import com.example.todo.model.TodoPriority;
import com.example.todo.repository.TodoRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {

    private static final int MAX_DESCRIPTION_LENGTH = 200;

    private static final Comparator<Todo> DEFAULT_SORT =
            Comparator.comparing(Todo::isCompleted)
                    .thenComparing(Todo::getDueDate)
                    .thenComparing(todo -> todo.getPriority().getWeight(), Comparator.reverseOrder())
                    .thenComparing(Todo::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()));

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional(readOnly = true)
    public List<Todo> listTodos() {
        return todoRepository.findAll().stream().sorted(DEFAULT_SORT).toList();
    }

    @Transactional
    public Todo createTodo(String description, TodoPriority priority, LocalDate dueDate) {
        String normalizedDescription = normalizeDescription(description);
        if (normalizedDescription.isBlank()) {
            throw new IllegalArgumentException("待办事项不能为空");
        }
        if (normalizedDescription.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("待办事项最多 " + MAX_DESCRIPTION_LENGTH + " 个字符");
        }
        if (priority == null) {
            throw new IllegalArgumentException("请选择优先级");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("请选择截止日期");
        }

        Todo todo = new Todo(normalizedDescription, priority, dueDate);
        return todoRepository.save(todo);
    }

    @Transactional
    public Todo markCompleted(long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        todo.setCompleted(true);
        return todoRepository.save(todo);
    }

    @Transactional
    public void delete(long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        todoRepository.delete(todo);
    }

    private static String normalizeDescription(String description) {
        if (description == null) {
            return "";
        }
        return description.trim().replaceAll("\\s+", " ");
    }
}
