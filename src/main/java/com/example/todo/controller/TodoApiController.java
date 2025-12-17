package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.model.TodoPriority;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
@Validated
public class TodoApiController {

    private final TodoService todoService;

    public TodoApiController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoDto> list() {
        return todoService.listTodos().stream().map(TodoDto::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDto create(@Valid @RequestBody CreateTodoRequest request) {
        Todo todo = todoService.createTodo(request.description(), request.priority(), request.dueDate());
        return TodoDto.from(todo);
    }

    @PostMapping("/{id}/complete")
    public TodoDto complete(@PathVariable long id) {
        return TodoDto.from(todoService.markCompleted(id));
    }

    @PostMapping("/{id}/reopen")
    public TodoDto reopen(@PathVariable long id) {
        return TodoDto.from(todoService.reopen(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        todoService.delete(id);
    }

    public record TodoDto(
            long id,
            String description,
            boolean completed,
            TodoPriority priority,
            LocalDate dueDate,
            String priorityLabel,
            String priorityCssClass) {

        public static TodoDto from(Todo todo) {
            return new TodoDto(
                    todo.getId(),
                    todo.getDescription(),
                    todo.isCompleted(),
                    todo.getPriority(),
                    todo.getDueDate(),
                    todo.getPriority().getLabel(),
                    todo.getPriority().getCssClass());
        }
    }

    public record CreateTodoRequest(
            @NotBlank @Size(max = 200) String description,
            @NotNull TodoPriority priority,
            @NotNull LocalDate dueDate) {}
}
