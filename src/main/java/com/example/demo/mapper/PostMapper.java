package com.example.demo.mapper;

import com.example.demo.dto.Dto.PostDto;
import com.example.demo.dto.WithDto.PostWithCommentariesDto;
import com.example.demo.dto.container.CommentaryContainerDto;
import com.example.demo.dto.request.PostRequest.CreatePostRequest;
import com.example.demo.dto.request.PostRequest.UpdatePostRequest;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring" , uses = {UserMapper.class,CommentaryMapper.class,PostImageMapper.class})
public interface PostMapper {

    @Mapping(target = "postImageContainerDto", source = "images")
    PostDto toDto(Post post);
    Post toEntity(PostDto postDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "user")
    @Mapping(target = "creationDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "numberOfLikes", constant = "0")
    @Mapping(target = "numberOfDislikes", constant = "0")
    @Mapping(target = "commentaries", ignore = true)
    Post toEntity(CreatePostRequest request, User user);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    Post toEntity(@MappingTarget Post post, UpdatePostRequest request);


//    @Mapping(target = "postDto", source = "postDto")
//    @Mapping(target = "commentaryContainerDto", source = "commentaries")
//    PostWithCommentariesDto toPostWithCommentariesDto(PostDto postDto, CommentaryContainerDto commentaries);
//


}
