package com.example.todo.controller;

import com.example.todo.model.TodoPriority;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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

    @ModelAttribute("requestId")
    public String requestId() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return Optional.ofNullable(attributes.getAttribute("requestId", RequestAttributes.SCOPE_REQUEST))
                .map(Object::toString)
                .orElse(null);
    }
}
