package com.example.demo.controller;

import com.example.demo.dto.error.ErrorResponseDto;
import com.example.demo.dto.error.FieldErrorDto;
import com.example.demo.dto.error.ValidationErrorResponse;
import com.example.demo.util.EmailAlreadyExistsException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        List<FieldErrorDto> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorDto(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return ResponseEntity
                .badRequest()
                .body(new ValidationErrorResponse(
                        "validation_error",
                        "Запрос содержит некорректные данные",
                        errors
                ));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(ValidationException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ValidationErrorResponse(
                        "validation_error",
                        "Запрос содержит некорректные данные",
                        List.of(new FieldErrorDto("data",ex.getMessage())
                )))
                ;

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(
                        "auth.bad_credentials",
                        "Неверная почта или пароль"
                ));
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyExists(EmailAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto("user.email_already_exists",ex.getMessage()));

    }
}
