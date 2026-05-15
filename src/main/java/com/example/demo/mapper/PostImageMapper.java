package com.example.demo.mapper;

import com.example.demo.dto.Dto.CommentaryDto;
import com.example.demo.dto.Dto.PostImageDto;
import com.example.demo.dto.container.CommentaryContainerDto;
import com.example.demo.dto.container.PostImageContainerDto;
import com.example.demo.entity.Commentary;
import com.example.demo.entity.PostImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PostImageMapper {

    @Mapping(target = "postId", source = "post.id")
    PostImageDto toDto(PostImage postImage);
    List<PostImageDto> toDtoList(List<PostImage> postImages);

    default PostImageContainerDto toContainerDto(List<PostImage> postImages) {
        return new PostImageContainerDto(toDtoList(postImages));
    }

}
