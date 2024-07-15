package com.morght.foro_hub.infra.errors;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ErrorMananer {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity error404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity error400(MethodArgumentNotValidException e) {
        List<errorValidation> errors = e.getFieldErrors().stream().map(errorValidation::new).toList();
        return ResponseEntity.badRequest().body(errors);
    }

    private record errorValidation(String campo, String error) {
        public errorValidation(FieldError e) {
            this(e.getField(), e.getDefaultMessage());
        }
    }
}
