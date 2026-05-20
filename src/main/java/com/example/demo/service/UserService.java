package com.example.demo.service;

import com.example.demo.dto.Dto.UserDto;
import com.example.demo.dto.container.UserContainerDto;
import com.example.demo.dto.request.UserRequest.RegisterUserRequest;
import com.example.demo.dto.request.UserRequest.UpdateUserRequest;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.EmailAlreadyExistsException;
import jakarta.validation.ValidationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserMapper userMapper;

    private final FileStorageService fileStorageService;

    public UserService(UserRepository userRepository, UserMapper userMapper, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.fileStorageService = fileStorageService;
    }

    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("User with id "+id+" is not found"));
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User with email  "+email+" is not found"));
    }

    public User register(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByEmail(registerUserRequest.email())) {
            throw new EmailAlreadyExistsException("Пользователь с такой почтой уже существует");
        }
        User user = new User();
        user.setFullName(registerUserRequest.fullName());
        user.setEmail(registerUserRequest.email());
        user.setPassword(passwordEncoder.encode(registerUserRequest.password()));
        user.setCreationDate(LocalDateTime.now());
        user.setRole("USER");
        return userRepository.save(user);
    }

    public UserDto updateUser(User user, UpdateUserRequest request) {
        if (!user.getEmail().equals(request.email()) &&userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Пользователь с такой почтой уже существует");
        }
        if (request.yearOfBirth() != null
                && request.yearOfBirth().isBefore(LocalDate.now().minusYears(120))) {
            throw new ValidationException("Дата рождения выглядит некорректной");
        }
        return userMapper.toDto(userRepository.save(userMapper.toEntity(user,request)));
    }



    public void deleteUserById(int id) {
        userRepository.deleteById(id);

    }

    public UserContainerDto getAllUsers(String search) {
        if (search!=null && !search.isBlank()){
            return userMapper.toContainerDto(userRepository.findUsersWithSearch(search));
        }
        return userMapper.toContainerDto(userRepository.findAll());
    }

    public UserDto setNewProfilePicture(MultipartFile profilePicture, User user) {
        if (user.getProfilePictureUrl() != null) {
            String oldFilename = extractFilename(user.getProfilePictureUrl());
            fileStorageService.deleteProfilePicture(oldFilename);
        }
        String pictureUrl = fileStorageService.saveProfilePicture(user.getId(),profilePicture);
        user.setProfilePictureUrl(pictureUrl);
        userRepository.save(user);
        return userMapper.toDto(user);


    }
    private String extractFilename(String avatarUrl) {
        return avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
    }
}