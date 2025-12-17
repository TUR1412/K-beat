package com.example.todo.controller;

import com.example.todo.model.TodoPriority;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class GlobalModelAttributes {

    private final String assetsVersion;

    public GlobalModelAttributes(@Value("${app.assets.version:dev}") String assetsVersion) {
        this.assetsVersion = assetsVersion;
    }

    @ModelAttribute("assetsVersion")
    public String assetsVersion() {
        return assetsVersion;
    }

    @ModelAttribute("today")
    public LocalDate today() {
        return LocalDate.now();
    }

    @ModelAttribute("priorities")
    public TodoPriority[] priorities() {
        return TodoPriority.values();
    }
}
