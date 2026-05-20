package com.example.demo.dto.WithDto;

import com.example.demo.dto.Dto.CommentaryDto;
import com.example.demo.dto.Dto.PostDto;
import com.example.demo.dto.container.CommentaryContainerDto;
import com.example.demo.dto.pageResponse.PageResponse;

public record PostWithCommentariesDto(PostDto postDto, PageResponse<CommentaryDto> commentaryPage) {



}
