package com.example.demo.dto.container;

import com.example.demo.dto.Dto.UserDto;

import java.util.List;

public class UserContainerDto {
    private final List<UserDto> users;

    public UserContainerDto(List<UserDto> users) {
        this.users = users;
    }

    public List<UserDto> getUsers() {
        return users;
    }
}