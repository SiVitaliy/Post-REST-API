package com.example.demo.controller;

import com.example.demo.dto.WithDto.PostWithCommentariesDto;
import com.example.demo.dto.container.PostContainerDto;
import com.example.demo.dto.Dto.PostDto;
import com.example.demo.dto.request.PostRequest.CreatePostRequest;
import com.example.demo.dto.request.PostRequest.UpdatePostRequest;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }
    @GetMapping("/posts")
    public PostContainerDto findAll(){
        return postService.findAll();
    }
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostWithCommentariesDto> findById(@PathVariable int id){

        return ResponseEntity.ok(postService.findPostWithCommentariesById(id));
    }
    @PostMapping("/posts/{postId}/images")
    public ResponseEntity<PostDto> addImages(@RequestParam List<MultipartFile> images,
                                             @PathVariable int postId,
                                             @AuthenticationPrincipal User user){
        if (postService.userIsAuthor(postId,user)){
            PostDto postDto  = postService.addImage(postId, images);
            return  ResponseEntity.ok(postDto);
        }
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @PostMapping("/posts")
    public ResponseEntity<PostDto> save(@RequestBody CreatePostRequest request, @AuthenticationPrincipal User user){
        return ResponseEntity.ok(postService.save(request,user));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> update(@PathVariable int postId, @RequestBody  UpdatePostRequest request, @AuthenticationPrincipal User user){


        if (postService.userIsAuthor(postId,user)){
            return  ResponseEntity.ok(postService.update(postId,request));
        }
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id, @AuthenticationPrincipal User user){

        if (postService.userIsAuthor(id,user)){
            postService.delete(id);
            return ResponseEntity.noContent().build();
        }
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }



}
