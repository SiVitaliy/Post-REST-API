package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.Dto.JwtResponseDto;
import com.example.demo.dto.request.UserRequest.LoginUserRequest;
import com.example.demo.dto.request.UserRequest.RegisterUserRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public ResponseEntity<JwtResponseDto> registration(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        userService.register(registerUserRequest);
        String token = jwtUtil.generateToken(registerUserRequest.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponseDto(token));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginUserRequest loginUserRequest) {
        User user = null;
        try {
            user = userService.findByEmail(loginUserRequest.email());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (!passwordEncoder.matches(loginUserRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(loginUserRequest.email());
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

//    @GetMapping("/credentials")
//    @PreAuthorize("#email == principal.username")  // ← только для себя
//    public ResponseEntity<String> getUserPassword(@RequestParam String email) {
//        User user = userService.findByEmail(email);
//        return ResponseEntity.ok(user.getPassword());
//    }
}