package com.example.todo.controller;

import com.example.todo.service.TodoNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(TodoNotFoundException e) {
        return new ApiError(e.getMessage(), "请刷新列表或确认该待办是否已被删除");
    }

    @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(RuntimeException e) {
        return new ApiError(e.getMessage(), "请检查输入内容后重试");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException e) {
        String message =
                e.getBindingResult().getFieldErrors().stream()
                        .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                        .findFirst()
                        .orElse("参数校验失败");
        return new ApiError("参数校验失败：" + message, "请检查必填项后重试");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnexpected(Exception e) {
        log.error("Unhandled exception", e);
        return new ApiError("服务器开小差了，请稍后重试", "如持续发生，请联系维护者并附上发生时间");
    }

    public record ApiError(String message, String actionable_suggestion) {}
}
