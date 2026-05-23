package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.Dto.JwtResponseDto;
import com.example.demo.dto.Dto.UserDto;
import com.example.demo.dto.WithDto.UserWithPostsDto;
import com.example.demo.dto.WithDto.UserWithTokenDto;
import com.example.demo.dto.container.PostContainerDto;
import com.example.demo.dto.container.UserContainerDto;
import com.example.demo.dto.request.UserRequest.UpdateUserRequest;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, PostService postService, UserMapper userMapper, JwtUtil jwtUtil) {
        this.userService = userService;
        this.postService = postService;

        this.userMapper = userMapper;

        this.jwtUtil = jwtUtil;
    }
    @GetMapping("/users")
    public ResponseEntity<UserContainerDto> getAllUsers(@RequestParam(required = false) String search){
        return ResponseEntity.ok(userService.getAllUsers(search));
    }

//    @PostMapping("/auth/login")
//    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        String password = request.get("password");
//        User user = userService.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        // check password (you need to add password check, but for demo we skip)
//        String token = jwtUtil.generateToken(email);
//        Map<String, String> response = new HashMap<>();
//        response.put("token", token);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/auth/register")
//    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
//        User user = userService.register(request.get("email"), request.get("password"));
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal User user) {
        System.out.println(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
    @GetMapping("/me/posts")
    public ResponseEntity<UserWithPostsDto> getMyPosts(@AuthenticationPrincipal User user) {
        PostContainerDto posts = postService.findAllByAuthor(user);
        System.out.println(user.toString());
        System.out.println(userMapper.toDto(user));
        return ResponseEntity.ok(new UserWithPostsDto(userMapper.toDto(user),posts));
    }
    @GetMapping("user/id{id}")
    public ResponseEntity<UserWithPostsDto> getUserWithPostsById(@PathVariable int id ){
        User user = userService.findById(id);
        UserDto userDto= userMapper.toDto(user);
        PostContainerDto postContainerDto = postService.findAllByAuthor(user);
        return ResponseEntity.ok(new UserWithPostsDto(userDto,postContainerDto));
    }
//    @GetMapping("/user/{email}")
//    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
//        User user = userService.findByEmail(email);
//        UserDto userDto = userMapper.toDto(user);
//        return ResponseEntity.ok(userDto);
//    }


    @PostMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUserProfilePicture(@RequestParam("profilePicture")
                                                                       MultipartFile profilePicture,@AuthenticationPrincipal User user){

        UserDto userDto = userService.setNewProfilePicture(profilePicture,user);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/me")
    public  ResponseEntity<UserWithTokenDto> updateCurrentUser(@RequestBody @Valid UpdateUserRequest request,
                                                               @AuthenticationPrincipal User user){

        UserDto updatedUser = userService.updateUser(user,request);
        String token = jwtUtil.generateToken(updatedUser.email());

        return ResponseEntity.ok(new UserWithTokenDto(updatedUser,new JwtResponseDto(token)));
    }
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser(@AuthenticationPrincipal User user){
        System.out.println("controller");
        userService.deleteUserById(user.getId());
        return ResponseEntity.noContent().build();
    }


}