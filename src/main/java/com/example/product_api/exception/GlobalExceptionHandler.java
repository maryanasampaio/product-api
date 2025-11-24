package com.example.product_api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> tratarErro(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}