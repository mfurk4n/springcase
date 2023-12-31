package com.javacase.javacase.exception;

import com.javacase.javacase.util.CustomErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({CompanyNotFoundException.class, EmployeeNotFoundException.class})
    public ResponseEntity<?> notFoundExceptionHandler(RuntimeException exception) {
        return CustomErrorResponse.sendError(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(CompanyNameAlreadyExistsException.class)
    public ResponseEntity<?> badRequestExceptionHandler(RuntimeException exception) {
        return CustomErrorResponse.sendError(HttpStatus.BAD_REQUEST, exception.getMessage());
    }


}
