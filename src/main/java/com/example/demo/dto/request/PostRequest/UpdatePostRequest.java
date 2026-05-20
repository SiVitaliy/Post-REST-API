package com.example.demo.dto.request.PostRequest;

import jakarta.validation.constraints.NotBlank;

public record UpdatePostRequest(
        @NotBlank
        String title,
        @NotBlank
        String text) {




}
