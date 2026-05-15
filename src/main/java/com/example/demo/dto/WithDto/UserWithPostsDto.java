package com.example.demo.dto.WithDto;

import com.example.demo.dto.Dto.UserDto;
import com.example.demo.dto.container.PostContainerDto;

public record UserWithPostsDto(UserDto userDto, PostContainerDto postContainerDto) {
}
