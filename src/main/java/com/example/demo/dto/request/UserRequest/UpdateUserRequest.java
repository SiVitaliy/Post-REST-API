package com.example.demo.dto.request.UserRequest;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record UpdateUserRequest(
        @NotBlank(message = "Имя не должно быть пустым")
        @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
        String fullName,

        @NotBlank(message = "Email не должен быть пустым")
        @Email(message = "Некорректный email")
        @Size(max = 150, message = "Email слишком длинный")
        String email,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @PastOrPresent(message = "Дата рождения не может быть в будущем")
        LocalDate yearOfBirth,

        @Size(min = 2, max = 2, message = "Код страны должен состоять из 2 символов")
        @Pattern(regexp = "^[A-Z]{2}$", message = "Код страны должен состоять из двух заглавных латинских букв")
        String countryCode,

        @Size(max = 2000, message = "Описание не должно быть длиннее 2000 символов")
        String bio
) {
}