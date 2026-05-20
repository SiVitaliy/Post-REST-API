package com.example.demo.dto.error;

import java.util.List;

public record ValidationErrorResponse (
        String code,
        String message,
        List<FieldErrorDto> errors

){
}
