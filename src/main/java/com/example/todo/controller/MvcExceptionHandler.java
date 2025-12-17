package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(annotations = Controller.class)
public class MvcExceptionHandler {

    @ExceptionHandler({
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class,
    })
    public String handleBadRequest(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("toastType", "error");
        redirectAttributes.addFlashAttribute("toastMessage", "表单参数不合法，请检查后重试");
        return "redirect:/todos";
    }
}
