package com.example.demo.dto.request.CommentaryRequest;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentaryRequest(@NotBlank String text) {

}
