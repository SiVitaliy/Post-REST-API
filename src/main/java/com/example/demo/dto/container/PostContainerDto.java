package com.example.demo.dto.container;

import com.example.demo.dto.Dto.PostDto;


import java.util.List;


public class PostContainerDto {


    private final List<PostDto> posts;

    public PostContainerDto(List<PostDto> posts) {
        this.posts = posts;
    }

    public List<PostDto> getPosts() {
        return posts;
    }
}
