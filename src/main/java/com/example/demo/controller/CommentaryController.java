package com.example.demo.controller;

import com.example.demo.dto.Dto.CommentaryDto;
import com.example.demo.dto.request.CommentaryRequest.CreateCommentaryRequest;
import com.example.demo.dto.request.CommentaryRequest.UpdateCommentaryRequest;
import com.example.demo.dto.request.PostRequest.UpdatePostRequest;
import com.example.demo.entity.User;
import com.example.demo.service.CommentaryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentaryController {
    private final CommentaryService commentaryService;
    public CommentaryController(CommentaryService commentaryService) {
        this.commentaryService = commentaryService;
    }


    @GetMapping("/commentaries/{id}")
    public ResponseEntity<CommentaryDto> findById(@PathVariable int id){
        return ResponseEntity.ok(commentaryService.findById(id));
    }


    @PostMapping("/posts/{postId}")
    public ResponseEntity<CommentaryDto> save(@PathVariable int postId, @RequestBody @Valid CreateCommentaryRequest request, @AuthenticationPrincipal User user){

        return ResponseEntity.ok(commentaryService.save(postId,request,user));
    }

    @PutMapping("/commentaries/{id}")
    public ResponseEntity<CommentaryDto> update(@PathVariable int id, @RequestBody @Valid UpdateCommentaryRequest request){
        return ResponseEntity.ok(commentaryService.update(id,request));
    }


    @DeleteMapping("/commentaries/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id,@AuthenticationPrincipal User user){
        if (commentaryService.userIsCommentaryOrPostAuthor(id,user)){
            commentaryService.delete(id);
            return ResponseEntity.noContent().build();
        }
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


}
