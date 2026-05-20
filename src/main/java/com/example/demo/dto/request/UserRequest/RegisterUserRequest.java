package com.example.demo.dto.request.UserRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "Имя не должно быть пустым")
        @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
        String fullName,

        @NotBlank(message = "Email не должен быть пустым")
        @Email(message = "Некорректный email")
        @Size(max = 150, message = "Email слишком длинный")
        String email,

        @NotBlank(message = "Пароль не должен быть пустым")
        @Size(min = 6, max = 50, message = "Пароль должен быть от 6 до 50 символов")
        String password) {

}
