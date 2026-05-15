package com.example.demo.dto.request.CommentaryRequest;

import com.example.demo.dto.Dto.UserDto;

public record CreateCommentaryRequest(UserDto author, String text) {
}
