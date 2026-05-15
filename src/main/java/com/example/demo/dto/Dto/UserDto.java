package com.example.demo.dto.Dto;

import java.time.LocalDate;

public record UserDto(int id, String fullName, String email,String profilePictureUrl,
                      LocalDate yearOfBirth,String role, String countryCode, String bio ) {



}