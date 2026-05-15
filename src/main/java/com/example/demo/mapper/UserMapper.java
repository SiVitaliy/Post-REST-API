package com.example.demo.mapper;

import com.example.demo.dto.Dto.CommentaryDto;
import com.example.demo.dto.Dto.UserDto;
import com.example.demo.dto.container.CommentaryContainerDto;
import com.example.demo.dto.container.UserContainerDto;
import com.example.demo.dto.request.UserRequest.UpdateUserRequest;
import com.example.demo.entity.Commentary;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")

public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "role",ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "commentaries", ignore = true)
    User toEntity(@MappingTarget User user, UpdateUserRequest request);

    List<UserDto> toDtoList(List<User> users);

    default UserContainerDto toContainerDto(List<User> users) {
        return new UserContainerDto(toDtoList(users));
    }


}
