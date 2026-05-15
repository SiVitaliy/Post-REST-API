package com.example.demo.dto.WithDto;

import com.example.demo.dto.Dto.PostDto;
import com.example.demo.dto.container.CommentaryContainerDto;

public record PostWithCommentariesDto(PostDto postDto, CommentaryContainerDto commentaryContainerDto) {



}
