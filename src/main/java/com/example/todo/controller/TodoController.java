package com.example.todo.controller;

import com.example.todo.model.TodoPriority;
import com.example.todo.service.TodoNotFoundException;
import com.example.todo.service.TodoService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TodoController {

    private final TodoService todoService;
    private final String assetsVersion;

    public TodoController(TodoService todoService, @Value("${app.assets.version}") String assetsVersion) {
        this.todoService = todoService;
        this.assetsVersion = assetsVersion;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/todos";
    }

    @GetMapping("/todos")
    public String getTodos(Model model) {
        model.addAttribute("todos", todoService.listTodos());
        model.addAttribute("priorities", TodoPriority.values());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("assetsVersion", assetsVersion);
        return "index";
    }

    @PostMapping("/todos")
    public String addTodo(
            @RequestParam String description,
            @RequestParam TodoPriority priority,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            RedirectAttributes redirectAttributes) {
        try {
            todoService.createTodo(description, priority, dueDate);
            redirectAttributes.addFlashAttribute("toastType", "success");
            redirectAttributes.addFlashAttribute("toastMessage", "已添加待办事项");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastType", "error");
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
        }
        return "redirect:/todos";
    }

    @PostMapping("/todos/complete/{id}")
    public String completeTodo(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            todoService.markCompleted(id);
            redirectAttributes.addFlashAttribute("toastType", "success");
            redirectAttributes.addFlashAttribute("toastMessage", "已标记完成");
        } catch (TodoNotFoundException e) {
            redirectAttributes.addFlashAttribute("toastType", "error");
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
        }
        return "redirect:/todos";
    }

    @PostMapping("/todos/delete/{id}")
    public String deleteTodo(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            todoService.delete(id);
            redirectAttributes.addFlashAttribute("toastType", "success");
            redirectAttributes.addFlashAttribute("toastMessage", "已删除");
        } catch (TodoNotFoundException e) {
            redirectAttributes.addFlashAttribute("toastType", "error");
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
        }
        return "redirect:/todos";
    }
}
