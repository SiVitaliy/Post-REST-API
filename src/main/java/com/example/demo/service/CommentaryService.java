package com.example.demo.service;

import com.example.demo.dto.Dto.CommentaryDto;
import com.example.demo.dto.request.CommentaryRequest.CreateCommentaryRequest;
import com.example.demo.dto.request.CommentaryRequest.UpdateCommentaryRequest;
import com.example.demo.entity.Commentary;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.mapper.CommentaryMapper;
import com.example.demo.mapper.PostMapper;
import com.example.demo.repository.CommentaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional

public class CommentaryService {
    private final CommentaryRepository commentaryRepository;
    private final CommentaryMapper commentaryMapper;
    private final PostService postService;
    private final PostMapper postMapper;

    public CommentaryService(CommentaryRepository commentaryRepository, CommentaryMapper commentaryMapper, PostService postService, PostMapper postMapper) {
        this.commentaryRepository = commentaryRepository;
        this.commentaryMapper = commentaryMapper;
        this.postService = postService;
        this.postMapper = postMapper;
    }

    public CommentaryDto findById(int id) {
        Commentary commentary = commentaryRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Commentary with id "+ id+" not found"));
        return commentaryMapper.toDto(commentary);

    }

    public CommentaryDto save(int postId, CreateCommentaryRequest request, User user){
        Post post = postService.findByIdWithImages(postId);
        Commentary commentary = commentaryMapper.toEntity(request,post,user);
        return commentaryMapper.toDto(commentaryRepository.save(commentary));
    }

    public CommentaryDto update(int id, UpdateCommentaryRequest request) {
        return commentaryRepository.findById(id).map(
                (existingCommentary)-> {
                    Commentary updatedCommentary = commentaryMapper.toEntity(existingCommentary,request);
                    return commentaryMapper.toDto(updatedCommentary);
                }
                    ).orElseThrow(()-> new IllegalArgumentException("Commentary with id "+ id+" not found"));
    }


    public boolean userIsCommentaryOrPostAuthor(int id, User user) {
        int postAuthorId = 0;
        int commentaryAuthorId = 0;
        try {
            postAuthorId = commentaryRepository.findPostAuthorIdByCommentaryId(id).orElseThrow(()->
                    new IllegalArgumentException("Author of post with commentary with id "+id+" not found"));
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        try {
            commentaryAuthorId = commentaryRepository.findAuthorIdByCommentaryId(id).orElseThrow(()->
                    new IllegalArgumentException("Author of commentary with id "+id+" not found"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return user.getId()==commentaryAuthorId || user.getId()==postAuthorId;
    }

    public void delete(int id) {
        commentaryRepository.deleteById(id);
    }


}
