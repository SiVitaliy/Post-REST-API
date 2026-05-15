package com.example.demo.dto.request.UserRequest;

import java.time.LocalDate;

public record UpdateUserRequest(String fullName, String email, LocalDate yearOfBirth, String countryCode, String bio) {
}