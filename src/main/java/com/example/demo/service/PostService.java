package com.example.demo.service;

import com.example.demo.dto.WithDto.PostWithCommentariesDto;
import com.example.demo.dto.container.CommentaryContainerDto;
import com.example.demo.dto.container.PostContainerDto;
import com.example.demo.dto.Dto.PostDto;
import com.example.demo.dto.request.PostRequest.CreatePostRequest;
import com.example.demo.dto.request.PostRequest.UpdatePostRequest;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.mapper.CommentaryMapper;
import com.example.demo.mapper.PostMapper;
import com.example.demo.repository.CommentaryRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final CommentaryRepository commentaryRepository;

    private final CommentaryMapper commentaryMapper;

    @Autowired
    public PostService(PostMapper postMapper, PostRepository postRepository, UserRepository userRepository, FileStorageService fileStorageService, CommentaryRepository commentaryRepository, CommentaryMapper commentaryMapper) {
        this.postMapper = postMapper;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.commentaryRepository = commentaryRepository;
        this.commentaryMapper = commentaryMapper;
    }

    public PostContainerDto findAll(){
        List<PostDto> posts= postRepository.findAllWithImages().stream().map(postMapper::toDto).collect(Collectors.toList());
        return new PostContainerDto(posts);
    }
    public PostWithCommentariesDto findPostWithCommentariesById(int id){
        Post post =postRepository.findByIdWithImages(id).orElseThrow(()->new IllegalArgumentException("Post with id "+id+" not found"));
        System.out.println("!!"+post.getImages().toString());
        PostDto postDto = postMapper.toDto(post);
        System.out.println(postDto);
        CommentaryContainerDto commentaries= commentaryMapper.toContainerDto(commentaryRepository.findByPostIdOrderByCreationDate(id));
          return postMapper.toPostWithCommentariesDto(postDto,commentaries);
    }
    public Post findById(int id){
        return postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Post with id "+id+" not found"));
    }
    public Post findByIdWithImages(int id){
        return postRepository.findByIdWithImages(id).orElseThrow(()->new IllegalArgumentException("Post with id "+id+" not found"));
    }

    public PostContainerDto findAllByAuthor(User author){
        List<PostDto> posts = postRepository.findAllByAuthor(author).stream().map(postMapper::toDto).toList();
        return new PostContainerDto(posts);
    }


    public PostDto save(CreatePostRequest request, User user){
        Post post =  postMapper.toEntity(request,user);
        System.out.println(post.getImages());
        return postMapper.toDto(postRepository.save(post));
    }

    public PostDto update(int id, UpdatePostRequest request){ //TODO
        return postRepository.findById(id).map(
                (existingPost)-> {
                    Post updatedPost = postMapper.toEntity(existingPost,request);
                    return postMapper.toDto(postRepository.save(updatedPost));
                }).orElseThrow(()-> new IllegalArgumentException("Post with id "+id+" not found"));

    }

    public void delete(int id){
        fileStorageService.deletePostImages(id);
        postRepository.deleteById(id);
    }

    public boolean userIsAuthor(int id, User user) {
        int authorId = postRepository.findAuthorIdByPostId(id).orElseThrow(()->
                new IllegalArgumentException("Post with id "+id+" not found"));
        return user.getId()==authorId;
    }

    public PostDto addImage(int postId, List<MultipartFile> images) {
        Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("Post with id"+postId+" not found"));
        fileStorageService.saveImages(images,post);

        return postMapper.toDto(post);

    }
}
