package com.example.demo.dto.Dto;

import com.example.demo.dto.WithDto.PostWithCommentariesDto;
import com.example.demo.dto.container.PostImageContainerDto;

import java.time.LocalDateTime;

public record PostDto(int id, UserDto author, LocalDateTime creationDate, int numberOfLikes, int numberOfDislikes,
                      String title, String text, PostImageContainerDto postImageContainerDto) {




}
