package com.example.demo.dto.request.CommentaryRequest;

import com.example.demo.dto.Dto.UserDto;
import jakarta.validation.constraints.NotBlank;

public record CreateCommentaryRequest(UserDto author, @NotBlank String text) {
}
