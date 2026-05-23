package com.example.demo.dto.WithDto;

import com.example.demo.dto.Dto.JwtResponseDto;
import com.example.demo.dto.Dto.UserDto;

public record UserWithTokenDto(UserDto userDto, JwtResponseDto jwt) {
}