package com.example.product_api.exception;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Map<String, Object>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toErrorItem)
                .collect(Collectors.toList());

        Map<String, Object> body = Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "message", "Erro de validação",
                "path", request.getRequestURI(),
                "errors", errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, Object>> handleBindException(BindException ex, HttpServletRequest request) {
    List<Map<String, Object>> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(this::toErrorItem)
        .collect(Collectors.toList());

    Map<String, Object> body = Map.of(
        "timestamp", OffsetDateTime.now().toString(),
        "status", HttpStatus.BAD_REQUEST.value(),
        "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "message", "Erro de validação",
        "path", request.getRequestURI(),
        "errors", errors
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
    List<Map<String, Object>> errors = ex.getConstraintViolations()
        .stream()
        .map(this::toErrorItem)
        .collect(Collectors.toList());

    Map<String, Object> body = Map.of(
        "timestamp", OffsetDateTime.now().toString(),
        "status", HttpStatus.BAD_REQUEST.value(),
        "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "message", "Erro de validação",
        "path", request.getRequestURI(),
        "errors", errors
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        Map<String, Object> body = Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "message", ex.getMessage(),
                "path", request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private Map<String, Object> toErrorItem(FieldError fe) {
        return Map.of(
                "field", fe.getField(),
                "message", fe.getDefaultMessage(),
                "rejectedValue", fe.getRejectedValue()
        );
    }

    private Map<String, Object> toErrorItem(ConstraintViolation<?> cv) {
        String field = cv.getPropertyPath() != null ? cv.getPropertyPath().toString() : null;
        return Map.of(
                "field", field,
                "message", cv.getMessage(),
                "rejectedValue", cv.getInvalidValue()
        );
    }
}