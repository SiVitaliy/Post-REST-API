package com.example.demo.dto.container;

import com.example.demo.dto.Dto.PostImageDto;

import java.util.List;

public class PostImageContainerDto {
    private final List<PostImageDto> postImages;

    public PostImageContainerDto(List<PostImageDto> postImages) {
        this.postImages = postImages;
    }

    public List<PostImageDto> getPostImages() {
        return postImages;
    }
}
