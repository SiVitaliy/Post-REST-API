//package com.example.postrestservice.controller;
//
//import com.example.postrestservice.dto.UserDto;
//import com.example.postrestservice.entity.User;
//import com.example.postrestservice.mapper.UserMapper;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//@ControllerAdvice
//public class UserControllerAdvice {
//    private final UserMapper userMapper ;
//
//    public UserControllerAdvice(UserMapper userMapper) {
//        this.userMapper = userMapper;
//    }
//
//    @ModelAttribute("currentUser")
//    public UserDto getCurrentUser(@AuthenticationPrincipal User user){
//        return userMapper.toDto(user);
//    }
//}
////