package com.example.demo.mapper;

import com.example.demo.dto.Dto.CommentaryDto;
import com.example.demo.dto.WithDto.PostWithCommentariesDto;
import com.example.demo.dto.container.CommentaryContainerDto;
import com.example.demo.dto.request.CommentaryRequest.CreateCommentaryRequest;
import com.example.demo.dto.request.CommentaryRequest.UpdateCommentaryRequest;
import com.example.demo.dto.request.PostRequest.UpdatePostRequest;
import com.example.demo.entity.Commentary;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.List;
@Mapper(componentModel = "spring", imports = {LocalDateTime.class})

public interface CommentaryMapper {
    @Mapping(target = "postId", expression = "java(commentary.getPost().getId())")
    CommentaryDto toDto(Commentary commentary);

    List<CommentaryDto> toDtoList(List<Commentary> commentaries);

    default CommentaryContainerDto toContainerDto(List<Commentary> commentaries) {
        return new CommentaryContainerDto(toDtoList(commentaries));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "user")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "creationDate", expression = "java(LocalDateTime.now())")
    @Mapping(target="text", source = "request.text")
    Commentary toEntity(CreateCommentaryRequest request, Post post, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "post",ignore = true)
    @Mapping(target = "text" , source = "request.text")
    Commentary toEntity(@MappingTarget Commentary commentary, UpdateCommentaryRequest request);
    }
